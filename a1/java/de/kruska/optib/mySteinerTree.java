package de.kruska.optib;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class mySteinerTree {
	
	//Receives a graph and computes a steiner tree
	public static AsSubgraph<Integer, DefaultWeightedEdge> computeSteinerTree(Graph<Integer, DefaultWeightedEdge> graph, HashSet<Integer> terminals) {
		Graph<Integer, DefaultWeightedEdge> steinerTree = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
		//Add terminals
		terminals.forEach(steinerTree::addVertex);

		Set<Integer> vertices = new HashSet<>();
		Set<DefaultWeightedEdge> edges = new HashSet<>();

		// compute paths between all terminal pairs
		for (Integer vertexA : steinerTree.vertexSet()) {
			for (Integer vertexB : steinerTree.vertexSet()) {
				//ensure uniqueness of pairs
				if (vertexA < vertexB) {
					myShortestPath shortestPath = new myShortestPath(graph, vertexA);
					shortestPath.computeDistPred();
					ArrayList<Integer> path = shortestPath.constructPathToNode(vertexB);
					//Remember all vertices and edges on shortest path
					for (int i = 0; i < path.size()-1; i++) {
						edges.add(graph.getEdge(path.get(i), path.get(i+1)));
						vertices.add(path.get(i));
					}
				}
			}
		}
		//Add all vertices that occurred in shortest paths
		vertices.forEach(steinerTree::addVertex);
		//Add all edges that occured in shortest paths
		edges.forEach(x->steinerTree.addEdge(graph.getEdgeSource(x), graph.getEdgeTarget(x)));
		//calculate min-spanning tree of graph consisting of the shortest paths between vertices
		return mySpanningTree.computeMST(steinerTree);
	}

}
