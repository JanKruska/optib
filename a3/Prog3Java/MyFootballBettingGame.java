

import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.util.SupplierUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jgrapht.util.SupplierUtil.DEFAULT_WEIGHTED_EDGE_SUPPLIER;

public class MyFootballBettingGame {
	private final List<Matchday> matchdays;
	private Map<Matchday, String> bets;

	public MyFootballBettingGame(List<Matchday> matchdays) {
		this.matchdays = matchdays;
	}

	/**
	 * Computes optimal betting Strategy.
	 *
	 * Problem can be solved by calculating maximal weighted matching on a bipartite "betting" graph. Color class U
	 * contains the matchdays, color class W contains the teams. Edges between matchdays and teams indicate the profit
	 * from betting on that team on that matchday (3, 1, 0).
	 *
	 */
	//Computes an optimal solution to the football betting game.
	public void computeOptimalBets() {

		//////////////////////////////////////////////////////////////////////////////
		// Read in teams & matches
		Map<Integer, Matchday> matchdayOfVertex = new HashMap<>();
		Map<Matchday, Integer> vertexOfMatchday = new HashMap<>();
		Map<Integer, String> teamOfVertex = new HashMap<>();
		Map<String, Integer> vertexOfTeam = new HashMap<>();

		this.matchdays.forEach((Matchday m) -> {
			vertexOfMatchday.put(m,null);
		});
		
		for (Map.Entry<Pair<String, String>, Pair<Integer, Integer>> entry : this.matchdays.get(0).getMatches().entrySet()) {
			vertexOfTeam.put(entry.getKey().getFirst(),null);
			vertexOfTeam.put(entry.getKey().getSecond(),null);
		}

		//////////////////////////////////////////////////////////////////////////////
		// Initialize betting matching graph
		DefaultUndirectedWeightedGraph<Integer, DefaultWeightedEdge> graph =
				new DefaultUndirectedWeightedGraph<>(SupplierUtil.createIntegerSupplier(), DEFAULT_WEIGHTED_EDGE_SUPPLIER);

		// add matchday vertices to graph
		for (Map.Entry<Matchday, Integer> entry : vertexOfMatchday.entrySet()) {
			Integer matchdayVertex = graph.addVertex();
			vertexOfMatchday.put(entry.getKey(), matchdayVertex);
			matchdayOfVertex.put(matchdayVertex, entry.getKey());
		}

		// add team vertices to graph
		for (Map.Entry<String, Integer> entry : vertexOfTeam.entrySet()) {
			Integer teamVertex = graph.addVertex();
			vertexOfTeam.put(entry.getKey(), teamVertex);
			teamOfVertex.put(teamVertex, entry.getKey());
		}

		// Add betting edges to graph
		for (Map.Entry<Matchday, Integer> entry : vertexOfMatchday.entrySet()) {
			Matchday m = entry.getKey();
			Integer mVertex = entry.getValue();
			for (Map.Entry<Pair<String, String>, Pair<Integer, Integer>> match : m.getMatches().entrySet()) {
				Pair<String, String> teams = match.getKey();
				Pair<Integer, Integer> scores = match.getValue();
				int scoreTeam1 = scores.getFirst() > scores.getSecond() ? 3 :
																scores.getFirst().equals(scores.getSecond()) ? 1 : 0;
				int scoreTeam2 = scores.getSecond() > scores.getFirst() ? 3 :
																scores.getSecond().equals(scores.getFirst()) ? 1 : 0;
				DefaultWeightedEdge e = graph.addEdge(mVertex, vertexOfTeam.get(teams.getFirst()));
				graph.setEdgeWeight(e, scoreTeam1);
				 e = graph.addEdge(mVertex, vertexOfTeam.get(teams.getSecond()));
				graph.setEdgeWeight(e, scoreTeam2);
			}
		}

		//////////////////////////////////////////////////////////////////////////////
		// solve maximal weighted matching problem
		MyBipartiteMatching<Integer, DefaultWeightedEdge> matching = new MyBipartiteMatching<>(graph);
		matching.computeMaximumWeightedMatching();

		//////////////////////////////////////////////////////////////////////////////
		// convert graph solution to this.bets
		bets = new HashMap<>();
		for (DefaultWeightedEdge e : matching.getMatching()) {
			Integer a = graph.getEdgeSource(e);
			Integer b = graph.getEdgeTarget(e);
			if (!matchdayOfVertex.containsKey(a)){
				// swap a and b such that: a is matchday and b is team
				int c = b;
				b = a;
				a = c;
			}
			bets.put(matchdayOfVertex.get(a), teamOfVertex.get(b));
		}
	}
	
	public Map<Matchday, String> getBets() {
		return bets;
	}
	
	public List<Matchday> getMatchdays() {
		return matchdays;
	}
}
