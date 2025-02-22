package index;

import java.util.Set;

public interface TableIndex {
    String getColumnName();
    void add(Object column, Object primaryKey);
    void remove(Object column, Object primaryKey);
    Set<Object> get(Object column);

}
