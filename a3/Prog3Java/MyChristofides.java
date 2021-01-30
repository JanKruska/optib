import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.AsSubgraph;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyChristofides <V, E>{

	private final Graph<V, E> graph;
	private Graph<V, E> shortestPathGraph;
	private List<V> tour;
	private boolean tourExists;
	private Pair<V, V> unconnectedVertices;
	
	public MyChristofides(Graph<V, E> graph) {
		this.graph = graph;
	}
	
	public void computeTour() {
		this.computeShortestPathGraph();
		if (tourExists) {
	        // TODO implement algorithm here
			
		}
		
	}
	
	private void computeShortestPathGraph() {
		KruskalMinimumSpanningTree<V,E> mstAlgorithm = new KruskalMinimumSpanningTree<>(this.graph);
		SpanningTreeAlgorithm.SpanningTree<E> mst = mstAlgorithm.getSpanningTree();
		//Get all vertices with odd degree in mst
		Stream<V> oddDegree = graph.vertexSet().stream().filter(
				v -> mst.getEdges().stream()
						.mapToInt(e -> graph.getEdgeTarget(e) == v || graph.getEdgeSource(e) == v ? 1 : 0)
						.sum() % 2 == 1);
		AsSubgraph<V,E> subgraph = new AsSubgraph<>(this.graph,oddDegree.collect(Collectors.toSet()));

		KolmogorovWeightedPerfectMatching<V,E> matchingAlgorithm = new KolmogorovWeightedPerfectMatching<>(subgraph);
		MatchingAlgorithm.Matching<V, E> matching = matchingAlgorithm.getMatching();

		HashSet<E> edgeSet = new HashSet<>();
		edgeSet.addAll(mst.getEdges());
		edgeSet.addAll(matching.getEdges());
		shortestPathGraph = new AsSubgraph<>(this.graph,this.graph.vertexSet(),edgeSet);
	}

	public Graph<V, E> getGraph() {
		return graph;
	}

	public boolean isTourExists() {
		return tourExists;
	}
	
	public List<V> getTour() {
		return tour;
	}
	
	public Pair<V, V> getUnconnectedVertices() {
		return unconnectedVertices;
	}
}
