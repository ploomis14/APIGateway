package gateway;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Storage system to keep track of which term is searched for most often. A guava cache is used to keep track of queries
 * and the number of times each was searched for.
 *
 * @author Peter Loomis
 */

public class CallLog {

    private static final long MAX_SIZE=1000; // maximum number of objects that can be stored

    private Cache<String,TermCount> cache;

    public CallLog() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(MAX_SIZE) // entries that aren't used often are evicted
                .build();
    }

    /**
     * If the searchTerm has already been searched for, its count is incremented, otherwise a new TermCount object is
     * created
     * @param searchTerm the query string
     */
    public void visit(String searchTerm) {
        TermCount termCount = cache.getIfPresent(searchTerm);
        if (termCount == null) {
            cache.put(searchTerm,new TermCount(searchTerm));
        } else {
            termCount.visit();
        }
    }

    /**
     * Iterates through the entries in the cache and finds the TermCount object with the highest count
     * @return TermCount object containing the highest count and the most searched for term
     */
    public TermCount getMostVisited() {
        TermCount highestCount = null;
        for (TermCount count : cache.asMap().values()) {
            if (highestCount == null || count.getTimesSearchedFor() > highestCount.getTimesSearchedFor()) {
                highestCount = count;
            }
        }
        return highestCount;
    }
}
