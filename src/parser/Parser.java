package parser;

import common.QueryResponse;
import engine.Database;
import engine.RowFilter;
import engine.RowFilterType;
import engine.TableSchema;
import exception.DatabaseMangerException;

import java.util.*;

public class Parser {

    Database database;

    public QueryResponse parse(List<String> tokens) throws DatabaseMangerException {

        String queryType = tokens.getFirst().toUpperCase();

        switch (queryType) {
            case "CREATE":
                database.createTable(tokens);
                return new QueryResponse(200, "Table created.");
            case "INSERT":
                String tableName = tokens.get(2);
                TableSchema tableSchema = database.getTable(tableName).getTableSchema();

                Map<String, String> row = new HashMap<>();

                List<String> allColumns = tableSchema.getAllColumns();

                for (String col: allColumns) {
                    row.put(col, null);
                }

                int columnIndex = 4;
                int valueIndex = tokens.indexOf("VALUES") + 2;

                while (valueIndex < tokens.size()-1) {
                    row.put(tokens.get(columnIndex), tokens.get(valueIndex));
                    columnIndex++;
                    valueIndex++;
                }

                return new QueryResponse(200, database.getTable(tableName).insertRow(row) + " inserted");

            case "SELECT":

                tableName = tokens.get(tokens.indexOf("FROM")+1);

                List<Integer> columns = new ArrayList<>();
                List<String> orderedCol = database.getTable(tableName).getTableSchema().getAllColumns();

                if (tokens.get(1).equals("*")) {
                    orderedCol.forEach(col -> columns.add(database.getTable(tableName).getTableSchema().getAllColumnsPos().get(col)));
                } else {
                    int index = 1;
                    while(!tokens.get(index).equals("FROM")) {
                        columns.add(database.getTable(tableName).getTableSchema().getAllColumnsPos().get(tokens.get(index)));
                    }
                }

                List<RowFilter> filters = new ArrayList<>();
                int filterIndex = tokens.indexOf("WHERE")+1;
                if (filterIndex > 0) {
                    while (filterIndex < tokens.size()) {
                        String col = tokens.get(filterIndex++);
                        RowFilterType filterType = RowFilterType.getRowFilterType(tokens.get(filterIndex++));
                        Object filterData = tokens.get(filterIndex++);
                        RowFilter rowFilter = new RowFilter(col, filterType, filterData);
                        filters.add(rowFilter);
                    }
                }

                if (filters.isEmpty()) {
                    return new QueryResponse(200, database.getTable(tableName).getData());
                }

                return new QueryResponse(200, database.getTable(tableName).select(columns, filters));

        }

        return null;
    }
}
