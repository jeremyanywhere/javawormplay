import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



public class WordSquareMaker implements Runnable {
    private static final int WORD_LENGTH = 5;
    private WordList wl;

    public WordSquareMaker() {
        wl = new WordList("griddle5.txt");
        loadTree();
    }
    private Node tree = new Node();

    public ArrayList<String> matchPattern(String pattern) {
        ArrayList<String> matchList = new ArrayList<String>();
        tree.getMatches(pattern, tree, matchList,"");
        return matchList;
    }
    public void loadTree() {  
        tree = new Node(); 
        for (String w : wl.getFullList()) {
            tree.insertWord(w, tree);
         }
    }
    public void dumpGrid(String grid) {
        System.out.println("wordsquare.."+grid);
        //tm = time.time()-ts
        for (int i=0; i < WORD_LENGTH; i++) {
            System.out.println(grid.substring(i*WORD_LENGTH, WORD_LENGTH*(i+1)));
        }
    }
    private void tryNextWord(String grid, int n, Set<String> usedSet) {
        int[] orderMapping = {0,1,2,3,4,5,6,7,8,9};
        if (n>9) {
		    System.out.println("{used set} -  "+ usedSet);
		    dumpGrid(grid);
		    return;
        }   
        int i = orderMapping[n];
        ArrayList<String> matchList = new ArrayList<String>();
        if ((i % 2) == 0) {// even, horiz.
		    String m = "";
		    for (int x=0; x< WORD_LENGTH; x++) {
			    m = m + grid.charAt((i/2)*WORD_LENGTH+x);
			    matchList = matchPattern(m); 
            }
        }
	    if ((i % 2) != 0) { //pull out verticals
		    String m = "";
		    for (int x=0; x< WORD_LENGTH; x++) {
			    m = m + grid.charAt(((i-1)/2)+x*WORD_LENGTH);
			    matchList = matchPattern(m);
            } 
        }
	    if (matchList.size() < 1) {  // recursion ends.
		    return;
        }

        for (String w : matchList) {
            if (!usedSet.contains(w)) { 
                char[] lw = grid.toCharArray();
                if ((i % 2) == 0) {//#even
                    for (int x=0; x< WORD_LENGTH; x++) {
                        lw[(i/2)*WORD_LENGTH+x] = w.charAt(x);
                    }
                }
                if ((i % 2) != 0) {// odd, vertical
                    for (int x=0; x< WORD_LENGTH; x++) {
                        lw[((i-1)/2)+x*WORD_LENGTH] = w.charAt(x);
                    }
                }
                String st = new String(lw);
                usedSet.add(w);
                tryNextWord(st, n+1, usedSet);
                usedSet.remove(w);
            }
        }
    }
    public static void main (String[] args)
     {
        System.out.println("..starting..");
        WordSquareMaker wt = new WordSquareMaker();
        wt.loadTree();
        System.out.println("Tree Loaded..");
        String[] mList =  {"shark"};
        for (String seed : mList) {
            System.out.println(" seeding with - "+seed);
            wt.tryNextWord(seed+"????????????????????", 1, new HashSet <String>());
        }
        // System.out.println("Starting Threads..");
        // for (int t = 0; t < 4; t++) {
        //     Thread th = new Thread(wt);
        //     th.start();
        // }
     }
    @Override
    public void run() {
        for (int x = 0; x < 200; x++) {
            System.out.println("word "+ x + ") "+ wl.getNextWord());
        }
    }
}
