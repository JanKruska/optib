import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		Map<Integer,Boolean> colour = new HashMap<>();
		for (Integer vertex : graph.vertexSet()) {
			distances.put(vertex, Double.MAX_VALUE);
			predecessors.put(vertex, null);
			colour.put(vertex,Boolean.FALSE);
        }
		distances.put(startVertex, 0.);
		colour.put(startVertex,Boolean.TRUE);
		adjacent(startVertex)
				.filter(x->colour.get(x)==Boolean.FALSE)
				.forEach(x->updateVertexDistance(startVertex,x));

		//Only works on graphs where a path to every other node is possible (as of now)
		//TODO: break if only unreachable nodes remain
		for (int i = 0; i < graph.vertexSet().size()-1; i++) {
			Map.Entry<Integer, Double> min = Collections.min(
						distances.entrySet().stream()
							.filter(x->colour.get(x.getKey())==Boolean.FALSE)
							.collect(Collectors.toList()),
						Map.Entry.comparingByValue());

			colour.put(min.getKey(),Boolean.TRUE);
			adjacent(min.getKey())
					.filter(x->colour.get(x)==Boolean.FALSE)
					.forEach(x->updateVertexDistance(min.getKey(),x));
		}
	}

	private Stream<Integer> adjacent(Integer start){
		return graph.vertexSet().stream().filter(x-> graph.containsEdge(start,x));
	}

	private void updateVertexDistance(Integer source, Integer target){
		Integer pred = predecessors.get(target);
		if(pred==null) {
			distances.put(target,graph.getEdgeWeight(graph.getEdge(source,target)));
			predecessors.put(target, source);
		}else if(distances.get(pred) + graph.getEdgeWeight(graph.getEdge(source,target)) < distances.get(target)){
			distances.put(target, distances.get(pred) + graph.getEdgeWeight(graph.getEdge(pred,target)));
			predecessors.put(target, source);
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
