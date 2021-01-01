/* Read only
Changes will have no impact on execution/evaluation within VPL
*/


import org.jgrapht.alg.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of a matchday of a football season
 */
public class Matchday {

    private Map<Pair<String, String>, Pair<Integer, Integer>> matches = new HashMap<Pair<String,String>, Pair<Integer,Integer>>();
    private Map<String, Pair<String, String>> pairingOf = new HashMap<String, Pair<String,String>>();

    public Matchday() {   }
    
    public void addMatch(Pair<String, String> pairing, Pair<Integer, Integer> score) {
		matches.put(pairing, score);
		pairingOf.put(pairing.getFirst(), pairing);
		pairingOf.put(pairing.getSecond(), pairing);

	}
    
    public Map<Pair<String, String>, Pair<Integer, Integer>> getMatches() {
        return matches;
    }

    public Pair<Integer, Integer> getScore(Pair<String, String> pairing) {
        return matches.get(pairing);
    }
    
    public Pair<String, String> getPairing(String pairing) {
		return pairingOf.get(pairing);
	}
}
