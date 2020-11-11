import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;
import java.util.stream.Collectors;

public class mySpanningTree {
	
	//Receives a graph and computes a minimum spanning tree
	public static AsSubgraph<Integer, DefaultWeightedEdge> computeMST(Graph<Integer, DefaultWeightedEdge> graph) {
		AsSubgraph<Integer, DefaultWeightedEdge> tree = new AsSubgraph<>(graph, graph.vertexSet(), new HashSet<>());

		//Sort edges by weight
		var edges = graph.edgeSet().stream()
				.sorted(Comparator.comparingDouble(graph::getEdgeWeight))
				.iterator();

		//Initialize connected components, in the beginning all vertices are their own component
		List<Set<Integer>> subgraphs = new ArrayList<>();
		for (Integer vertex: graph.vertexSet()) {
			Set<Integer> set = new HashSet<>();
			set.add(vertex);
			subgraphs.add(set);
		}

		//While there are multiple components, add edges that connect two
		while(subgraphs.size()>1) {
			DefaultWeightedEdge edge = edges.next();
			//Get the components containing source and target
			var first = subgraphs.stream()
					.filter(x->x.contains(graph.getEdgeSource(edge)))
					.collect(Collectors.toList());
			var second = subgraphs.stream()
					.filter(x->x.contains(graph.getEdgeTarget(edge)))
					.collect(Collectors.toList());

			//Should only be one subgraph, otherwise something went wrong
			assert first.size()==1;
			assert second.size()==1;

			Set<Integer> g1 = first.get(0);
			Set<Integer> g2 = second.get(0);

			//If source and target are in different components, adding the edge between them does not result in a cycle, so add it
			if(g1!=g2){
				//Add edge
				tree.addEdge(graph.getEdgeSource(edge),graph.getEdgeTarget(edge),edge);
				//Fuse the two components together
				if(g1.size()>g2.size()) {
					subgraphs.remove(g2);
					g1.addAll(g2);
				}else{
					subgraphs.remove(g1);
					g2.addAll(g1);
				}
			}

		}
		return tree;
	}

}
