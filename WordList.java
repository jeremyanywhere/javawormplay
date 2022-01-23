import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

//threadsafe wordlist
public class WordList {
    private ArrayList<String> words;
    private String fileName;
    private int index = 0;

    public WordList(String fileName) {
        this.fileName = fileName;
        load();
    }
    public void load() {
        try {
            Scanner s = new Scanner(new File(fileName));
            words = new ArrayList<String>();
            index = 0;
            while (s.hasNext()) {
                words.add(s.next());
            }
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    public synchronized String getNextWord() {
        if (index > words.size()-1) {
            return "";
        }
        index++;
        return words.get(index-1);
    }
    public ArrayList<String> getFullList() {
        return words;
    }
}
