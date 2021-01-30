import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;
import java.util.function.Supplier;

import static org.jgrapht.util.SupplierUtil.DEFAULT_WEIGHTED_EDGE_SUPPLIER;
import static org.jgrapht.util.SupplierUtil.createIntegerSupplier;

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
	 * by checking for reversed edges. Currently O(n^4)
	 */
	//Computes a maximum weighted matching.
	public void computeMaximumWeightedMatching() {
		final Supplier<Integer> INTEGER_SUPPLIER = createIntegerSupplier();

		///////////////////////////////////////////////////////////////////////
		// Convert graph <V,E> to wrappedGraph <Integer,DefaultWeightedEdge> so s, t in hungarian algorithm can be added to graph.
		// Backwards transformation at the end via wrappedEdgesToEdges.

		Graph<Integer, DefaultWeightedEdge> wrappedGraph = new DefaultUndirectedWeightedGraph<>
				(INTEGER_SUPPLIER, DEFAULT_WEIGHTED_EDGE_SUPPLIER);
		Map<V, Integer> vtoWrappedV = new HashMap<>();
		Map<DefaultWeightedEdge, E> wrappedEdgesToEdges = new HashMap<>();

		//convert vertices
		for (V v : graph.vertexSet()) {
			vtoWrappedV.put(v, wrappedGraph.addVertex());
		}

		//convert edges
		for (E e : graph.edgeSet()) {
			DefaultWeightedEdge wrappedEdge = wrappedGraph.addEdge(
					vtoWrappedV.get(graph.getEdgeSource(e)),
					vtoWrappedV.get(graph.getEdgeTarget(e)));
			wrappedGraph.setEdgeWeight(wrappedEdge, graph.getEdgeWeight(e));
			wrappedEdgesToEdges.put(wrappedEdge, e);
		}

		///////////////////////////////////////////////////////////////////////
		// First find color classes of the wrappedGraph using MyBipartition
		MyBipartition<Integer, DefaultWeightedEdge> bipartition = new MyBipartition<>(wrappedGraph);
		bipartition.computeBipartitioning();
		// assert that the wrappedGraph is bipartite
		if (!bipartition.isBipartite()){
			//Probably more correct to throw exception here, but that won't work in the VPL
			throw new java.lang.Error("Attempting to construct bipartite matching for non-bipartite graph!");
		}
		// get color classes of bipartite wrappedGraph
		List<HashSet<Integer>> colorClasses = bipartition.getPartitioning();
		HashSet<Integer> U = colorClasses.get(0);
		HashSet<Integer> W = colorClasses.get(1);

		///////////////////////////////////////////////////////////////////////
		// Initialize augmentingGraph for an empty Matching

		Graph<Integer, DefaultWeightedEdge> augmentingGraph = new DefaultDirectedWeightedGraph<>
				(INTEGER_SUPPLIER, DEFAULT_WEIGHTED_EDGE_SUPPLIER);
		U.forEach(augmentingGraph::addVertex);
		W.forEach(augmentingGraph::addVertex);
		// add s,t
		Integer s = augmentingGraph.addVertex();
		Integer t = augmentingGraph.addVertex();

		// add edges to augmentingGraph, such that they point from U to W.
		wrappedGraph.edgeSet().forEach(
				(DefaultWeightedEdge e) -> {
					Integer z = wrappedGraph.getEdgeTarget(e);
					DefaultWeightedEdge eAdded;
					//add edge in correct orientation
					if (W.contains(z)){
						eAdded = augmentingGraph.addEdge(wrappedGraph.getEdgeSource(e), z);
					} else {
						eAdded = augmentingGraph.addEdge(z, wrappedGraph.getEdgeSource(e));
					}
					//weights are negative as points from U to W.
					augmentingGraph.setEdgeWeight(eAdded, -wrappedGraph.getEdgeWeight(e));
				}
		);

		// add s-u edges
		U.forEach((Integer uVertex) -> {
				augmentingGraph.setEdgeWeight(augmentingGraph.addEdge(s, uVertex), 0);
		});

		// add t-w edges
		W.forEach((Integer wVertex) -> {
			augmentingGraph.setEdgeWeight(augmentingGraph.addEdge(wVertex, t), 0);
		});

		///////////////////////////////////////////////////////////////////////
		while (true){
			//find shortest s-t path in augmentingGraph
			BellmanFordShortestPath<Integer,DefaultWeightedEdge> shortestPath = new BellmanFordShortestPath<>(augmentingGraph);
			GraphPath<Integer, DefaultWeightedEdge> path = shortestPath.getPath(s, t);
			if (path == null) {
				// if path is null no augmenting path exists -> maximal weighted matching found.
				break;
			} else {
				// if path is not null augment augmentingGraph graph using this path:
				// flip edge direction and invert the sign of the weight for every edge in path
				path.getEdgeList().forEach((DefaultWeightedEdge e) -> {
					DefaultWeightedEdge eAdded =
							augmentingGraph.addEdge(augmentingGraph.getEdgeTarget(e), augmentingGraph.getEdgeSource(e));
					//set new edge weight
					augmentingGraph.setEdgeWeight(eAdded, -augmentingGraph.getEdgeWeight(e));
					// remove old edge
					augmentingGraph.removeEdge(e);
				});

				// remove first and last arc in path from augmentingGraph.
				augmentingGraph.removeEdge(path.getVertexList().get(1), s);
				augmentingGraph.removeEdge(t, path.getVertexList().get(path.getVertexList().size()-2));
			}
		}
		////////////////////////////////////////////////////////////////////////
		// create matching from final augmentingGraph, that is edges from W to U are in M
		matching = new HashSet<>();
		augmentingGraph.edgeSet().forEach(
				(DefaultWeightedEdge e) -> {
					Integer z = augmentingGraph.getEdgeTarget(e);
					// target is in U and ignore s to U edges
					if (U.contains(z) && (!s.equals(augmentingGraph.getEdgeSource(e)))){
						matching.add(wrappedEdgesToEdges.get(wrappedGraph.getEdge(z, augmentingGraph.getEdgeSource(e))));
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
