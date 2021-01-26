import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	}

	private GraphPath<V, E> getAlternatingPath(Set<E> matching, Graph<V,E> equality){
		Set<V> unmatched = this.unmatchedVertices(matching);
		DijkstraShortestPath<V,E> shortestPath = new DijkstraShortestPath<>(equality);
		for (V s:unmatched) {
			for (V t:unmatched) {
				if(!s.equals(t)){
					GraphPath<V, E> path = shortestPath.getPath(s, t);
					if(isAlternating(matching,path)){
						return path;
					}
				}
			}
		}
		return null;
	}

	private boolean isAlternating(Set<E> matching, GraphPath<V,E> path){
		List<E> edges = path.getEdgeList();
		boolean last = matching.contains(edges.get(0));
		for (int i = 1; i < edges.size(); i++) {
			boolean current = matching.contains(edges.get(i));
			if(current == last){
				return false;
			}
			last = current;
		}
		return true;
	}

	private Set<V> matchedVertices(Set<E> matching){
		Set<V> matched = new HashSet<>();
		for (E edge:matching) {
			matched.add(graph.getEdgeSource(edge));
			matched.add(graph.getEdgeTarget(edge));
		}
		return matched;
	}

	private Set<V> unmatchedVertices(Set<E> matching){
		Set<V> unmatched = new HashSet<>(graph.vertexSet());
		unmatched.removeAll(this.matchedVertices(matching));
		return unmatched;
	}

	public Set<E> getMatching() {
		return matching;
	}

	public Graph<V, E> getGraph() {
		return graph;
	}
}
