/* Read only
Changes will have no impact on execution/evaluation within VPL
*/


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.util.Pair;

public class FootballReader {
    public static List<Matchday> readMatchdays(String pathToFootballFile) {
        List<Matchday> matchdays = new ArrayList<Matchday>();
		BufferedReader reader;
        try {
			reader = new BufferedReader(new FileReader(pathToFootballFile));
            Matchday currentMatchday = null;
			String line = reader.readLine();
			while (line!=null) {
                if (!line.isEmpty()) {
                    if(line.charAt(0) == '#') {
                        currentMatchday = new Matchday();
                        matchdays.add(currentMatchday);
                    } else {
                        String[] items = line.split(",");
                        Pair<String, String> pairing = Pair.of(items[0], items[1]);
                        Pair<Integer, Integer> score = Pair.of(Integer.parseInt(items[2]), Integer.parseInt(items[3]));
                        currentMatchday.addMatch(pairing, score);
                    }
                }
                line = reader.readLine();
            }
        }
        catch(IOException e) {
			System.err.println("Cannot read file");
        	e.printStackTrace();
        }
        return matchdays;
    }
}
