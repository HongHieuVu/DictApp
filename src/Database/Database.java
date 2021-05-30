package Database;

import Database.DBExceptions.InvalidQuery;
import Database.DBExceptions.NoResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String WORD = "word";
    private static final String MEANING = "description";
    private static final String WEB_STYLE = "html";

    //query customizations (query types)
    private static final int EXACT_MATCH = 0;
    private static final int STARTS_WITH = 1;

    private static Connection database = null;

    /**
     * constructor guarantees only one connection is made to database
     */
    public Database() {
        if (database == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                database = DriverManager.getConnection("jdbc:sqlite:Database/dictDB.db");
                System.out.println("Connected to database");
            } catch (SQLException e) {
                System.out.println(e + " Error code: " + e.getErrorCode());
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * searches the database for words beginning with input string. Method doesn't care if there's
     * no result.
     *
     * @param word word to search
     * @return a list of unique words beginning with parameter "word"
     */
    public List<Word> searchInitialEV(String word) {
        String query = " SELECT word, description FROM av WHERE word LIKE ?;";
        return queryDatabase(query, word, STARTS_WITH);
    }

    /**
     * search for all definitions of a word.
     *
     * @param word word to be searched
     * @return a list of unique word objects with definitions
     * @throws NoResult when query list returned empty
     */
    public List<Word> searchWordEV(String word) throws NoResult {
        String query = " SELECT word, description, html, COUNT(*) OVER() AS resNum " +
                "FROM av WHERE word LIKE ?;";
        List<Word> resultList = queryDatabase(query, word, EXACT_MATCH);

        //handles if there's no result
        if (resultList.isEmpty()) throw new NoResult("No definition in database");

        return resultList;
    }


    /**
     * executes a custom query statement to get a set of unique results. To compare results, it is
     * mandatory to search for the word AND the meaning.
     *
     * @param query      the query to execute, but with word parameter replaced with question mark
     * @param word       word to search for
     * @param searchType specific search type, one of defined at the start of file
     * @return list of unique results
     */
    private List<Word> queryDatabase(String query, String word, int searchType){
        //validate query
        if (!query.contains(WORD) | !query.contains(MEANING)) return new ArrayList<>();

        //handles custom search type
        if (word.equals(""))
            return new ArrayList<>(); //to prevent STARTS_WITH search type from getting the entire database
        if (searchType == STARTS_WITH) word += "%";

        try {
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setString(1, word);
            ResultSet rs = preparedStatement.executeQuery();

            //append result
            List<Word> resultList = new ArrayList<>();
            while (rs.next()) {
                Word thisWord = new Word(rs.getString(WORD), rs.getString(MEANING));
                if (columnExists(rs, WEB_STYLE)) thisWord.setHtml(rs.getString(WEB_STYLE));
                if (!thisWord.duplicated(resultList)) resultList.add(thisWord);
            }

            return resultList;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    /**
     * checks if a column exists in a query
     *
     * @param rs      result set
     * @param colName name to check against
     * @return true if column is in query
     * @throws SQLException when database is corrupt
     */
    private boolean columnExists(ResultSet rs, String colName) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            if (rsmd.getColumnName(i).equals(colName)) return true;
        }
        return false;
    }
}