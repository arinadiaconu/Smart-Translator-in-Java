package TestSet;

import Administration.Administration;
import Dictionary.Definition;
import Dictionary.Word;
import Dictionary.WordInALanguage;

import java.util.ArrayList;

public class TestDictionaryMethods {

    public static void main(String[] args) {
        Administration administration = new Administration();
        System.out.println("============ LET'S TEST THE METHODS! ============");
        System.out.println("-------------------------------------------------");

        //FIRST METHOD - addWord
        String[] singular = {"cuvânt"};
        String[] plural = {"cuvinte"};
        Definition[] definitions = new Definition[2];
        String[] text1 = {"Unitate de bază a vocabularului, care reprezintă asocierea unui sens (sau a unui complex" +
                " de sensuri) și a unui complex sonor", "servește ca element de bază pentru formarea altor cuvinte"};
        String[] text2 = {"vorbă", "gând", "idee", "discurs", "conferință"};
        definitions[0] = new Definition("Dicționarul explicativ al limbii române (ediția a II-a revăzută și " +
                "adăugită)", "definitions", 1950, text1);
        definitions[1] = new Definition("Dicționar de sinonime", "synonyms", 2001, text2);
        Word w = new Word("cuvânt", "word", "noun", singular, plural, definitions);
        System.out.println("============ FIRST METHOD -- addWord ============");
        //NORMAL CASE - adds a word to the dictionary that doesn't already exist
        System.out.println("NORMAL CASE:    The result of the method is " + administration.addWord(w, "ro"));
        //EXCEPTION CASE - the word is already in the dictionary
        System.out.println("EXCEPTION CASE: The result of the method is " + administration.addWord(w, "ro"));
        System.out.println();

        //SECOND METHOD - removeWord
        System.out.println("========== SECOND METHOD -- removeWord ==========");
        //NORMAL CASE - removes a word that exists in the dictionary
        System.out.println("NORMAL CASE:    The result of the method is " + administration.removeWord("cuvânt",
                "ro"));
        //EXCEPTION CASE - the word doesn't exist in the dictionary
        System.out.println("EXCEPTION CASE: The result of the method is " + administration.removeWord("test",
                "ro"));
        System.out.println();

        //THIRD METHOD - addDefinitionForWord
        System.out.println("====== THIRD METHOD - addDefinitionForWord ======");
        //NORMAL CASE - the new definition was added to the given word
        String[] textNewDefinition = {"Chat ou tchat est un anglicisme. Discussion entre deux ou plusieurs personnes" +
                " sur Internet.", "Personne qui doit attraper les autres au jeu du même nom."};
        Definition newDefinition = new Definition("Dictionnaire francais", "definitions", 1976,
                textNewDefinition);
        System.out.println("NORMAL CASE:    The result of the method is " + administration.addDefinitionForWord(
                "chat", "fr", newDefinition));
        //EXCEPTION CASE - there is already a definition from this dictionary for the given word
        System.out.println("EXCEPTION CASE: The result of the method is " + administration.addDefinitionForWord(
                "chat", "fr", newDefinition));
        System.out.println();

        //FOURTH METHOD - removeDefinition
        System.out.println("======= FOURTH METHOD -- removeDefinition =======");
        //NORMAL CASE - removes the definition from the given dictionary
        System.out.println("NORMAL CASE:    The result of the method is " + administration.removeDefinition(
                "chat", "fr", "Dictionnaire francais"));
        //EXCEPTION CASE - there cannot be found a definition from the given dictionary
        System.out.println("EXCEPTION CASE: The result of the method is " + administration.removeDefinition(
                "chat", "fr", "Dictionnaire francais"));
        System.out.println();

        //FIFTH METHOD - translateWord
        System.out.println("========= FIFTH METHOD -- translateWord =========");
        //NORMAL CASE - returns the translation of the given word
        System.out.println("NORMAL CASE:");
        System.out.println("The translation of the word <pisică> is " + administration.translateWord(
                "pisică", "ro", "fr") + ".");
        //EXCEPTION CASE - there is no translation for the word
        System.out.println("EXCEPTION CASE:");
        System.out.println("The translation of the word <periuță> is " + administration.translateWord(
                "periuță", "ro", "fr") + ".");
        System.out.println();

        //SIXTH METHOD - translateSentence
        System.out.println("======= SIXTH METHOD -- translateSentence =======");
        //NORMAL CASE - translates all words in the sentence
        System.out.println("NORMAL CASE:");
        System.out.println("The translation of the sentence:");
        System.out.println("<pisici mănâncă carne> is ");
        System.out.println(administration.translateSentence("pisici mănâncă carne", "ro",
                "fr"));
        //EXCEPTION CASE - cannot translate all words in the sentence
        System.out.println("EXCEPTION CASE:");
        System.out.println("The translation of the sentence:");
        System.out.println("<câini nu mănâncă pește> is ");
        System.out.println(administration.translateSentence("câini nu mănâncă pește", "ro",
                "fr"));
        System.out.println();

        //SEVENTH METHOD - translateSentences
        System.out.println("====== SEVENTH METHOD - translateSentences ======");
        //NORMAL CASE - there are 3 different translations for a sentence
        System.out.println("NORMAL CASE:");
        System.out.println("The translations of the sentence:");
        System.out.println("<mere cad păsări zboară> are ");
        ArrayList<String> a = administration.translateSentences("mere cad păsări zboară", "ro",
                "fr");
        for (String s : a) {
            System.out.println(s);
        }
        //EXCEPTION CASE - not enough available translations
        System.out.println("EXCEPTION CASE:");
        System.out.println("The translations of the sentence:");
        System.out.println("<ourse mange> are ");
        ArrayList<String> a1 = administration.translateSentences("ourse mange", "fr",
                "ro");
        for (String s : a1) {
            System.out.println(s);
        }
        System.out.println();

        //EIGHTH METHOD - getDefinitionsForWord
        System.out.println("===== EIGHTH METHOD - getDefinitionsForWord =====");
        //NORMAL CASE - sorts the definitions by year
        System.out.println("NORMAL CASE:");
        System.out.println("The definitions' years before sorting");
        WordInALanguage wordInALanguage = administration.dictionary.wordTreeMap.get("câine");
        for(Definition d : wordInALanguage.getWordInALanguage().getDefinitions()) {
            System.out.println(d.getYear());
        }
        System.out.println("The definitions' years after sorting");
        ArrayList<Definition> d = administration.getDefinitionsForWord("câine", "ro");
        for (Definition definition : d) {
            System.out.println(definition.getYear());
        }
        System.out.println("And the definitions are:");
        for (Definition definition : d) {
            for (int j = 0; j < definition.getText().length; j++) {
                System.out.println(definition.getText()[j]);
            }
        }
        //EXCEPTION CASE - there are no definitions for a word
        System.out.println("EXCEPTION CASE:");
        ArrayList<Definition> d1 = administration.getDefinitionsForWord("prier", "fr");
        if(d1.size() == 0) {
            System.out.println("No definitions for this word!");
        }
        System.out.println();

        //NINTH METHOD - exportDictionary
        System.out.println("======== NINTH METHOD - exportDictionary ========");
        //NORMAL CASE - the language exists in the dictionary
        System.out.println("NORMAL CASE:");
        administration.exportDictionary("fr");
        System.out.println("Check out the <dict_fr.json> file just created!");
        administration.exportDictionary("ro");
        System.out.println("Check out the <dict_ro.json> file just created!");
        //EXCEPTION CASE - the language doesn't exist
        System.out.println("EXCEPTION CASE:");
        administration.exportDictionary("bg");

        System.out.println("-------------------------------------------------");
        System.out.println("==================== THE END ====================");
    }
}
