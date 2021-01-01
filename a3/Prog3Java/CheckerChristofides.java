/* Read only
Changes will have no impact on execution/evaluation within VPL
*/


import java.util.HashSet;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class CheckerChristofides {
	
	public static <V,E> void checkTour(MyChristofides<V, E> christofides) {
		Graph<V, E> graph = christofides.getGraph();
		if (christofides.isTourExists()) {
			List<V> tour = christofides.getTour();
			if (new HashSet<V>(tour).size() != tour.size()-1 || tour.size()-1 != graph.vertexSet().size() || tour.get(0) != tour.get(tour.size()-1)) {
				System.out.println("The computed tour should contain all vertices exactly once, except for the starting node. Start and end should be the same vertex.");
			}
			else {
				double tourValue = 0;
				DijkstraShortestPath<V, E> dijkstraShortestPath = new DijkstraShortestPath<V, E>(graph);
				for (int i = 0; i < tour.size()-1; i++) {
					tourValue += dijkstraShortestPath.getPathWeight(tour.get(i), tour.get(i+1));
				}
				if (Double.isInfinite(tourValue)) {
					System.out.println("The tour uses edges between unconnected vertices.");
				}
				else {
					System.out.println("The tour is valid and has value "+tourValue+".");
				}
			}
		}
		else {
			if (new ConnectivityInspector<V, E>(graph).pathExists(christofides.getUnconnectedVertices().getFirst(), christofides.getUnconnectedVertices().getSecond())) {
				System.out.println("The \"unconnected\" pair of vertices is connected.");
			}
			else {
				System.out.println("There exists no tour, since the computed pair of vertices is not connected.");
			}
		}
	}
}
