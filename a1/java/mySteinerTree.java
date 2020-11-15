import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class mySteinerTree {
	
	//Receives a graph and computes a steiner tree
	public static AsSubgraph<Integer, DefaultWeightedEdge> computeSteinerTree(Graph<Integer, DefaultWeightedEdge> graph, HashSet<Integer> terminals) {
		Graph<Integer, DefaultWeightedEdge> steinerTree = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
		for (Integer terminal : terminals) {
			steinerTree.addVertex(terminal);
		}

		// compute distance between all vertex pairs
		for (Integer vertexA : steinerTree.vertexSet()) {
			for (Integer vertexB : steinerTree.vertexSet()) {

				//ensure uniqueness of pairs
				if (vertexA < vertexB) {
					myShortestPath shortestPath = new myShortestPath(graph, vertexA);
					shortestPath.computeDistPred();
					steinerTree.setEdgeWeight(steinerTree.addEdge(vertexA, vertexB), shortestPath.getDistanceToNode(vertexB));
				}
			}
		}

		// calculate min-spanning tree of -> dense <- terminal graph
		return mySpanningTree.computeMST(steinerTree);
	}

}
