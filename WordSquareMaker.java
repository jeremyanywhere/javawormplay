
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;



public class WordSquareMaker implements Runnable {
    private static final int WORD_LENGTH = 5;
    private WordList wl;
    ThreadLocal<String> threadName = new ThreadLocal<String>();
    Random rand = new Random();
    int counter = 0;
    private Node tree = new Node();
    private long timeStamp = System.currentTimeMillis();
    public WordSquareMaker() {
        wl = new WordList("griddle5.txt");
        loadTree();
    }

    private long getTS() {
        return System.currentTimeMillis()-timeStamp;
    }
    private String generateThreadName() {
        String[] adjectives = {"funny", "silly", "happy", "crazy", "bouncy", "stealthy", "wise","giant","tiny","magnificent","magnanimous","angry","clever", "weird", "funny","sad","fierce","gentle"};
        String[] nouns = {"fairy", "monk","tiger","eagle","dragon","snake","bull","coyote","elf","orc","wizard","witch","hawk","shark","bunny","wolf","bear","warrior"};
        return adjectives[rand.nextInt(adjectives.length)] + "-" + nouns[rand.nextInt(nouns.length)];
    }
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
    public synchronized void dumpGrid(String grid) {
        //tm = time.time()-ts
        counter ++;
        System.out.println("Wordsquare "+counter+" found by thread: "+ threadName.get() + " Timestamp - "+ getTS());
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
        // String[] mList =  {"shark"};
        // for (String seed : mList) {
        //     System.out.println(" seeding with - "+seed);
        //     wt.tryNextWord(seed+"????????????????????", 1, new HashSet <String>());
        // }
        System.out.println("Starting Threads..");
        for (int t = 0; t < 4; t++) {
            Thread th = new Thread(wt);
            th.start();
        }
     }
    @Override
    public void run() {
        String sillyName = generateThreadName();
        System.out.println("creating thread.. "+ sillyName);
        threadName.set(sillyName);
        String seed = wl.getNextWord();
        while (seed.length()>0) {
            System.out.println("\nSeeding with " + seed);
            HashSet<String> usedSet = new HashSet<String>();
            usedSet.add(seed);
            tryNextWord(seed+"????????????????????", 1,usedSet);
            seed = wl.getNextWord();
        }
        System.out.println("Thread done. ");
    }
}
