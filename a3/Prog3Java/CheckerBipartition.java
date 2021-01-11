/* Read only
Changes will have no impact on execution/evaluation within VPL
*/


import java.util.HashSet;
import java.util.List;

import org.jgrapht.Graph;

public class CheckerBipartition {
	
	public static <V,E> void checkBipartition(MyBipartition<V, E> bipartition) {
		Graph<V, E> graph = bipartition.getGraph();
		if (bipartition.isBipartite()) {
			if (bipartition.getPartitioning().size() != 2) {
				System.out.println("The number of partitioning classes does not equal 2.");
			}
			else {
				HashSet<V> vertexSet = new HashSet<V>(bipartition.getPartitioning().get(0));
				vertexSet.addAll(bipartition.getPartitioning().get(1));
				if (bipartition.getPartitioning().get(0).size() + bipartition.getPartitioning().get(1).size() == graph.vertexSet().size()
						&& !vertexSet.equals(graph.vertexSet())) {
					System.out.println("The partitioning is invalid.");
				}
				else {
					boolean edgesInClasses = false;
					outerloop:
					for (int i = 0; i < 1; i++) {
						HashSet<V> partition = bipartition.getPartitioning().get(i);
						for (V vertex : partition) {
							for (V vertex2 : partition) {
								if (graph.containsEdge(vertex, vertex2)) {
									edgesInClasses = true;
									break outerloop;
								}
							}
						}
					}
					if (edgesInClasses) {
						System.out.println("There exists at least one edge within a bipartition class.");
					}
					else {
						System.out.println("The computed bipartitioning is valid.");
					}
				}
			}
		}
		else {
			List<V> oddCycle = bipartition.getOddCycle();
			if (new HashSet<V>(oddCycle).size() != oddCycle.size()-1 || oddCycle.size() <2 || oddCycle.get(0) != oddCycle.get(oddCycle.size()-1)) {
				System.out.println("The computed cycle should be simple and start and end with the same vertex.");
			}
			else {
				if (oddCycle.size() % 2 != 0) {
					System.out.println("The computed cycle is not of odd length.");
				}
				else {
					boolean edgesExist = true;
					for (int i = 0; i < oddCycle.size()-1; i++) {
						if (!graph.containsEdge(oddCycle.get(i), oddCycle.get(i+1))) {
							edgesExist = false;
							break;
						}
					}
					if (edgesExist) {
						System.out.println("The computed odd cycle is valid.");
					}
					else {
						System.out.println("The cycle uses edges that do not exist.");
					}
				}
			}
		}
	}
}
