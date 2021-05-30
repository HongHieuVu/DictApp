package Database;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;
import java.util.Objects;

public class Word {
    private StringProperty wordTarget;
    private StringProperty wordMeaning;
    private String html;

    public Word() {
        wordTarget = wordTargetProperty();
        wordMeaning = wordMeaningProperty();
        html = "";
    }

    public Word(String word, String meaning) {
        setWord(word);
        setMeaning(meaning);
    }

    public Word(String target, String explain, String html) {
        setWord(target);
        setMeaning(explain);
        setHtml(html);
    }

    public String getWord() {
        return wordTargetProperty().get();
    }

    public String getMeaning() {
        return wordMeaningProperty().get();
    }

    public String getHtml() {
        return html;
    }

    public StringProperty wordTargetProperty() {
        if (wordTarget == null) wordTarget = new SimpleStringProperty(this, "wordTarget");
        return wordTarget;
    }

    public void setWord(String target) {
        wordTargetProperty().set(target);
    }

    public void setMeaning(String explain) {
        wordMeaningProperty().set(explain);
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public StringProperty wordMeaningProperty() {
        if (wordMeaning == null) wordMeaning = new SimpleStringProperty(this, "wordMeaning");
        return wordMeaning;
    }

    /**
     * compare two words
     *
     * @param o word to compare against
     * @return true if word objects' word and definition are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        if (word.getMeaning() == null) return false;

        //intuitive definition of equal
        boolean sameWord = this.getWord().equals(word.getWord());
        boolean sameDef = this.getMeaning().equals(word.getMeaning());
        return !(!sameWord & !sameDef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordTarget, wordMeaning);
    }

    /**
     * checks if this word is already in a word list
     *
     * @param wordList list to check against
     * @return true if duplicated
     */
    public boolean duplicated(List<Word> wordList) {
        for (Word word : wordList) {
            if (this.equals(word)) return true;
        }
        return false;
    }
}