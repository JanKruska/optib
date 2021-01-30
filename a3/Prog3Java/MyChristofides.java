import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.ManyToManyShortestPathsAlgorithm;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching;
import org.jgrapht.alg.shortestpath.DijkstraManyToManyShortestPaths;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyChristofides <V, E>{

	private final Graph<V, E> graph;
	private Graph<V, DefaultWeightedEdge> shortestPathGraph;
	private List<V> tour;
	private boolean tourExists;
	private Pair<V, V> unconnectedVertices;
	
	public MyChristofides(Graph<V, E> graph) {
		this.graph = graph;
	}
	
	public void computeTour() {
		this.computeShortestPathGraph();
		if (tourExists) {
			//Construct minimum spanning Tree
			KruskalMinimumSpanningTree<V,DefaultWeightedEdge> mstAlgorithm = new KruskalMinimumSpanningTree<>(this.shortestPathGraph);
			SpanningTreeAlgorithm.SpanningTree<DefaultWeightedEdge> mst = mstAlgorithm.getSpanningTree();

			//Get all vertices with odd degree in mst, and construct induced subgraph
			Set<V> oddDegree = this.shortestPathGraph.vertexSet().stream().filter(
					v -> mst.getEdges().stream()
							.mapToInt(e -> this.shortestPathGraph.getEdgeTarget(e) == v || this.shortestPathGraph.getEdgeSource(e) == v ? 1 : 0)
							.sum() % 2 == 1).collect(Collectors.toSet());
			assert oddDegree.size() % 2 == 0;
			AsSubgraph<V,DefaultWeightedEdge> subgraph = new AsSubgraph<>(this.shortestPathGraph,oddDegree);

			//Construct minimum weight perfect matching
			KolmogorovWeightedPerfectMatching<V,DefaultWeightedEdge> matchingAlgorithm = new KolmogorovWeightedPerfectMatching<>(subgraph);
			MatchingAlgorithm.Matching<V, DefaultWeightedEdge> matching = matchingAlgorithm.getMatching();

			//Construct shortest path graph containing only edges in mst or matching
			HashSet<DefaultWeightedEdge> edgeSet = new HashSet<>();
			edgeSet.addAll(mst.getEdges());
			edgeSet.addAll(matching.getEdges());
			
			AsSubgraph<V,DefaultWeightedEdge> remaining = new AsSubgraph<>(this.shortestPathGraph,this.shortestPathGraph.vertexSet(),edgeSet);
			List<V> eulerTour = new ArrayList<>();
			//Choose random vertex to start euler tour, all vertices should have even degree
			eulerTour.add(this.shortestPathGraph.vertexSet().iterator().next());

			//Construct euler tour
			while(remaining.edgeSet().size()>0){
				Optional<V> next = adjacent(remaining, eulerTour.get(eulerTour.size() - 1)).findFirst();
				if (next.isEmpty()){
					//Probably more correct to throw exception here, but that won't work in the VPL
					throw new java.lang.Error("Euler tour could not be constructed");
				}else{
					remaining.removeEdge(remaining.getEdge(eulerTour.get(eulerTour.size() - 1),next.get()));
					eulerTour.add(next.get());
				}
			}

			this.tour = new ArrayList<>();
			HashSet<V> visited = new HashSet<>();//DS for efficient O(1) contains checks
			for (V v:eulerTour) {
				if(!visited.contains(v)){
					this.tour.add(v);
					visited.add(v);
				}
			}
		}
		
	}

	private void computeShortestPathGraph() {
		this.shortestPathGraph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.shortestPathGraph,this.graph.vertexSet());
		//Graphs.addGraph(this.shortestPathGraph,this.graph);
		ManyToManyShortestPathsAlgorithm<V, E> shortestPathAlgorithm = new DijkstraManyToManyShortestPaths<>(this.graph);
		for (V s: this.graph.vertexSet()) {
			for (V t: this.graph.vertexSet()){
				if(!s.equals(t) && !this.shortestPathGraph.containsEdge(s,t) && !this.shortestPathGraph.containsEdge(t,s)){
					if(shortestPathAlgorithm.getPath(s,t)==null){
						unconnectedVertices = new Pair<>(s,t);
						tourExists = false;
						return;
					}else {
						DefaultWeightedEdge edge = this.shortestPathGraph.addEdge(s, t);
						this.shortestPathGraph.setEdgeWeight(edge, shortestPathAlgorithm.getPathWeight(s, t));
					}
				}
			}
		}
		tourExists = true;
	}

	private <X,Y> Stream<X> adjacent(Graph<X,Y> graph,X start){
		return graph.vertexSet().stream().filter(x-> graph.containsEdge(start,x));
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
