package index;

import engine.DataType;
import engine.Database;
import engine.RowFilter;
import engine.RowFilterType;

import java.util.*;

public class BTreeIndex implements TableIndex{
    private BTreeIndexNode root;
    private int t;

    public BTreeIndex(int t) {
        this.t = t;
        this.root = new BTreeIndexNode(t, true); // Create a root node as a leaf
    }

    @Override
    public void add(Object key, Object primaryKey) {
        // If root is full, create a new root
        if (root.numKeys == 2 * t - 1) {
            BTreeIndexNode newNode = new BTreeIndexNode(t, false);
            newNode.children[0] = root;
            splitChild(newNode, 0);
            root = newNode;
        }

        insertNonFull(root, (CompositeKey) key);
    }

    private void insertNonFull(BTreeIndexNode node, CompositeKey key) {
        int i = node.numKeys - 1;

        if (node.isLeaf) {
            // Insert the key in the appropriate position in the leaf node
            while (i >= 0 && key.compareTo(node.keys[i]) < 0) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.numKeys++;
        } else {
            // Find the child to insert the key into
            while (i >= 0 && key.compareTo(node.keys[i]) < 0) {
                i--;
            }
            i++;

            if (node.children[i].numKeys == 2 * t - 1) {
                splitChild(node, i);
                if (key.compareTo(node.keys[i]) > 0) {
                    i++;
                }
            }
            insertNonFull(node.children[i], key);
        }
    }

    @Override
    public Set<Object> get(Object key) {
        Set<Object> result = new HashSet<>();
        findAllNodesWithKey(root, (CompositeKey)key, result);
        return result;
    }

    public Set<Object> getFilteredData(RowFilter rowFilter) {
        Map<Integer, DataType> map = new HashMap<>();
        map.put(0, rowFilter.getDataType());
        CompositeKey key = new CompositeKey(Collections.singletonList(rowFilter.getFilterData()), null, new CompositeKeySchema(map));
        if (rowFilter.getType() == RowFilterType.EQ) {
            return get(key);
        } else if (rowFilter.getType() == RowFilterType.LTE) {
            CompositeKey start = getLowestKey();

            return getAllNodesForKeyRange(start, key);
        } else if (rowFilter.getType() == RowFilterType.GTE) {
            CompositeKey end = getHighestKey();

            return getAllNodesForKeyRange( key, end);
        }
        return new HashSet<>();
    }


    private Set<Object> getAllNodesForKeyRange(CompositeKey lowerBound, CompositeKey upperBound) {
        Set<Object> result = new HashSet<>();
        findNodesInRange(root, lowerBound, upperBound, result);
        return result;
    }


    private void findAllNodesWithKey(BTreeIndexNode node, CompositeKey key, Set<Object> result) {
        int i = 0;
        // Traverse through the keys in the node and find if any key matches
        while (i < node.numKeys && key.compareTo(node.keys[i]) > 0) {
            i++;
        }

        if (i < node.numKeys && key.compareTo(node.keys[i]) == 0) {
            // If we find the key, add the current node to the result
            for (CompositeKey k: node.keys) {
                result.add(k.getPrimaryKey());
            }
        }

        // If the node is not a leaf, search the children
        if (!node.isLeaf) {
            findAllNodesWithKey(node.children[i], key, result);
            // If the key is greater than the current node's key, we need to search the next child
            if (i + 1 < node.numKeys && key.compareTo(node.keys[i + 1]) >= 0) {
                findAllNodesWithKey(node.children[i + 1], key, result);
            }
        }
    }

    private void findNodesInRange(BTreeIndexNode node, CompositeKey lowerBound, CompositeKey upperBound, Set<Object> result) {
        int i = 0;

        // Traverse through the keys in the node
        while (i < node.numKeys && upperBound.compareTo(node.keys[i]) < 0) {
            i++;
        }

        // If the node is within the range, add it to the result
        if (i < node.numKeys && lowerBound.compareTo(node.keys[i]) <= 0 && upperBound.compareTo(node.keys[i]) >= 0) {
            for (CompositeKey k: node.keys) {
                result.add(k.getPrimaryKey());
            }
        }

        // If the node is not a leaf, recursively search its children
        if (!node.isLeaf) {
            for (int j = 0; j <= node.numKeys; j++) {
                if (j < node.numKeys && lowerBound.compareTo(node.keys[j]) > 0) {
                    continue; // Skip the current child if lower bound is greater than the key
                }
                findNodesInRange(node.children[j], lowerBound, upperBound, result);
            }
        }
    }

    private CompositeKey getLowestKey() {
        if (root == null || root.numKeys == 0) {
            return null;  // Empty tree or root node with no keys
        }

        // Traverse the leftmost path to the leaf node
        BTreeIndexNode currentNode = root;
        while (!currentNode.isLeaf) {
            currentNode = currentNode.children[0];  // Move to the leftmost child
        }

        // The smallest key is the first key in the leftmost leaf node
        return currentNode.keys[0];
    }
    private CompositeKey getHighestKey() {
        if (root == null || root.numKeys == 0) {
            return null;  // Empty tree or root node with no keys
        }

        // Traverse the rightmost path to the leaf node
        BTreeIndexNode currentNode = root;
        while (!currentNode.isLeaf) {
            currentNode = currentNode.children[currentNode.numKeys];  // Move to the rightmost child
        }

        // The largest key is the last key in the rightmost leaf node
        return currentNode.keys[currentNode.numKeys - 1];
    }



    private void splitChild(BTreeIndexNode parent, int index) {
        BTreeIndexNode child = parent.children[index];
        BTreeIndexNode newChild = new BTreeIndexNode(t, child.isLeaf);

        parent.children[index + 1] = newChild;
        parent.keys[index] = child.keys[t - 1];
        parent.numKeys++;

        for (int i = 0; i < t - 1; i++) {
            newChild.keys[i] = child.keys[i + t];
        }

        if (!child.isLeaf) {
            for (int i = 0; i < t; i++) {
                newChild.children[i] = child.children[i + t];
            }
        }

        child.numKeys = t - 1;
    }

    // Remove duplicates based on Set uniqueness
    public Set<CompositeKey> toSet() {
        Set<CompositeKey> set = new HashSet<>();
        addKeysToSet(root, set);
        return set;
    }

    private void addKeysToSet(BTreeIndexNode node, Set<CompositeKey> set) {
        if (node != null) {
            for (int i = 0; i < node.numKeys; i++) {
                set.add(node.keys[i]);
            }
            if (!node.isLeaf) {
                for (int i = 0; i <= node.numKeys; i++) {
                    addKeysToSet(node.children[i], set);
                }
            }
        }
    }

    //TODO
    @Override
    public String getColumnName() {
        return "";
    }

    @Override
    public void remove(Object column, Object primaryKey) {

    }
}
