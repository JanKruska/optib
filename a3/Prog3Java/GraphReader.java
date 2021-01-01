/* Read only
Changes will have no impact on execution/evaluation within VPL
*/


import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GraphReader {
	
	
	public static Graph<Integer, DefaultWeightedEdge> readGraph(String pathToGraphFile) {
        Graph<Integer, DefaultWeightedEdge> graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);       
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(pathToGraphFile));
			String line;
			while (!reader.readLine().contains("label")) {}
			line = reader.readLine();
			while (!line.equals("")) {
				graph.addVertex(Integer.parseInt(line.trim()));
				line = reader.readLine();
			}

			while (!reader.readLine().contains("weight")) {}
			line = reader.readLine();
			while (line!=null) {
				String[] strings = line.split(" ");
				graph.addEdge(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
				graph.setEdgeWeight(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), Double.parseDouble(strings[2]));
				line = reader.readLine();
			}
		}
		catch(IOException e) {
			System.err.println("Cannot read file");
        	e.printStackTrace();
		}
		
		return graph;
	}
}