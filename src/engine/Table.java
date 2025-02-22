package engine;

import exception.DatabaseMangerException;
import exception.ErrorResponse;
import index.TableIndex;

import java.util.*;
import java.util.stream.Collectors;

public class Table {
    private final TableSchema tableSchema;
    private final String tableName;
    private final Map<Object, Row> data;
    private final Map<String, TableIndex> indexMap;

    public Table(TableSchema tableSchema, String tableName) {
        this.tableSchema = tableSchema;
        this.tableName = tableName;
        this.data = new TreeMap<>();
        indexMap = new HashMap<>();
    }

    public TableSchema getTableSchema() {
        return tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public Map<Object, Row> getData() {
        return data;
    }

    public int insertRow(Map<String, String> rowDataToInsert) throws DatabaseMangerException {
        Object primaryKey = null;
        String primaryKeyColumnName = tableSchema.primaryKey.columnName;

        List<Object> rowData = new ArrayList<>();
        for (Map.Entry<String, String> en: rowDataToInsert.entrySet()) {

            SchemaMember schemaMember = tableSchema.getSchemaFor(en.getKey());
            if (en.getValue() == null) {
                if (schemaMember.isRequired) {
                    throw new DatabaseMangerException(new ErrorResponse(1004, "NOT_NULL constraint validation failed."));
                } else {
                    rowData.add(null);
                }
            } else {
                if (en.getKey().equalsIgnoreCase(primaryKeyColumnName)) {
                    primaryKey = en.getValue();
                }
                if (schemaMember.columnType == DataType.INT) {
                    try {
                        rowData.add(Integer.parseInt(en.getValue()));
                    } catch (Exception e) {
                        new ErrorResponse(1006, "Data type mismatch: " + e.getMessage());
                    }
                } else {
                    rowData.add(en.getValue());
                }
            }
        }

        data.put(primaryKey, new Row(rowData));

        return 1;
    }

    public List<List<String>> get(List<String> columns, List<RowFilter> filters ) {
        List<TableIndex> indicesToApply = new ArrayList<>();

        for (RowFilter rowFilter: filters) {
            indicesToApply.addAll(indexMap.values().stream()
                    .filter(index -> index.getColumnName().equalsIgnoreCase(rowFilter.column)).toList());
        }

        // TODO apply indicesToApply based on filters to fetch list of primary keys
        // Then from data map fetch actual data

        return null;
    }

    private void updateIndex(Map<String, String> rowDataToInsert) {

        String primaryKeyColumnName = tableSchema.primaryKey.columnName;
        Object primaryKey = rowDataToInsert.get(primaryKeyColumnName);

        for(Map.Entry<String, TableIndex> en: indexMap.entrySet()) {

            String columnName = en.getValue().getColumnName();

            Object valueToAdd = rowDataToInsert.get(columnName);

            en.getValue().add(valueToAdd, primaryKey);
        }
    }
}
