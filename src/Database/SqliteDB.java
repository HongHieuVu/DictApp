package Database;

import Database.DBExceptions.NoResult;
import Database.DBExceptions.TooManyResults;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteDB {
    private Connection c = null;

    public SqliteDB(){
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Database/dictDB.db");
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println(e + " Error code: " + e.getErrorCode());
        } catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * searches the database for this word (exact match)
     * @param word - word to search
     * @return a word object with definition
     */
    public ResultSet getWordAV(String word){
        //searches the database for this word
        String query = " SELECT word, description, pronounce FROM av WHERE word LIKE ?;";
        try {
            PreparedStatement preparedStatement = this.c.prepareStatement(query);
            preparedStatement.setString(1, word + "%");
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * searches the database for words beginning with input string
     * @param word - word to search
     * @return a list of words beginning with parameter "word"
     */
    public List<Word> searchInitialEV(String word){
        String query = " SELECT word, description FROM av WHERE word LIKE ?;";
        try {
            PreparedStatement preparedStatement = this.c.prepareStatement(query);
            preparedStatement.setString(1, word + "%");
            ResultSet rs = preparedStatement.executeQuery();
            List<Word> resultList = new ArrayList<>();
            while (rs.next()) {
                Word wordCreated = new Word(rs.getString("word"));
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
     * @param word - word to be searched
     * @return a word object with definition of the word entered
     */
    public Word searchWordEV(String word) throws TooManyResults, NoResult {
        String query = " SELECT word, description, COUNT(*) OVER() AS resNum " +
                "FROM av WHERE word LIKE ?;";
        try {
            PreparedStatement preparedStatement = this.c.prepareStatement(query);
            preparedStatement.setString(1, word);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.getString("resNum").equals("1"))
                throw new TooManyResults("search result for \"word\" is not unique");
            else if (rs.getString("resNum").equals("0"))
                throw new NoResult("No result found for word: " + word);
            return new Word(rs.getString("word"), rs.getString("description"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getWordTargetAsHtmlAV(String word){
        //searches the database for this word and returns an answer

        word = word + "%";
        String sql = " SELECT html FROM av WHERE word LIKE ?;";
        try {
            PreparedStatement preparedStatement = this.c.prepareStatement(sql);
            preparedStatement.setString(1, word);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.getString("html");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

