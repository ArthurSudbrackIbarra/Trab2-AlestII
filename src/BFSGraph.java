// Java program to find minimum edge
// between given two vertex of Graph

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

class BFSGraph {

    private final Vector<Integer>[] edges;
    private final Vector<Boolean> visited;
    private final Vector<Integer> distance;

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

    // Method for finding minimum no. of edge using BFS.
    public int minEdgeBFS(int u, int v)
    {
        // queue to do BFS.
        Queue<Integer> Q = new LinkedList<>();
        distance.setElementAt(0, u);

        Q.add(u);
        visited.setElementAt(true, u);
        while (!Q.isEmpty())
        {
            int x = Q.peek();
            Q.poll();

            for (int i=0; i<edges[x].size(); i++)
            {
                if (visited.elementAt(edges[x].get(i)))
                    continue;

                // update distance for i
                distance.setElementAt(distance.get(x) + 1,edges[x].get(i));
                Q.add(edges[x].get(i));
                visited.setElementAt(true,edges[x].get(i));
            }
        }
        return distance.get(v);
    }

    // method for addition of edge
    public void addEdge(int u, int v)
    {
        this.edges[u].add(v);
        this.edges[v].add(u);
    }
}