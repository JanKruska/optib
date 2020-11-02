import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class mySpanningTree {
	
	//Receives a graph and computes a minimum spanning tree
	public static AsSubgraph<Integer, DefaultWeightedEdge> computeMST(Graph<Integer, DefaultWeightedEdge> graph) {
		AsSubgraph<Integer, DefaultWeightedEdge> tree = new AsSubgraph<Integer, DefaultWeightedEdge>(graph, graph.vertexSet(), new HashSet<DefaultWeightedEdge>());
		var edges = graph.edgeSet().stream().sorted().iterator();

		Set<Set<Integer>> subgraphs = new HashSet<Set<Integer>>();
		for (Integer vertex: graph.vertexSet()) {
			subgraphs.add(new HashSet<>(vertex));
		}

		while(subgraphs.size()>1) {
			DefaultWeightedEdge edge = edges.next();
			//Get all subgraphs containing source and target
			var first = subgraphs.stream().filter(x->x.contains(graph.getEdgeSource(edge)));
			var second = subgraphs.stream().filter(x->x.contains(graph.getEdgeTarget(edge)));

			//Should only be one subgraph, otherwise something went wrong
			assert first.count()==1;
			assert second.count()==1;

			Set<Integer> g1 = first.findFirst().get();
			Set<Integer> g2 = second.findFirst().get();

			if(g1!=g2){
				tree.addEdge(graph.getEdgeSource(edge),graph.getEdgeTarget(edge),edge);
				g1.addAll(g2);
				subgraphs.remove(g2);
			}

		}
		return tree;
	}

}
