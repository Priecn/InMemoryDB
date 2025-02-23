package engine;

import java.util.List;

public class RowFilter {
    String column;
    RowFilterType type;
    Object filterData;
    DataType dataType;

    public RowFilter(String column, RowFilterType type, Object filterData) {
        this.column = column;
        this.type = type;
        this.filterData = filterData;
    }

    public String getColumn() {
        return column;
    }

    public RowFilterType getType() {
        return type;
    }

    public Object getFilterData() {
        return filterData;
    }

    public DataType getDataType() {
        return dataType;
    }
}
