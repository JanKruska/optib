

import java.util.Set;

import org.jgrapht.Graph;

/**
 * Implementation of an algorithm for maximum weighted matchings in bipartite graphs.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public class MyBipartiteMatching <V,E> {
	private final Graph<V, E> graph;
	private Set<E> matching;
	
	public MyBipartiteMatching(Graph<V, E> graph) {
		this.graph = graph;
	}
	
	//Computes a maximum weighted matching.
	public void computeMaximumWeightedMatching() {
        // TODO implement algorithm here
		
	}

	public Set<E> getMatching() {
		return matching;
	}

	public Graph<V, E> getGraph() {
		return graph;
	}
}
