package de.kruska.optib;
/* Read only
Changes will have no impact on execution/evaluation within VPL
*/

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashSet;

public class CheckerSteinerTree {
	
	public static void checkSteinerTree(AsSubgraph<Integer, DefaultWeightedEdge> steinerTree, HashSet<Integer> terminals) {
		
		if (new ConnectivityInspector<>(steinerTree).connectedSetOf(terminals.iterator().next()).containsAll(terminals)) {
			double weight = 0;
			for (DefaultWeightedEdge edge : steinerTree.edgeSet()) {
				weight += steinerTree.getEdgeWeight(edge);
			}
			System.out.println("The computed graph connects all terminal nodes and has weight "+weight+".");
		}
		else {
			System.out.println("The computed graph does not connect all terminal nodes.");
		}
	}
}
