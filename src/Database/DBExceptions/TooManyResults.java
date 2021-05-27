package Database.DBExceptions;

public class TooManyResults  extends Exception{
    public TooManyResults(){}
    public TooManyResults(String message){
        super(message);
    }
}
