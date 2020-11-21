import java.util.Hashtable;
import java.util.Set;

public class MyDisjointSet {
    private Hashtable<Integer, Integer> vertexComponentAssociation;
    private Set<Integer> vertices;

    public MyDisjointSet(Set<Integer> vertexSet) {
        vertexComponentAssociation = new Hashtable<>();
        vertices = vertexSet;
        for (Integer vertex: vertexSet) {
            vertexComponentAssociation.put(vertex, vertex);
        }
    }

    public Integer SEARCH(Integer vertex){
        return vertexComponentAssociation.get(vertex);
    }

    public void MERGE(Integer componentAId, Integer componentBId){
        for (Integer vertex: vertices) {
            if (vertexComponentAssociation.get(vertex).equals(componentBId)){
                vertexComponentAssociation.put(vertex, componentAId);
            }
        }
    }


}
