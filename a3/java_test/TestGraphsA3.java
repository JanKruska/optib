import org.jgrapht.Graph;
import org.jgrapht.generate.GnmRandomBipartiteGraphGenerator;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.util.SupplierUtil;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestGraphsA3 {

    String dir = "a3/Prog3Java/";

    @RepeatedTest(10)
    void mst(RepetitionInfo repetitionInfo) {
        String graphNumber = Integer.toString(repetitionInfo.getCurrentRepetition());
        System.out.println("Computing bipartition of Graph "+graphNumber+":");
        Graph<Integer, DefaultWeightedEdge> graph;
        if (repetitionInfo.getCurrentRepetition() < 5) {
            graph = GraphReader.readGraph(dir + "graph" + graphNumber + ".lgf");
        } else {
            GnmRandomBipartiteGraphGenerator<Integer, DefaultWeightedEdge> gnm
                    = new GnmRandomBipartiteGraphGenerator<Integer, DefaultWeightedEdge>(1000,  1200,  2000);

            graph = new Pseudograph<Integer, DefaultWeightedEdge>(
                    SupplierUtil.createIntegerSupplier(), SupplierUtil.DEFAULT_WEIGHTED_EDGE_SUPPLIER, false);
            Map<String, Integer> stuff = new HashMap<String, Integer>();
            gnm.generateGraph(graph, stuff);
            graph.addEdge(0,1);

        }
        MyBipartition<Integer, DefaultWeightedEdge> bipartition = new MyBipartition<Integer, DefaultWeightedEdge>(graph);
        bipartition.computeBipartitioning();
        CheckerBipartition.checkBipartition(bipartition);
    }

    @RepeatedTest(10)
    void bipartiteMatching(RepetitionInfo repetitionInfo) {
        String graphNumber = Integer.toString(repetitionInfo.getCurrentRepetition());
        System.out.println("Computing bipartition of Graph "+graphNumber+":");
        Graph<Integer, DefaultWeightedEdge> graph;
        if (repetitionInfo.getCurrentRepetition() < 5) {
            graph = GraphReader.readGraph(dir + "graph" + graphNumber + ".lgf");
        } else {
            GnmRandomBipartiteGraphGenerator<Integer, DefaultWeightedEdge> gnm
                    = new GnmRandomBipartiteGraphGenerator<Integer, DefaultWeightedEdge>(1000,  1200,  2000);
            graph = new Pseudograph<Integer, DefaultWeightedEdge>(
                    SupplierUtil.createIntegerSupplier(), SupplierUtil.DEFAULT_WEIGHTED_EDGE_SUPPLIER, false);
            Map<String, Integer> stuff = new HashMap<String, Integer>();
            gnm.generateGraph(graph, stuff);

        }
        MyBipartiteMatching<Integer, DefaultWeightedEdge> matching = new MyBipartiteMatching<Integer, DefaultWeightedEdge>(graph);
        matching.computeMaximumWeightedMatching();
        CheckerBipartiteMatching.checkBipartition(matching);
    }

    @RepeatedTest(1)
    void footballTester(RepetitionInfo repetitionInfo) {
        System.out.println("Computing Optimal Betting:");
        List<Matchday> matchdays = FootballReader.readMatchdays(dir+"Season12.txt");
        MyFootballBettingGame football = new MyFootballBettingGame(matchdays);
        football.computeOptimalBets();
        CheckerFootballBettingGame.checkBettinggame(football);
    }

}
