import org.jgrapht.Graph;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class TestGraphs {

    String dir = "a1/cxx/";

    @RepeatedTest(3)
    void mst(RepetitionInfo repetitionInfo) {
        String graphNumber = Integer.toString(repetitionInfo.getCurrentRepetition());
        System.out.println("Computing MST of Graph "+graphNumber+":");
        Graph<Integer, DefaultWeightedEdge> graph = GraphReader.readGraph(dir+"Graph"+graphNumber+".lgf");
        AsSubgraph<Integer, DefaultWeightedEdge> tree = mySpanningTree.computeMST(graph);
        CheckerSpanningTree.checkSpanningTree(graph, tree);
    }

    @RepeatedTest(3)
    void shortestPath(RepetitionInfo repetitionInfo) {
        String graphNumber = Integer.toString(repetitionInfo.getCurrentRepetition());
        System.out.println("Computing ShortestPath of Graph "+graphNumber+":");
        Graph<Integer, DefaultWeightedEdge> graph = GraphReader.readGraph(dir+"Graph"+graphNumber+".lgf");
        ArrayList<Integer> vertexList = new ArrayList<>(graph.vertexSet());
        Random randomGenerator = new Random();
        Integer startVertex = vertexList.get(randomGenerator.nextInt(vertexList.size()));
        Integer endVertex = vertexList.get(randomGenerator.nextInt(vertexList.size()));
        myShortestPath shortestPath = new myShortestPath(graph, startVertex);
        shortestPath.computeDistPred();
        ArrayList<Integer> path = shortestPath.constructPathToNode(endVertex);
        CheckerPath.checkPath(graph, startVertex, endVertex, path);
    }

    @RepeatedTest(3)
    void steinerTree(RepetitionInfo repetitionInfo) {
        String graphNumber = Integer.toString(repetitionInfo.getCurrentRepetition());
        String terminalNumber = "";
        String output = "Computing SteinerTree of Graph "+graphNumber;
        output += ":";
        System.out.println(output);
        Graph<Integer, DefaultWeightedEdge> graph = GraphReader.readGraph(dir+"Graph"+graphNumber+".lgf");
        HashSet<Integer> terminals = GraphReader.readTerminals(dir+"Graph"+graphNumber+"_Terminals"+terminalNumber+".txt");
        AsSubgraph<Integer, DefaultWeightedEdge> steinerTree = mySteinerTree.computeSteinerTree(graph, terminals);
        CheckerSteinerTree.checkSteinerTree(steinerTree, terminals);
    }


}
