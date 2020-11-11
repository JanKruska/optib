import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGraphs {

    String dir = "a1/cxx/";
    String img_dir = "out/res";

    public void saveImage(Graph<Integer, DefaultWeightedEdge> graph, String directory) throws IOException {
        JGraphXAdapter<Integer, DefaultWeightedEdge> graphAdapter =
                new JGraphXAdapter<>(graph);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File(directory);
        ImageIO.write(image, "PNG", imgFile);

        assertTrue(imgFile.exists());
    }

    @RepeatedTest(3)
    void mst(RepetitionInfo repetitionInfo) {
        String graphNumber = Integer.toString(repetitionInfo.getCurrentRepetition());
        System.out.println("Computing MST of Graph "+graphNumber+":");
        Graph<Integer, DefaultWeightedEdge> graph = GraphReader.readGraph(dir+"Graph"+graphNumber+".lgf");
        AsSubgraph<Integer, DefaultWeightedEdge> tree = mySpanningTree.computeMST(graph);
        CheckerSpanningTree.checkSpanningTree(graph, tree);
        try {
            saveImage(tree,img_dir + "/graph_mst_"+graphNumber+".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
