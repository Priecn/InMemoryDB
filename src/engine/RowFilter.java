package engine;

import java.util.List;

public class RowFilter {
    String column;
    RowFilterType type;

    public RowFilter(String column, RowFilterType type) {
        this.column = column;
        this.type = type;
    }

}
