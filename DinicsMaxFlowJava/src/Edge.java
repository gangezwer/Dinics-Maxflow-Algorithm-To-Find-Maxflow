//name - Gangezwer Uthayakumar
//UOW ID - w1761186
//Student ID - 2019100

public class Edge {

    //staring and ending nodes of the graph
    public int from,to;

    //Residual edge will be assigned, edge from end node to start node
    public Edge residual;

    //The current flow of an edge in the flow network graph.Initial value flow will be 0
    public long flow;

    //The capacity of an edge.This will be assigned when creating an edge or when creating a residual edge for a flow network graph
    public long capacity;

    /**
     * Constructor will initialize the values.
     * @param from start node
     * @param to end node
     * @param capacity the capacity of that EDGE.
     */
    public Edge(int from,int to,long capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
    }

    /**
     * This will calculate the available space of an edge by subtracting  total capacity from the current flow of an edge
     * @return remaining capacity.
     */
    public long remainingCapacity(){
        return capacity-flow;
    }

    /**
     * This method will substract the flow and increase the current flow of the residual Edge
     * @param bottleNeck flow which needs to be added / substracted
     */
    public void augment(long bottleNeck){
        flow += bottleNeck;
        residual.flow -= bottleNeck;
    }

    //Print the entire graph details
    public String toString(int s, int t) {
        String u =(from ==s)?"s":((from==t)?"t":String.valueOf(from));
        String v =(to ==s)?"s":((to==t)?"t":String.valueOf(to));
        return String.format("Edge %s -> %s | flow = %3d | capacity = %3d",
                u,v,flow,capacity);
    }
}
//References used for this Course Work
// Source :https://github.com/williamfiset/Algorithms/tree/master/src/main/java/com/williamfiset/algorithms/graphtheory/networkflow/examples
