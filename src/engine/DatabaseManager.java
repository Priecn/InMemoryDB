package engine;

import exception.ErrorResponse;
import exception.DatabaseMangerException;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private final Map<StringData, Database> databaseMap;
    public DatabaseManager() {
        databaseMap = new HashMap<>();
    }
    public Database getDatabase(StringData databaseName) throws DatabaseMangerException {
        if (!databaseMap.containsKey(databaseName)) {
            throw new DatabaseMangerException(new ErrorResponse(1002, "Database does not exists."));
        }
        return databaseMap.get(databaseName);
    }

    public void createDatabase(StringData databaseName) throws DatabaseMangerException {
        if (databaseMap.containsKey(databaseName)) {
            throw new DatabaseMangerException(new ErrorResponse(1001, "Database already present with same name"));
        }
        databaseMap.put(databaseName, new Database());
    }

    public void deleteDatabase(StringData databaseName, boolean forceDeletion) throws DatabaseMangerException {
        if(!databaseMap.containsKey(databaseName)) {
            throw new DatabaseMangerException(new ErrorResponse(1005, "No database present to delete"));
        }
        databaseMap.remove(databaseName);
    }
}
