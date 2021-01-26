import com.sun.tools.jconsole.JConsoleContext;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.jgrapht.util.SupplierUtil.DEFAULT_WEIGHTED_EDGE_SUPPLIER;

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

	/**
	 * Computes Maximum Weighted Matching via Hungarian Algorithm.
	 *
	 * Only keeps track of the directed support graph ("augmentingGraph") as matching can be deduced from it
	 * by checking for reversed edges. Currently O(n^4) luls
	 */
	//Computes a maximum weighted matching.
	public void computeMaximumWeightedMatching() {
		///////////////////////////////////////////////////////////////////////
		// First find color classes of bipartite graph using MyBipartition

		MyBipartition<V, E> bipartition = new MyBipartition<>(graph);
		bipartition.computeBipartitioning();
		// assert that graph is bipartite
		if (!bipartition.isBipartite()){
			throw new java.lang.Error("MyBipartiteMatching called for non bipartite graph!");
		}
		// get color classes of bipartite graph
		List<HashSet<V>> colorClasses = bipartition.getPartitioning();
		HashSet<V> U = colorClasses.get(0);
		HashSet<V> W = colorClasses.get(1);

		///////////////////////////////////////////////////////////////////////
		// Initialize augmentingGraph for an empty Matching

		Graph<V, DefaultWeightedEdge> augmentingGraph = new DefaultDirectedWeightedGraph<>(graph.getVertexSupplier(),
				DEFAULT_WEIGHTED_EDGE_SUPPLIER);
		U.forEach(augmentingGraph::addVertex);
		W.forEach(augmentingGraph::addVertex);
		V s = augmentingGraph.addVertex();
		V t = augmentingGraph.addVertex();

		graph.edgeSet().forEach(
				(E e) -> {
					V z = graph.getEdgeTarget(e);
					DefaultWeightedEdge eAdded = null;
					if (W.contains(z)){
						eAdded = augmentingGraph.addEdge(graph.getEdgeSource(e), z);
					} else {
						eAdded = augmentingGraph.addEdge(z, graph.getEdgeSource(e));
					}
					augmentingGraph.setEdgeWeight(eAdded, -graph.getEdgeWeight(e));
				}
		);

		U.forEach((V uVertex) -> {
				augmentingGraph.setEdgeWeight(augmentingGraph.addEdge(s, uVertex), 0);
		});

		W.forEach((V wVertex) -> {
			augmentingGraph.setEdgeWeight(augmentingGraph.addEdge(wVertex, t), 0);
		});

		///////////////////////////////////////////////////////////////////////
		while (true){
			//find shortest s-t path in augmentingGraph
			BellmanFordShortestPath<V,DefaultWeightedEdge> shortestPath = new BellmanFordShortestPath<>(augmentingGraph);
			GraphPath<V, DefaultWeightedEdge> path = shortestPath.getPath(s, t);
			if (path == null) {
				// if path is null no augmenting path exists => done
				break;
			} else {
				// else augment augmentingGraph graph using path
				// flip edge directions and invert sign of weight for every edge in path
				path.getEdgeList().forEach((DefaultWeightedEdge e) -> {
					double edgeWeight = augmentingGraph.getEdgeWeight(e);
					DefaultWeightedEdge eAdded =
							augmentingGraph.addEdge(augmentingGraph.getEdgeTarget(e), augmentingGraph.getEdgeSource(e));
					augmentingGraph.setEdgeWeight(eAdded, -augmentingGraph.getEdgeWeight(e));
					augmentingGraph.removeEdge(e);
				});

				// remove flipped s-first, last-t arcs
				augmentingGraph.removeEdge(path.getVertexList().get(1), s);
				augmentingGraph.removeEdge(t, path.getVertexList().get(path.getVertexList().size()-2));
			}
		}
		////////////////////////////////////////////////////////////////////////
		// create matching from final augmentingGraph, that is edges from W to U are in M
		matching = new HashSet<>();
		augmentingGraph.edgeSet().forEach(
				(DefaultWeightedEdge e) -> {
					V z = augmentingGraph.getEdgeTarget(e);
					// target is in U and ignore s to U edges
					if (U.contains(z) && (s != augmentingGraph.getEdgeSource(e))){
						matching.add(graph.getEdge(z, augmentingGraph.getEdgeSource(e)));
					}
				}
		);

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
