package index;

import java.util.*;

public class MapIndex implements TableIndex{

    Map<Object, Set<Object>> index;
    String columnName;

    public MapIndex(String columnName) {
        this.columnName = columnName;
        index = new TreeMap<>();
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public void add(Object column, Object primaryKey) {
        if (!index.containsKey(column)) {
            index.put(column, new HashSet<>());
        }
        index.get(column).add(primaryKey);
    }

    @Override
    public void remove(Object column, Object primaryKey) {

        index.get(column).remove(primaryKey);

        if (index.get(column).isEmpty()) {
            index.remove(column);
        }

    }

    @Override
    public Set<Object> get(Object column) {
        return index.get(column);
    }
}
