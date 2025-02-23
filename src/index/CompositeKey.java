package index;

import engine.DataType;
import engine.Database;

import java.util.List;
import java.util.Map;
import java.util.Objects;

class CompositeKey implements Comparable<CompositeKey> {
    private final List<Object> keys;
    private final Object primaryKey;
    private final CompositeKeySchema compositeKeySchema;

    public CompositeKey(List<Object> keys, Object primaryKey, CompositeKeySchema compositeKeySchema) {
        this.keys = keys;
        this.primaryKey = primaryKey;
        this.compositeKeySchema = compositeKeySchema;
    }

    public List<Object> getKeys() {
        return keys;
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CompositeKey that = (CompositeKey) obj;

        if (this.keys.size() != that.keys.size()) return false;
        for (int i=0; i<keys.size(); i++) {
            if (!keys.get(i).equals(that.keys.get(i))) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(keys);
    }

    @Override
    public int compareTo(CompositeKey other) {
        if (keys.size() != other.keys.size()) {
            return Integer.compare(keys.size(), other.keys.size());
        }

        for (int i = 0; i < keys.size(); i++) {
            DataType dataType = compositeKeySchema.getKeysInIndexWithType().get(i);
            if (dataType == DataType.INT) {
                Integer v1 = (Integer)keys.get(i);
                Integer v2 = (Integer)other.keys.get(i);
                int comparison =  v1.compareTo(v2);

                if (comparison != 0) {
                    return comparison;
                }
            } else {
                String v1 = (String)keys.get(i);
                String v2 = (String)other.keys.get(i);
                int comparison =  v1.compareTo(v2);

                if (comparison != 0) {
                    return comparison;
                }
            }

        }

        return 0;
    }
}

