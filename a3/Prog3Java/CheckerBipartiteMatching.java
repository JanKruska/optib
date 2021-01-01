/* Read only
Changes will have no impact on execution/evaluation within VPL
*/


import java.util.HashSet;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.PartitioningAlgorithm.Partitioning;
import org.jgrapht.alg.matching.MaximumWeightBipartiteMatching;
import org.jgrapht.alg.partition.BipartitePartitioning;

public class CheckerBipartiteMatching {
	
	public static <V,E> void checkBipartition(MyBipartiteMatching<V, E> bipartiteMatching) {
		Graph<V, E> graph = bipartiteMatching.getGraph();
		HashSet<V> matchedNodes = new HashSet<V>();
		for (E edge : bipartiteMatching.getMatching()) {
			matchedNodes.add(graph.getEdgeSource(edge));
			matchedNodes.add(graph.getEdgeTarget(edge));
		}
		if (matchedNodes.size() != 2*bipartiteMatching.getMatching().size()) {
			System.out.println("The computed matching is invalid.");
		}
		else {
			double weight = 0;
			for (E edge : bipartiteMatching.getMatching()) {
				weight += graph.getEdgeWeight(edge);
			}
			Partitioning<V> partitioning = new BipartitePartitioning<V, E>(graph).getPartitioning();
			MaximumWeightBipartiteMatching<V, E> maximumWeightBipartiteMatching = new MaximumWeightBipartiteMatching<V,E>(graph, partitioning.getPartition(0), partitioning.getPartition(1));
			double compareWeight = 0;	//getweight is broken in jgrapht 1.4
			for (E edge : maximumWeightBipartiteMatching.getMatching()) {
				compareWeight += graph.getEdgeWeight(edge);
			}
			if (weight != compareWeight) {
				System.out.println("The computed matching has not maximum weight.");
			}
			else {
				System.out.println("The computed matching has maximum weight "+weight+".");
			}
		}
	}
}
