package Administration;

import Dictionary.Word;
import Dictionary.WordInALanguage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DictionaryInitialisation {
    //the collection that contains all words in all languages
    public TreeMap<String, WordInALanguage> wordTreeMap;

    //reads from a json file using gson library
    void read_JSON(Path name, TreeMap<String,WordInALanguage> treeMap, String language) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(String.valueOf(name)));
            //creates a list with the words read from the json file
            List<Word> words = gson.fromJson(bufferedReader, new TypeToken<List<Word>>() {}.getType());
            //creates a list with the words and their languages
            List<WordInALanguage> wordsInALanguage = new ArrayList<>();
            for(Word word : words) {
                WordInALanguage w = new WordInALanguage(word, language);
                wordsInALanguage.add(w);
            }
            //adds all words in all languages to a TreeMap
            //which keeps them sorted after each word's name
            for(WordInALanguage wordInALanguage : wordsInALanguage) {
                treeMap.put(wordInALanguage.getWordInALanguage().getWord(), wordInALanguage);
            }
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DictionaryInitialisation() {
        this.wordTreeMap = new TreeMap<>();
        //reads all files from the path to the input folder
        try(Stream<Path> path = Files.walk(Paths.get("src/main/resources/input"))) {
            List<Path> list = path.filter(Files::isRegularFile).collect(Collectors.toList());
            Path p;
            int i = 0;
            while(i < list.size()) {
                //for each path to a file from the input folder
                p = list.get(i++);
                //gets the name of the file
                String string = p.getFileName().toString();
                //gets the language formed by the first 2 characters
                String language = string.substring(0, 2);
                //reads the words from each json
                this.read_JSON(p, wordTreeMap, language);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
