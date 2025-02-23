package engine;

import java.util.*;
import java.util.stream.Collectors;

public class TableSchema {
    SchemaMember primaryKey;
    Map<String, SchemaMember> schemaMembers;
    Map<String, Integer> mapping;

    public TableSchema() {
        this.primaryKey = null;
        this.schemaMembers = new HashMap<>();
        this.mapping = new HashMap<>();
    }

    public void addPrimaryKey(String column) {
        primaryKey = new SchemaMember(0, column, schemaMembers.get(column).columnType, true, true);
        this.mapping.put(column, 0);
    }

    public void addColumn(int pos, String column, DataType type, boolean isRequired, boolean isNullable) {
        schemaMembers.put(column, new SchemaMember(pos, column, DataType.INT, isRequired, isNullable));
        this.mapping.put(column, pos);
    }

    public SchemaMember getSchemaFor(String column) {
        return schemaMembers.get(column);
    }

    public List<String> getAllColumns() {
        List<SchemaMember> values = new ArrayList<>(schemaMembers.values());
        values.sort(Comparator.comparingInt(SchemaMember::getColPos));
        return values.stream().map(SchemaMember::getColumnName).toList();

    }

    public Map<String, Integer> getAllColumnsPos() {
        return mapping;

    }
}
