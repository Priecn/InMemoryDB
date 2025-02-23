package index;

import engine.DataType;

import java.util.Map;

public class CompositeKeySchema {
    private final Map<Integer, DataType> keysInIndexWithType;

    public CompositeKeySchema(Map<Integer, DataType> keysInIndexWithType) {
        this.keysInIndexWithType = keysInIndexWithType;
    }

    public Map<Integer, DataType> getKeysInIndexWithType() {
        return keysInIndexWithType;
    }
}
