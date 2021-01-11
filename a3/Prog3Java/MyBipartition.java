

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

/**
 * Implementation of a bipartition detection algorithm.
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
public class MyBipartition <V,E> {

    private final Graph<V, E> graph;
    private boolean isBipartite;
    private List<HashSet<V>> bipartitioning;
    private List<V> oddCycle;

    private  HashMap<V, Integer> colorClass;

    public MyBipartition(Graph<V, E> graph) {
        this.graph = graph;
    }
    
    //Decides whether the graph is bipartite or not.
    //Either computes a bipartitioning of the vertex set or finds a cycle of odd length if the graph is not bipartite.
    public void computeBipartitioning() {
        //Depth first search (dfs) approach

        colorClass = new HashMap<>();
        isBipartite = true;

        dfsColorClassesScan();

        // build bipartitioning if isBipartite
        if (isBipartite) {
            bipartitioning = new ArrayList<>();
            bipartitioning.add(new HashSet<>());
            bipartitioning.add(new HashSet<>());

            for (V v : colorClass.keySet()) {
                bipartitioning.get(colorClass.get(v)).add(v);
            }
        }

    }

	private void dfsColorClassesScan(){
        HashSet<V> unvisited = new HashSet<>(graph.vertexSet());
        while (!unvisited.isEmpty()) {

            Stack<V> frames = new Stack<>();
            HashMap<V,V> predecessor = new HashMap<>();
            V r = unvisited.iterator().next();

            frames.push(r);
            colorClass.put(r, 0);
            predecessor.put(r, null);

            // dfs
            while (!frames.isEmpty()){
                V u = frames.pop();
                if (unvisited.contains(u)){
                    unvisited.remove(u);
                    for (V w : Graphs.neighborListOf(graph, u)) {
                        frames.push(w);

                        // Colorings
                        if (!predecessor.containsKey(w)){
                            // if there is no current predecessor color w opposite to u
                            //colors are 0, 1 so (x+1)%2 is opposite color
                            colorClass.put(w, (colorClass.get(u) + 1)%2);

                            // set u as predecessor of w
                            predecessor.put(w, u);
                        } else if (colorClass.get(u).equals(colorClass.get(w))){
                            // if u and current w have same colors, odd cycle detected.
                            isBipartite = false;

                            // build and set odd cycle
                            setOddCycle(w, u, predecessor);

                            // Exit dfs
                            return;
                        }
                    }
                }
            }
        }
    }

    private void setOddCycle(V w, V u, HashMap<V,V> predecessor) {
        List<V> wRootPath = getPathToRoot(w, predecessor);
        List<V> uRootPath = getPathToRoot(u, predecessor);
        uRootPath.add(w);

        cutCommonStart(wRootPath, uRootPath);
        Collections.reverse(uRootPath);
        oddCycle = uRootPath;
        oddCycle.addAll(wRootPath);
        //System.out.println("oddCycle found of vertices:");
        //System.out.println(oddCycle);
    }

    private void cutCommonStart(List<V> wRootPath, List<V> uRootPath) {
        V lastpoped = null;
        while ((wRootPath.size() > 0 && uRootPath.size() > 0) && wRootPath.get(0) == uRootPath.get(0)){
            lastpoped = wRootPath.get(0);
            wRootPath.remove(0);
            uRootPath.remove(0);
        }
        if (lastpoped != null){
            wRootPath.add(0,lastpoped);
        }

    }

    private List<V> getPathToRoot(V start, Map<V,V> predecessor){
        ArrayList<V> path = new ArrayList<>();
        V s = start;
        path.add(s);
        while (predecessor.get(s) != null){
            s = predecessor.get(s);
            path.add(s);
        }
        Collections.reverse(path);
        return path;
    }
    
    public Graph<V, E> getGraph() {
		return graph;
	}

    public boolean isBipartite() {
		return isBipartite;
	}

	public List<HashSet<V>> getPartitioning() {
    	return bipartitioning;
    }

    public List<V> getOddCycle() {
        return oddCycle;
    }
}
