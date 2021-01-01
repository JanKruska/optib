/* Read only
Changes will have no impact on execution/evaluation within VPL
*/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Main {
	public static void main(String[] args) throws IOException {
		
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Which algorithm would you like to test (Bipartition, BipartiteMatching, Football, Christofides)?");
        String algorithm = reader.readLine();
        
        if (algorithm.equals("Bipartition")) {
            System.out.println("Which instance would you like to solve (1,2,3,4)?");
            String graphNumber = reader.readLine();
        	System.out.println("Computing Bipartitioning of Graph "+graphNumber+":");
        	Graph<Integer, DefaultWeightedEdge> graph = GraphReader.readGraph("graph"+graphNumber+".lgf");
        	MyBipartition<Integer, DefaultWeightedEdge> bipartition = new MyBipartition<Integer, DefaultWeightedEdge>(graph);
        	bipartition.computeBipartitioning();
        	CheckerBipartition.checkBipartition(bipartition);
        }
        if (algorithm.equals("BipartiteMatching")) {
            System.out.println("Which instance would you like to solve (1,2,3,4)?");
            String graphNumber = reader.readLine();
        	System.out.println("Computing Bipartite Matching of Graph "+graphNumber+":");
        	Graph<Integer, DefaultWeightedEdge> graph = GraphReader.readGraph("graph"+graphNumber+".lgf");
        	MyBipartiteMatching<Integer, DefaultWeightedEdge> matching = new MyBipartiteMatching<Integer, DefaultWeightedEdge>(graph);
        	matching.computeMaximumWeightedMatching();
        	CheckerBipartiteMatching.checkBipartition(matching);
        }
        if (algorithm.equals("Football")) {
        	System.out.println("Computing Optimal Betting:");
        	List<Matchday> matchdays = FootballReader.readMatchdays("Season12.txt");
        	MyFootballBettingGame football = new MyFootballBettingGame(matchdays);
        	football.computeOptimalBets();
        	CheckerFootballBettingGame.checkBettinggame(football);
        }
        if (algorithm.equals("Christofides")) {
            System.out.println("Which instance would you like to solve (1,2,3,4)?");
            String graphNumber = reader.readLine();
        	System.out.println("Computing Christofides Tour for Graph "+graphNumber+":");
        	Graph<Integer, DefaultWeightedEdge> graph = GraphReader.readGraph("graph"+graphNumber+".lgf");
        	MyChristofides<Integer, DefaultWeightedEdge> christofides = new MyChristofides<Integer, DefaultWeightedEdge>(graph);
        	christofides.computeTour();
        	CheckerChristofides.checkTour(christofides);
        }
        reader.close();
	}
}