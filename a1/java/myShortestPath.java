import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

//Implements shortest path algorithms. Can return distances to vertices from a given start vertex and the corresponding shortest path.
public class myShortestPath {
	//Variables
	private Graph<Integer, DefaultWeightedEdge> graph;			//The graph in which we search for a shortest path
	private Integer startVertex;								//The start vertex
	private HashMap<Integer, Double> distances;					//Distances from each vertex to the start vertex
	private HashMap<Integer, Integer> predecessors;				//Predecessors for each vertex on a shortest path to the start vertex
	
	//Constructor
	public myShortestPath(Graph<Integer, DefaultWeightedEdge> graph, Integer startVertex) {
		this.graph = graph;
		this.startVertex = startVertex;
		distances = new HashMap<>();
		predecessors = new HashMap<>();
	}
	
	//Computes distances and predecessors for all nodes
	public void computeDistPred() {

		for (Integer vertex : graph.vertexSet()) {
			distances.put(vertex, Double.MAX_VALUE);
			predecessors.put(vertex, null);
		}
		distances.put(startVertex, 0.);

		for (int i = 1; i < graph.vertexSet().size()-1; i++) {
			for (var edge: graph.edgeSet()) {
				Integer u = graph.getEdgeSource(edge);
				Integer v = graph.getEdgeTarget(edge);
				double weight = graph.getEdgeWeight(edge);

				if(distances.get(u) + weight < distances.get(v)) {
					distances.put(v, distances.get(u) + weight);
					predecessors.put(v, u);
				}else if(distances.get(v) + weight < distances.get(u)){
					distances.put(u, distances.get(v) + weight);
					predecessors.put(u, v);
				}

			}
		}
	}

	//Constructs the shortest path from the start node to a given end node using the list of predecessors
	public ArrayList<Integer> constructPathToNode(Integer endVertex) {
		ArrayList<Integer> path = new ArrayList<>();
		path.add(endVertex);
		while (!path.contains(startVertex)) {
			Integer currentVertex = path.get(path.size()-1);
			Integer nextVertex = predecessors.get(currentVertex);
			path.add(nextVertex);
		}
		Collections.reverse(path);
		return path;
	}
		
	//Returns the distance to a given end node
	public double getDistanceToNode(Integer endVertex) {
		return distances.get(endVertex);
	}
}
