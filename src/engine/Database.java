package engine;

import exception.DatabaseMangerException;
import exception.ErrorResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    String databaseName;
    Map<String, Table> tableMap;
    public Database() {
        tableMap = new HashMap<>();
    }

    public Table getTable(String tableName) {
        return tableMap.get(tableName);
    }
    public void createTable(List<String> tokens) throws DatabaseMangerException {
        String tableName = tokens.get(2);

        if (!tableMap.containsKey(tableName)) {
            throw new DatabaseMangerException(new ErrorResponse(1003, "Table already exists."));
        }

        TableSchema tableSchema = new TableSchema();

        int index = 4;

        while (index < tokens.size()-1) {
            List<String> current = new ArrayList<>();

            while (!tokens.get(index).equals(",")) {
                current.add(tokens.get(index));
                index++;
            }

            // PRIMARY KEY ColumnName
            if ("PRIMARY_KEY".equalsIgnoreCase(current.getFirst())) {
                tableSchema.addPrimaryKey(current.get(1));
            } else {
                // columnName columnType CONSTRAINT1 CONSTRAINT2
                String columnName = current.getFirst();
                DataType type = "STRING".equalsIgnoreCase(current.get(1)) ? DataType.STRING : DataType.INT;
                boolean isNullable = true;
                boolean isRequired = false;

                if (current.size() > 2) {
                    if ("NOT_NULL".equalsIgnoreCase(current.get(2))) {
                        isNullable = false;
                    } else if ("REQUIRED".equalsIgnoreCase(current.get(2))) {
                        isRequired = true;
                    }
                }

                if (current.size() > 3) {
                    if ("NOT_NULL".equalsIgnoreCase(current.get(3))) {
                        isNullable = false;
                    } else if ("REQUIRED".equalsIgnoreCase(current.get(3))) {
                        isRequired = true;
                    }
                }

                tableSchema.addColumn(columnName, type, isRequired, isNullable);
            }

            index++;
        }



        tableMap.put(tableName, new Table(tableSchema, tableName));
    }
    public String getDatabaseName() {
        return this.databaseName;
    }
}
