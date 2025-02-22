package engine;

public class SchemaMember {
    String columnName;
    DataType columnType;
    boolean isRequired;
    boolean isNullable;
    public SchemaMember(String columnName, DataType columnType, boolean isRequired,
                        boolean isNullable) {
        this.columnType = columnType;
        this.isRequired = isRequired;
        this.isNullable = isNullable;
        this.columnName = columnName;
    }
}
