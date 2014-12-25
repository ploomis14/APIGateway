package gateway;

import java.net.URI;

/**
 * @author Peter Loomis
 */

public class Endpoint implements Comparable<Endpoint> {

    private String term;
    private int timesSearchedFor;

    public Endpoint(String term) {
        this.term = term;
        timesSearchedFor = 1;
    }

    public int compareTo(Endpoint endpoint) {
        if (this.timesSearchedFor > endpoint.timesSearchedFor) {
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
