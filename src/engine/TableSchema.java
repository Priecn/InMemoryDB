package engine;

import java.util.HashMap;
import java.util.Map;

public class TableSchema {
    SchemaMember primaryKey;
    Map<String, SchemaMember> schemaMembers;

    public TableSchema() {
        this.primaryKey = null;
        this.schemaMembers = new HashMap<>();
    }

    public void addPrimaryKey(String column) {
        primaryKey = new SchemaMember(column, schemaMembers.get(column).columnType, true, true);
    }

    public void addColumn(String column, DataType type, boolean isRequired, boolean isNullable) {
        schemaMembers.put(column, new SchemaMember(column, DataType.INT, true, false));
    }

    public SchemaMember getSchemaFor(String column) {
        return schemaMembers.get(column);
    }
}
