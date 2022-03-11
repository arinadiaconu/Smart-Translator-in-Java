package Dictionary;

public class WordInALanguage {
    private Word word;
    private String language;

    public WordInALanguage(Word word, String language) {
        this.word = word;
        this.language = language;
    }

    public Word getWordInALanguage() {
        return word;
    }

    public void setWordInALanguage(Word word) {
        this.word = word;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
