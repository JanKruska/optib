

import java.util.List;
import java.util.Map;

public class MyFootballBettingGame {
	private final List<Matchday> matchdays;
	private Map<Matchday, String> bets;

	public MyFootballBettingGame(List<Matchday> matchdays) {
		this.matchdays = matchdays;
	}
	
	//Computes an optimal solution to the football betting game.
	public void computeOptimalBets() {
        // TODO implement algorithm here

	}
	
	public Map<Matchday, String> getBets() {
		return bets;
	}
	
	public List<Matchday> getMatchdays() {
		return matchdays;
	}
}
