package engine;

public class SchemaMember {
    int colPos;
    String columnName;
    DataType columnType;
    boolean isRequired;
    boolean isNullable;
    public SchemaMember(int colPos, String columnName, DataType columnType, boolean isRequired,
                        boolean isNullable) {
        this.columnType = columnType;
        this.isRequired = isRequired;
        this.isNullable = isNullable;
        this.columnName = columnName;
        this.colPos = colPos;
    }

    public int getColPos() {
        return colPos;
    }

    public String getColumnName() {
        return columnName;
    }
}
