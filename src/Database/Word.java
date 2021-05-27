package Database;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Word {
    private StringProperty wordTarget;
    private StringProperty wordExplain;

    public Word(){}
    public Word(String target, String explain){
        wordTargetProperty().set(target);
        wordExplainProperty().set(explain);}
    public Word(String target){wordTargetProperty().set(target);}


    public String getWord(){return wordTargetProperty().get();}
    public String getExplain(){return wordExplainProperty().get();}
    public StringProperty wordTargetProperty(){
        if (wordTarget == null) wordTarget = new SimpleStringProperty(this, "wordTarget");
        return wordTarget;
    }
    public void setWord(String target){wordTargetProperty().set(target);}
    public void setExplain(String explain){wordExplainProperty().set(explain);}
    public StringProperty wordExplainProperty(){
        if (wordExplain == null) wordExplain = new SimpleStringProperty(this, "wordExplain");
        return wordExplain;
    }
}
