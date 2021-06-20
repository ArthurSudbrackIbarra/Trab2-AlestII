import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

class BFSGraph {

    // Attributes.
    private final Vector<Integer>[] edges;
    private final Vector<Boolean> visited;
    private final Vector<Integer> cameFrom;

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
        this.cameFrom = new Vector<Integer>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            cameFrom.addElement(-1);
        }
    }

    // Method for addition of edge.
    public void addEdge(int u, int v)
    {
        this.edges[u].add(v);
        this.edges[v].add(u);
    }

    // Method for finding minimum number of edge using BFS.
    public LinkedList<Integer> minEdgeBFS(int u, int v)
    {
        // Queue to do BFS.
        Queue<Integer> queue = new LinkedList<>();
        cameFrom.setElementAt(u, u);

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

                cameFrom.setElementAt(x, edges[x].get(i));

                queue.add(edges[x].get(i));
                visited.setElementAt(true, edges[x].get(i));
            }
        }

        LinkedList<Integer> path = new LinkedList<>();
        int currentVertex = this.cameFrom.get(v);
        if(currentVertex == -1){
            return path;
        }
        path.add(0, currentVertex);
        while(currentVertex != u){
            currentVertex = this.cameFrom.get(currentVertex);
            path.add(0, currentVertex);
        }
        path.add(0, currentVertex);
        return path;
    }
}