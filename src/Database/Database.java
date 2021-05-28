package Database;

import Database.DBExceptions.NoResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String WORD = "word";
    private static final String MEANING = "description";
    private static final String WEB_STYLE = "html";
    private static Connection database = null;

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
     * searches the database for this word (exact match)
     *
     * @param word word to search
     * @return a word object with definition
     */
    public ResultSet getWordAV(String word) {
        String query = " SELECT word, description, pronounce FROM av WHERE word LIKE ?;";
        try {
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setString(1, word);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * searches the database for words beginning with input string
     *
     * @param word word to search
     * @return a list of words beginning with parameter "word"
     */
    public List<Word> searchInitialEV(String word) {
        String query = " SELECT word, description FROM av WHERE word LIKE ?;";
        try {
            //execute query
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setString(1, word + "%");
            ResultSet rs = preparedStatement.executeQuery();

            //create result list
            List<Word> resultList = new ArrayList<>();
            while (rs.next()) {
                Word wordCreated = new Word(rs.getString(WORD));
                resultList.add(wordCreated);
            }
            return resultList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * search for definition of input string, should only be used to look up definition
     * of a key with only 1 matching word (unique word).
     *
     * @param word word to be searched
     * @return a word object with definition of the word entered
     */
    public List<Word> searchWordEV(String word) throws NoResult {
        String query = " SELECT word, description, html, COUNT(*) OVER() AS resNum " +
                "FROM av WHERE word LIKE ?;";
        try {
            //execute query
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setString(1, word);
            ResultSet rs = preparedStatement.executeQuery();

            //handle if there's no result
            int numResult = Integer.parseInt(rs.getString("resNum"));
            if (numResult == 0) throw new NoResult("No result found for word: " + word);

            //append result
            List<Word> resultList = new ArrayList<>();
            while (rs.next()){
                Word thisWord = new Word(rs.getString(WORD), rs.getString(MEANING), rs.getString(WEB_STYLE));
                if (!thisWord.duplicated(resultList)) resultList.add(thisWord);
            }

            return resultList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

