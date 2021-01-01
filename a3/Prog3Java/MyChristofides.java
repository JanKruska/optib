
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.util.Pair;

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
        // TODO implement algorithm here
		
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
