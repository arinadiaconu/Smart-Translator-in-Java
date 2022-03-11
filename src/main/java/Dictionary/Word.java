package Dictionary;

public class Word {
    private String word;
    private String word_en;
    private String type;
    private String[] singular;
    private String[] plural;
    private Definition[] definitions;

    public Word(String word, String word_en, String type,
                String[] singular, String[] plural,
                Definition[] definitions) {
        this.word = word;
        this.word_en = word_en;
        this.type = type;
        this.singular = singular;
        this.plural = plural;
        this.definitions = definitions;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord_en() {
        return word_en;
    }

    public void setWord_en(String word_en) {
        this.word_en = word_en;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getSingular() {
        return singular;
    }

    public void setSingular(String[] singular) {
        this.singular = singular;
    }

    public String[] getPlural() {
        return plural;
    }

    public void setPlural(String[] plural) {
        this.plural = plural;
    }

    public Definition[] getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Definition[] definitions) {
        this.definitions = definitions;
    }
}

