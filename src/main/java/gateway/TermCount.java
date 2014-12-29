package gateway;

/**
 * This class stores a search query and the number of times it was searched for
 * @author Peter Loomis
 */

public class TermCount implements Comparable<TermCount> {

    private String term;
    private int timesSearchedFor;

    public TermCount(String term) {
        this.term = term;
        timesSearchedFor = 1;
    }

    public int compareTo(TermCount termCount) {
        if (this.timesSearchedFor > termCount.timesSearchedFor) {
            return 1;
        }
        return 0;
    }

    public String getTerm() {
        return term;
    }

    public void visit() {
        timesSearchedFor += 1;
    }

    public int getTimesSearchedFor() {
        return timesSearchedFor;
    }
}
