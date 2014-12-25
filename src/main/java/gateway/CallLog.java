package gateway;

import java.net.URI;
import java.util.ArrayList;

/**
 * Storage system to keep track of which term is searched for most often. A binary heap is used to store visited
 * endpoints so that the endpoint with the most visits can be quickly retrieved.
 *
 * @author Peter Loomis
 */

public class CallLog {

    private BinaryHeap<Endpoint> heap;

    public CallLog() {
        heap = new BinaryHeap<Endpoint>();
    }

    /**
     * Iterates through visited endpoints in the heap array until an endpoint with a matching search term is found. The visited
     * count of this endpoint is then incremented. If no matching term is found, then a new endpoint is created with the
     * uri parameter and inserted into the heap.
     *
     * @param searchTerm // visit the endpoint with this term
     */
    public void visit(String searchTerm) {
        boolean visited = false;
        ArrayList<Endpoint> items = heap.getItems();
        int index = 0;
        for (Endpoint n : items) {
            if (n.getTerm().equals(searchTerm)) {
                n.visit();
                heap.shiftUp(index);
                visited = true;
                break;
            }
            index++;
        }
        if (visited == false) { // search term isn't contained in any endpoint in the heap
            Endpoint endpoint = new Endpoint(searchTerm);
            heap.insert(endpoint);
        }
    }

    public Endpoint getMostVisited() {
        return heap.getRoot();
    }
}
