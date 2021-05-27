package Database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OutToConsole {
    public static void fixedLengthPrint(String input, int length){
        System.out.printf("%1$" + length + "s", input);
    }

    public static void fixedLengthPrint(int input, int length){
        System.out.printf("%1$" + length + "s", input);
    }

    public static void printWord(ResultSet rs){
        try {
            while(rs.next()){
                fixedLengthPrint(rs.getString("word"), 30);
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void printOut(ResultSet rs){
        try{
            while(rs.next()){
                fixedLengthPrint(rs.getString("word") + " | ", 30);
                fixedLengthPrint("Nghĩa: " + rs.getString("description"), 30);
                System.out.println();
            }
        } catch (SQLException eSqlException){
            System.out.println(eSqlException.getMessage());
        }
    }


}
