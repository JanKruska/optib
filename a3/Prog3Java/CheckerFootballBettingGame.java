/* Read only
Changes will have no impact on execution/evaluation within VPL
*/


import java.util.HashSet;

import org.jgrapht.alg.util.Pair;

public class CheckerFootballBettingGame {
	
	public static <V,E> void checkBettinggame(MyFootballBettingGame bettingGame) {
		if (new HashSet<String>(bettingGame.getBets().values()).size() != 17) {
			System.out.println("The betting does not choose 17 different teams.");
		}
		else {
			int solutionValue = 0;
			for (Matchday matchday : bettingGame.getBets().keySet()) {
				String chosenTeam = bettingGame.getBets().get(matchday);
				Pair<String, String> pairing = matchday.getPairing(chosenTeam);
				Pair<Integer, Integer> score = matchday.getScore(pairing);
				if (score.getFirst() == score.getSecond()) {
					solutionValue++;
				}
				else if ((chosenTeam.equals(pairing.getFirst()) && score.getFirst() > score.getSecond())
						|| (chosenTeam.equals(pairing.getSecond()) && score.getFirst() < score.getSecond())) {
						solutionValue += 3;
				}
			}
			if (solutionValue != 51) {
				System.out.println("The betting is valid, but not optimal.");
			}
			else {
				System.out.println("The betting is valid with an optimal value of 51.");
			}
		}
	}
}
