import java.util.ArrayList;

public class Node {
    public Node[] children;
    public Node () {
        children = new Node[26];
    }
    public void insertWord(String word, Node n) {
        int i = word.charAt(0)-97;
        //System.out.println("Putting in word: " + word + "  At index: "+i);
        if (n.children[i] == null) {
            n.children[i] = new Node();
        }
        if (word.length() > 1) {
            insertWord(word.substring(1),n.children[i]);
        }
    }
    public void getMatches(String pattern, Node n, ArrayList<String> matchList, String constructedWord) {
        if (pattern.length()==0) {
            matchList.add(constructedWord);
            constructedWord = "";
        } else {
            if (pattern.charAt(0)=='?') {
                for (int x =0; x < n.children.length; x++) {
                    if (n.children[x] != null) {
                        getMatches(pattern.substring(1), n.children[x], matchList, constructedWord+(char)(x+97));
                    }
                }
            } else {
                int i = pattern.charAt(0)-97;
                if (n.children[i] != null) {
                    constructedWord += pattern.charAt(0);
                    getMatches(pattern.substring(1), n.children[i], matchList, constructedWord);
                }
            }
        }
    }
}
