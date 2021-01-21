package de.kruska.optib;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

public class mySpanningTree {
	
	//Receives a graph and computes a minimum spanning tree
	public static AsSubgraph<Integer, DefaultWeightedEdge> computeMST(Graph<Integer, DefaultWeightedEdge> graph) {
		AsSubgraph<Integer, DefaultWeightedEdge> tree = new AsSubgraph<>(graph, graph.vertexSet(), new HashSet<>());

		//Sort edges by weight
		Iterator<DefaultWeightedEdge> edges = graph.edgeSet().stream()
				.sorted(Comparator.comparingDouble(graph::getEdgeWeight))
				.iterator();

		//Initialize connected components, in the beginning all vertices are their own component
		MyDisjointSet disjointSet = new MyDisjointSet(graph.vertexSet());

		//While there are multiple components, add edges that connect two
		while(edges.hasNext()) {
			DefaultWeightedEdge edge = edges.next();
			//Get the componentsIds containing source and target
			Integer compSource = disjointSet.SEARCH(graph.getEdgeSource(edge));
			Integer compTarget = disjointSet.SEARCH(graph.getEdgeTarget(edge));

			//If source and target are in different components, adding the edge between them does not result in a cycle, so add it
			if(!compSource.equals(compTarget)){
				//Add edge
				tree.addEdge(graph.getEdgeSource(edge),graph.getEdgeTarget(edge),edge);
				//Fuse the two components together
				disjointSet.MERGE(compSource, compTarget);
			}
		}
		return tree;
	}

}
