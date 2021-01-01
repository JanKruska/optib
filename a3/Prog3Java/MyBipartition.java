

import java.util.HashSet;
import java.util.List;

import org.jgrapht.Graph;

/**
 * Implementation of a bipartition detection algorithm.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public class MyBipartition <V,E> {

    private final Graph<V, E> graph;
    private boolean isBipartite;
    private List<HashSet<V>> bipartitioning;
    private List<V> oddCycle;

    public MyBipartition(Graph<V, E> graph) {
        this.graph = graph;
    }
    
    //Decides whether the graph is bipartite or not.
    //Either computes a bipartitioning of the vertex set or finds a cycle of odd length if the graph is not bipartite.
    public void computeBipartitioning() {
        // TODO implement algorithm here
    	    	
	}
    
    public Graph<V, E> getGraph() {
		return graph;
	}

    public boolean isBipartite() {
		return isBipartite;
	}

	public List<HashSet<V>> getPartitioning() {
    	return bipartitioning;
    }

    public List<V> getOddCycle() {
        return oddCycle;
    }
}
