//name - Gangezwer Uthayakumar
//UOW ID - w1761186
//Student ID - 2019100

import java.util.ArrayList;
import java.util.List;

abstract class Base {

    //Set the maximum value to find the fist bottle neck value.
    static final long INF = Long.MAX_VALUE;

    //total number of nodes, Source node, End node
    final int n;
    final int s;
    final int t;

    //Ensure the end of execution
    protected boolean solved;

    //Holds the max flow value
    protected long maxFlow;

    //Main data structures which holds the details about nodes and edges of the flow network graph.
    protected List<Edge>[] graph;

    //The count of paths that are found
    protected int paths;

    /**
     * Initializes variables and set up for the execution
     * @param n total number of nodes
     * @param s source node
     * @param t end node
     */
    public Base(int n, int s, int t){
        this.n =n;
        this.s=s;
        this.t = t;
        // The main data structure is initialized with empty edges.
        InitializeEmptyFlowGraph();
        // to count number of paths found
        paths = 0;
    }

    /**
     * Initialize a flow graph with the length of n, and assign empty ArrayList<Edge>.
     * ArrayList<Edge> will LATER hold the details of Edge starting from a particular Node.
     * ex : (from source) graph[0] - <edge1>,<edge2>,<edge3>
     *      (from node1)  graph[1] - <edge3>,<edge5>
     *      (from node2)  graph[2] - <edge4> ...
     */
    private void InitializeEmptyFlowGraph(){
        graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<Edge>();
        }
    }

    /**
     *
     * @param from node where the edge starts from
     * @param to node where the edge ends
     * @param capacity
     */
    public void addEdge(int from,int to,long capacity){
        if(capacity<=0)
            throw new IllegalArgumentException("Forward edge capacity <=0");

        if(from == to){
            throw new IllegalArgumentException("Starting node & ending node are same!");
        }

        //In case where capacity is mentioned from both direction of the same edge./In this case,
        // residual edge will not get created again.
        // ex : node1 -> node2  capacity = 7
        //      node2 -> node1  capacity = 4
        for(Edge edge : graph[from]){
            if(edge.from == from && edge.to == to){
                edge.capacity+=capacity;
                return;
            }
        }

        //Create new edge with start node, end node and capacity
        Edge e1 = new Edge(from, to, capacity);

        //Here creating a residual edge with capacity of 0
        Edge e2 = new Edge(to, from, 0);

        //assign relevant edges as 'residual' edge.
        e1.residual = e2;
        e2.residual = e1;

        //Adding the edges in the relevant position in the graph.
        graph[from].add(e1);
        graph[to].add(e2);
    }

    /**
     * to get the whole network flow graph with details like node, capacity, flow details etc... )
     * @return the graph ArrayList.
     * graph will be returned after the Execution of finding the max flow value for the flow network.
     */
    public List[] getGraph(){
        execute();
        return graph;
    }

    /**
     * Max flow will be summed up using the solve() method which will be called inside the execute() method.
     * @return maximum flow
     */
    public long getMaxFlow(){
        execute();
        return maxFlow;
    }

    /**
     * checks if the execution is completed, if NOT completed, it will call the solve() method.
     * solved = true  -> Execution is completed
     * solved = false -> not completed yet
     */
    private void execute(){
        if(solved) return;
        solved=true;
        solve();
    }

    /**
     * @return number of paths that has been found in the graph
     */
    public int getPathsCount(){
        execute();
        return paths;
    }

    /**
     * This is the solution CODE for Max flow problem.
     * Declared abstract as there are many other solutions available for Maximum flow problem
     * In this coursework I have implemented Dinic's Algorithm.
     */
    public abstract void solve();
}

//References used for this Course Work
// Source :https://github.com/williamfiset/Algorithms/tree/master/src/main/java/com/williamfiset/algorithms/graphtheory/networkflow/examples

