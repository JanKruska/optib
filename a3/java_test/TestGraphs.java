import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.generate.GnmRandomBipartiteGraphGenerator;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.util.SupplierUtil;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGraphs {

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



}
