import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

class BFSGraph {

    private final Vector<Integer>[] edges;
    private final Vector<Boolean> visited;
    private final Vector<Integer> distance;

    // Constructor.
    public BFSGraph(int vertexCount){
        this.edges = new Vector[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            this.edges[i] = new Vector<>();
        }
        this.visited = new Vector<Boolean>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            visited.addElement(false);
        }
        this.distance = new Vector<Integer>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            distance.addElement(0);
        }
    }

    // Method for addition of edge.
    public void addEdge(int u, int v)
    {
        this.edges[u].add(v);
        this.edges[v].add(u);
    }

    // Method for finding minimum no. of edge using BFS.
    public int minEdgeBFS(int u, int v)
    {
        // Queue to do BFS.
        Queue<Integer> queue = new LinkedList<>();
        distance.setElementAt(0, u);

        queue.add(u);
        visited.setElementAt(true, u);
        while (!queue.isEmpty())
        {
            int x = queue.peek();
            queue.poll();

            for (int i=0; i<edges[x].size(); i++)
            {
                if (visited.elementAt(edges[x].get(i)))
                    continue;

                // Update distance for i.
                distance.setElementAt(distance.get(x) + 1,edges[x].get(i));
                queue.add(edges[x].get(i));
                visited.setElementAt(true,edges[x].get(i));
            }
        }
        return distance.get(v);
    }
}