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
		if(graph.edgeSet().stream().anyMatch(x->graph.getEdgeWeight(x)<0)){
			bellmanFord();
		}else {
			dijkstra();
		}
	}

	/**
	 * Bellman-Ford shortest path, currently only for undirected graphs
	 */
	public void bellmanFord(){
		//Distance to all vertices is inf and pred is null
		for (Integer vertex : graph.vertexSet()) {
			distances.put(vertex, Double.MAX_VALUE);
			predecessors.put(vertex, null);
		}
		//Distance to start vertex is zero
		distances.put(startVertex, 0.);

		//V-1 times
		for (int i = 1; i < graph.vertexSet().size()-1; i++) {
			//relax each edge
			for (DefaultWeightedEdge edge: graph.edgeSet()) {
				//Get vertices of edge and respective weight
				Integer u = graph.getEdgeSource(edge);
				Integer v = graph.getEdgeTarget(edge);
				double weight = graph.getEdgeWeight(edge);

				//Currently checks edge both ways
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

	/**
	 * Dijkstras shortest path
	 */
	public void dijkstra() {
		Map<Integer,Boolean> added = new HashMap<>();
		//Initialize all distances as Inf, pred as null
		for (Integer vertex : graph.vertexSet()) {
			distances.put(vertex, Double.MAX_VALUE);
			predecessors.put(vertex, null);
			added.put(vertex,Boolean.FALSE);
		}
		//Add start vertex with distance = 0
		distances.put(startVertex, 0.);
		added.put(startVertex,Boolean.TRUE);
		//Update all neighbours of start vertex
		adjacent(startVertex)
				.filter(x->added.get(x)==Boolean.FALSE)
				.forEach(x->updateVertexDistance(startVertex,x));

		for (int i = 0; i < graph.vertexSet().size()-1; i++) {
			//Determine if there are any reachable (non infinite distance) non-added vertices
			if (distances.entrySet().stream().anyMatch(x->added.get(x.getKey())==Boolean.FALSE && x.getValue()!=Double.MAX_VALUE)) {
				//Get vertex,distance pair with minimum distance
				Map.Entry<Integer, Double> min = Collections.min(
						distances.entrySet().stream()
								.filter(x -> added.get(x.getKey()) == Boolean.FALSE) //Only consider unadded vertices
								.collect(Collectors.toList()),
						Map.Entry.comparingByValue());
				//Set the minimum vertex to added
				added.put(min.getKey(), Boolean.TRUE);
				//Update all neighbouring non-added vertices
				adjacent(min.getKey())
						.filter(x -> added.get(x) == Boolean.FALSE)
						.forEach(x -> updateVertexDistance(min.getKey(), x));
			}else{
				break;
			}
		}
	}

	private Stream<Integer> adjacent(Integer start){
		return graph.vertexSet().stream().filter(x-> graph.containsEdge(start,x));
	}

	private void updateVertexDistance(Integer source, Integer target){
		if(distances.get(source) + graph.getEdgeWeight(graph.getEdge(source,target)) < distances.get(target)){
			distances.put(target, distances.get(source) + graph.getEdgeWeight(graph.getEdge(source,target)));
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
