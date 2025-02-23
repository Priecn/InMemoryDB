package index;

public class BTreeIndexNode {

        CompositeKey[] keys;
        BTreeIndexNode[] children;
        int numKeys;
        boolean isLeaf;

        public BTreeIndexNode(int t, boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.keys = new CompositeKey[2 * t - 1];
            this.children = new BTreeIndexNode[2 * t];
            this.numKeys = 0;
        }
}


