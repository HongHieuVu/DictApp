package Database;

/**
 * Singleton object. One query to execute at a time
 */
public class DatabaseQuery {
    private static DatabaseQuery queryObj = null;

    private DatabaseQuery(){}

    public DatabaseQuery createQuery(){
        if (queryObj == null) return new DatabaseQuery();
        return queryObj;
    }


}
