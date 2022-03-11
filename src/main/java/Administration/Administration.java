package Administration;

import Dictionary.Definition;
import Dictionary.Word;
import Dictionary.WordInALanguage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Administration {
    public DictionaryInitialisation dictionary;

    public Administration() {
        this.dictionary = new DictionaryInitialisation();
    }

    public boolean addWord(Word word, String language) {
        //the condition for a word that already exists in the dictionary
        if(dictionary.wordTreeMap.containsKey(word.getWord()) &&
          (dictionary.wordTreeMap.get(word.getWord()).getLanguage().equals(language))) {
            return false;
        }
        //the word doesn't already exist and must be added to the dictionary
        WordInALanguage perfectWord = new WordInALanguage(word, language);
        dictionary.wordTreeMap.put(word.getWord(), perfectWord);
        return true;
    }

    public boolean removeWord(String word, String language) {
        //the condition for a word that doesn't exist in the dictionary
        if(!dictionary.wordTreeMap.containsKey(word) ||
          !(dictionary.wordTreeMap.get(word).getLanguage().equals(language))) {
            return false;
        }
        //the word exists in the dictionary and must be removed
        dictionary.wordTreeMap.remove(word);
        return true;
    }

    public boolean addDefinitionForWord(String word, String language, Definition definition) {
        WordInALanguage wordToModify = dictionary.wordTreeMap.get(word);
        //the condition for the language of the word
        if(!wordToModify.getLanguage().equals(language)) {
            return false;
        }
        //the condition for a definition from the same dictionary (dict)
        for(Definition existingDefinition : wordToModify.getWordInALanguage().getDefinitions()) {
            if(existingDefinition.getDict().equals(definition.getDict())) {
                return false;
            }
        }
        //the new definition can be added to the definitions
        //increases the size of the definitions' array by one
        Definition[] newDefinitions = Arrays.copyOf(wordToModify.getWordInALanguage().getDefinitions(),
                wordToModify.getWordInALanguage().getDefinitions().length + 1);
        //adds the new definition
        newDefinitions[newDefinitions.length - 1] = definition;
        wordToModify.getWordInALanguage().setDefinitions(newDefinitions);
        return true;
    }

    public boolean removeDefinition(String word, String language, String dictionary) {
        WordInALanguage wordToModify = this.dictionary.wordTreeMap.get(word);
        //the condition for the language of the word
        if(!wordToModify.getLanguage().equals(language)) {
            return false;
        }
        //searches for the dictionary from which the definition must be removed
        for(int i = 0; i < wordToModify.getWordInALanguage().getDefinitions().length; i++) {
            Definition existingDefinition = wordToModify.getWordInALanguage().getDefinitions()[i];
            if(existingDefinition.getDict().equals(dictionary)) {
                //the dictionary is found
                //removes the definition
                for(int j = i + 1; j < wordToModify.getWordInALanguage().getDefinitions().length; j++) {
                    wordToModify.getWordInALanguage().getDefinitions()[i] =
                            wordToModify.getWordInALanguage().getDefinitions()[j];
                    i++;
                }
                //decreases the size of the definitions' array by one
                Definition[] newDefinitions = Arrays.copyOf(wordToModify.getWordInALanguage().getDefinitions(),
                        wordToModify.getWordInALanguage().getDefinitions().length - 1);
                wordToModify.getWordInALanguage().setDefinitions(newDefinitions);
                return true;
            }
        }
        //there is no definition from the given dictionary
        return false;
    }

    public String translateWord(String word, String fromLanguage, String toLanguage) {
        WordInALanguage wordInALanguage = dictionary.wordTreeMap.get(word);
        //if the word isn't the key, it must be searched for in all forms of singular and plural
        int noun = 0, verb = 0;
        int[] singular = {0, 0, 0};
        int[] plural = {0, 0, 0};
        if(wordInALanguage == null) {
            for(Map.Entry<String,WordInALanguage> entry: dictionary.wordTreeMap.entrySet()) {
                WordInALanguage word1 = entry.getValue();
                if(word1.getWordInALanguage().getType().equals("noun")) {
                    //the noun is found in the plural form
                    if(word1.getWordInALanguage().getPlural()[0].equals(word)) {
                        noun = 1;
                        wordInALanguage = word1;
                        break;
                    }
                }
                if(word1.getWordInALanguage().getType().equals("verb")) {
                    //the verb is in one of the singular forms
                    for(int i = 0 ; i < word1.getWordInALanguage().getSingular().length; i++) {
                        if(word1.getWordInALanguage().getSingular()[i].equals(word)) {
                            verb = 1;
                            singular[i] = 1;
                            wordInALanguage = word1;
                            break;
                        }
                    }
                    //the verb is in one of the plural forms
                    for(int i = 0 ; i < word1.getWordInALanguage().getPlural().length; i++) {
                        if(word1.getWordInALanguage().getPlural()[i].equals(word)) {
                            verb = 1;
                            plural[i] = 1;
                            wordInALanguage = word1;
                            break;
                        }
                    }
                }
            }
        }
        //if the word isn't found in the dictionary or if the language isn't the expected one
        //the word remains as it was
        if((wordInALanguage == null) || !wordInALanguage.getLanguage().equals(fromLanguage)) {
            return word;
        }
        //searches for the translation of the word
        String word_en;
        word_en = wordInALanguage.getWordInALanguage().getWord_en();
        if((noun == 0) && (verb == 0)) {    //the dictionary form of a word
            for(Map.Entry<String,WordInALanguage> entry: dictionary.wordTreeMap.entrySet()) {
                if(entry.getValue().getWordInALanguage().getWord_en().equals(word_en)
                        && entry.getValue().getLanguage().equals(toLanguage)) {
                    return entry.getKey();
                }
            }
        } else if(noun == 1) {    //plural of a noun
            for(Map.Entry<String,WordInALanguage> entry: dictionary.wordTreeMap.entrySet()) {
                if(entry.getValue().getWordInALanguage().getWord_en().equals(word_en)
                        && entry.getValue().getLanguage().equals(toLanguage)) {
                    return entry.getValue().getWordInALanguage().getPlural()[0];
                }
            }
        } else {    //the conjugation of a verb
            for(Map.Entry<String,WordInALanguage> entry: dictionary.wordTreeMap.entrySet()) {
                if(entry.getValue().getWordInALanguage().getWord_en().equals(word_en)
                        && entry.getValue().getLanguage().equals(toLanguage)) {
                    for(int i = 0 ; i < singular.length; i++) {
                        if(singular[i] == 1) {
                            return entry.getValue().getWordInALanguage().getSingular()[i];
                        }
                        if(plural[i] == 1) {
                            return entry.getValue().getWordInALanguage().getPlural()[i];
                        }
                    }
                }
            }
        }
        //there hasn't been found a translation for the word
        return word;
    }

    public String translateSentence(String sentence, String fromLanguage, String toLanguage) {
        String[] splitSentence = sentence.split("[ ]");
        StringBuilder translatedSentence = new StringBuilder(translateWord(splitSentence[0], fromLanguage, toLanguage));
        for(int i = 1; i < splitSentence.length; i++) {
            translatedSentence.append(" ").append(translateWord(splitSentence[i],
                    fromLanguage, toLanguage));
        }
        return translatedSentence.toString();
    }

    public ArrayList<String> translateSentences(String sentence, String fromLanguage, String toLanguage) {
        ArrayList<String> translatedSentences = new ArrayList<>();
        //the normal translation of the sentence
        String translatedSentence = translateSentence(sentence, fromLanguage, toLanguage);
        translatedSentences.add(translatedSentence);
        String[] splitSentence = translatedSentence.split("[ ]");
        String[] splitOriginalSentence = sentence.split("[ ]");
        int trackSentences = 0;     //keeps track of how many translated sentences to return
        StringBuilder nextSentence = null;
        //searches for the synonyms for each word
        for(int i = 0 ; i < splitSentence.length; i++) {
            if(i == 1) { nextSentence = new StringBuilder(splitSentence[i - 1]); }
            else if(i > 1) {
                if (nextSentence != null) {
                    nextSentence.append(" ").append(splitSentence[i - 1]);
                }
            }
            if(dictionary.wordTreeMap.containsKey(splitSentence[i]) ||
                    (!splitOriginalSentence[i].equals(splitSentence[i]))) {
                //the word is in the dictionary form
                WordInALanguage wordInALanguage = dictionary.wordTreeMap.get(splitSentence[i]);
                //the word is either a plural noun or a conjugated form of a verb
                if(wordInALanguage == null) {
                    for(Map.Entry<String,WordInALanguage> entry: dictionary.wordTreeMap.entrySet()) {
                        WordInALanguage word1 = entry.getValue();
                        if(word1.getWordInALanguage().getType().equals("noun")) {
                            if(word1.getWordInALanguage().getPlural()[0].equals(splitSentence[i])) {
                                wordInALanguage = word1;
                                break;
                            }
                        }
                        if(word1.getWordInALanguage().getType().equals("verb")) {
                            //the verb is in one of the singular forms
                            for(int p = 0 ; p < word1.getWordInALanguage().getSingular().length; p++) {
                                if(word1.getWordInALanguage().getSingular()[p].equals(splitSentence[i])) {
                                    wordInALanguage = word1;
                                    break;
                                }
                                if(word1.getWordInALanguage().getPlural()[p].equals(splitSentence[i])) {
                                    wordInALanguage = word1;
                                    break;
                                }
                            }
                        }
                    }
                }
                //searches for the synonyms of the previously found word
                if (wordInALanguage != null) {
                    for(Definition definition : wordInALanguage.getWordInALanguage().getDefinitions()) {
                        if(definition.getDictType().equals("synonyms")) {
                            for (int j = 0; (j < definition.getText().length) && (trackSentences < 2); j++) {
                                StringBuilder builtSentence;
                                if (i == 0) {
                                    builtSentence = new StringBuilder(definition.getText()[j]);
                                } else {
                                    builtSentence = new StringBuilder(nextSentence + " " + definition.getText()[j]);
                                }
                                //builds the sentence with the rest of the words
                                for (int k = i + 1; k < splitSentence.length; k++) {
                                    builtSentence.append(" ").append(splitSentence[k]);
                                }
                                translatedSentences.add(builtSentence.toString());
                                trackSentences++;
                            }
                        }
                    }
                }
            }
        }
        return  translatedSentences;
    }

    static class SortByYear implements Comparator<Definition> {
        @Override
        public int compare(Definition a, Definition b) {
            return a.getYear() - b.getYear();
        }
    }

    void sortDefinitions(Word word) {
        Arrays.sort(word.getDefinitions(), new SortByYear());
    }

    public ArrayList<Definition> getDefinitionsForWord(String word, String language) {
        WordInALanguage wordInALanguage = dictionary.wordTreeMap.get(word);
        //verifies the language
        if(!wordInALanguage.getLanguage().equals(language)) { return null; }
        sortDefinitions(wordInALanguage.getWordInALanguage());
        return new ArrayList<>(Arrays.asList(wordInALanguage.
                getWordInALanguage().getDefinitions()));
    }

    public void exportDictionary(String language) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Word> dictionaryForLanguage = new ArrayList<>();
        //searches for all words in the dictionary from the given language
        for(WordInALanguage word : dictionary.wordTreeMap.values()) {
            if(word.getLanguage().equals(language)) {
                //the definitions are sorted after the year
                sortDefinitions(word.getWordInALanguage());
                dictionaryForLanguage.add(word.getWordInALanguage());
            }
        }
        //if the language doesn't exist in the dictionary
        if(dictionaryForLanguage.isEmpty()) {
            System.out.println("This language is not in the dictionary.");
            System.out.println("Try again after the next update!");
            return;
        }
        try {
            String json = gson.toJson(dictionaryForLanguage);
            String fileName = "src/test/java/ExportedDictionaries/dict_" + language + ".json";
            FileWriter writer = new FileWriter(fileName);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
