package index;

import engine.RowFilter;
import engine.RowFilterType;

import java.util.*;

public class MapIndex implements TableIndex{

    TreeMap<Object, Set<Object>> index;
    String columnName;

    public MapIndex(String columnName) {
        this.columnName = columnName;
        index = new TreeMap<>();
    }

    @Override
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
    public Set<Object> getFilteredData(RowFilter rowFilter) {
        if (rowFilter.getType() == RowFilterType.EQ) {
            return index.get(rowFilter.getFilterData());
        } else if (rowFilter.getType() == RowFilterType.LTE) {
            Object start = index.firstKey();
            Object end = index.floorKey(rowFilter.getFilterData());

            SortedMap<Object, Set<Object>> filtered = index.subMap(start, end);

            return new HashSet<>(filtered.values());
        } else if (rowFilter.getType() == RowFilterType.GTE) {
            Object start = index.ceilingKey(rowFilter.getFilterData());
            Object end = index.lastKey();

            SortedMap<Object, Set<Object>> filtered = index.subMap(start, end);

            return new HashSet<>(filtered.values());
        }
        return new HashSet<>();
    }

    @Override
    public Set<Object> get(Object column) {
        return index.get(column);
    }
}
