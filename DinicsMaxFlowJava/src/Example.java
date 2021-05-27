//name - Gangezwer Uthayakumar
//UOW ID - w1761186
//Student ID - 2019100

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Example {
    static int count = 0;     //To keep track of no of Edited files
    static String fileName; //File name which is currently on process

    //The instance for the base class which contains the code to set up the flow network graph and the functions needed for it
    static Base solver;

    static int n; //number of nodes
    static int s = 0;//starting node
    static int t;//ending node


    public static void main(String[] args) {
        Example ex = new Example();
        ex.runCode();
    }

    //This method is is used to read the input file to perform the maxflow calculation for a flow network graph
    public void runCode() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n++++++++++MAXIMUM FLOW SOLUTION USING DINIC'S ALGORITHM++++++++++");
        System.out.print("\nPLEASE ENTER THE NAME OF THE fILE: ");
        fileName = sc.next();

        //To check the if the user has entered the file extension with filename, if not ".txt" will get added automatically
        int len = fileName.length();
        if (len < 4 || (fileName.charAt(len - 4) != '.'
                || fileName.charAt(len - 3) != 't'
                || fileName.charAt(len - 2) != 'x'
                || fileName.charAt(len - 1) != 't'))
            fileName = fileName + ".txt";

        //Reads the flow network graph details from the input file and save it in the 'solver'(instance of the base class)
        readFromFile();

        //The menu gets executed here
        while (true) {
            System.out.println("\n++++++++++WELCOME TO THE MAIN MENU OF MAXIMUM FLOW SOLUTION USING DINIC'S ALGORITHM++++++++++");
            System.out.print("\nEnter \"A\" Calculate Maximum flow"
                    + "\nEnter \"B\" Add a new Edge"
                    + "\nEnter \"C\" Delete an existing edge"
                    + "\nEnter \"D\" Edit capacity of an existing edge"
                    + "\nEnter \"E\" Display available edges "
                    + "\nEnter \"F\" Open a different file"
                    + "\nEnter \"Q\" Exit"
                    + "\n\nEnter Your Choice: ");
            String userInput = sc.next().toLowerCase();
            switch (userInput) {

                //When user enters option "A" it'll calculate the maxflow for the inserted flow network graph
                case "a":


                    //calculating the maxflow and displaying it
                    System.out.format(ConsoleColors.PURPLE_BOLD + "%19s" + solver.getMaxFlow(), "\n     Maximum Flow is: ");
                    System.out.println();

                    //Find the no.of paths found
                    int paths = solver.getPathsCount();

                    //Displaying the no.of paths found from the above  arraylist named "paths"
                    System.out.format("%19s", "\n     Number of Paths: ");
                    System.out.println(paths + ConsoleColors.RESET);

                    //Getting the graph after the execution to display the updates and furthur details.
                    List<Edge>[] resultGraph = solver.getGraph();

                    System.out.println(ConsoleColors.PURPLE_BOLD+"\n           RESULT GRAPH");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++");

                    for (List<Edge> edges : resultGraph) {

                        for (Edge e : edges)
                            System.out.println(ConsoleColors.PURPLE + e.toString(s, t) + ConsoleColors.RESET);

                    }
                    break;

                //When user enters option "B" it'll add a new edge to the existing flow network graph
                case "b":

                    displayAvailableNodes();

                    //Enter the starting node for the new edge
                    System.out.println("Enter new starting node: ");
                    int user_sNode = validate(solver.s, solver.t - 1);

                    //Enter the ending node for the new edge
                    System.out.println("Enter new ending node: ");
                    int user_eNode = validate(solver.s, solver.t);

                    //Enter the capacity for the new edge
                    System.out.println("Enter new capacity: ");

                    //validating if it's a long number from the user
                    long user_cap = validateLong();

                    try {
                        solver.addEdge(user_sNode, user_eNode, user_cap);
                        System.out.println("Success! The new Edge has been added! ");
                        writeToFile();
                    } catch (Exception e) {
                        System.out.println("Starting node & Ending node are same!");
                    }
                    break;

                //When user enters option "B" it'll delete an edge from the existing flow network graph
                case "c":
                    displayAvailableNodes();

                    //Enter the starting node for the edge which needs to be deleted
                    System.out.println("start node: ");
                    int user_deleteStartNode = validate(solver.s, solver.t - 1);

                    //Displaying the available edges which are connected to the starting edge
                    List<Edge> availableEdges_toDelete = displayAvailableEdges(user_deleteStartNode);

                    //Enter the ending node for the edge which needs to be deleted
                    System.out.println("Enter ending node's value: ");

                    //when user enters the node, it will return the position of that ending node
                    int selectedEdgeID_toDelete = validate(availableEdges_toDelete);

                    deleteEdge(user_deleteStartNode, selectedEdgeID_toDelete);

                    System.out.println("Success! The selected Edge has been deleted ");
                    //After deleting the edge save the new network graph to a new file
                    writeToFile();
                    break;

                //Enter "D" to edit the capacity of an existing edge
                case "d":
                    displayAvailableNodes();

                    //enter the starting node
                    System.out.println("Enter the starting node: ");
                    int user_startNode = validate(solver.s, solver.t - 1);
                    List<Edge> availableEdges = displayAvailableEdges(user_startNode);

                    //Enter the ending node
                    System.out.println("Enter ending node: ");
                    int selectedEdgeID = validate(availableEdges);

                    //Enter the new capacity for the existing node
                    System.out.println("Enter new capacity: ");

                    long user_capacity = validateLong();

                    solver.graph[user_startNode].get(selectedEdgeID).capacity = user_capacity;

                    System.out.println("Success! The capacity is set to " + user_capacity);
                    writeToFile();
                    break;

                //Display all the available edges present in the flow network
                case "e":
                    displayAvailableNodes();
                    break;
                //Enter "F" to open the new file which contains the flow network
                case "f":
                    runCode();
                    break;

                //Enter "Q" to exit from the application
                case "q":   //Exit
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid Option Entered ..");
                    break;
            }
        }
    }

    //File handling is used to write the the flow network to the new file if the inserted flow network graph got
    // modified by add or deleting an edge or else change the capacity of the node
    //count is used to not to hang to the previous files
    public void writeToFile() {
        fileName = "edited_File" + (++count) + ".txt";

        try {
            FileWriter myWriter = new FileWriter(fileName);
            //Writing the new edited graph
            myWriter.write(solver.n + "\n");

            //Getting the number of positive edges, to avoid null pointer error when reading the file
            int total = 0;
            for (List<Edge> edges : solver.graph) {
                for (Edge e : edges) {
                    if (e.capacity != 0)
                        total++;
                }
            }

            //Writing data to the new file
            solver.graph[solver.graph.length - 1].size();
            int count = 0;
            for (List<Edge> edges : solver.graph) {
                for (Edge e : edges) {
                    if (e.capacity != 0) {
                        myWriter.write(e.from + " " + e.to + " " + e.capacity);
                        count++;

                        //This is checked to avoid Null pointer errors when reading the file
                        if (!(count == total))
                            myWriter.write("\n");
                    }

                }
            }

            myWriter.close();
            System.out.println("Updated graph is saved in a new File: " + fileName + "\n\n");
        } catch (IOException e) {
            System.out.println("An error occurred.\n\n");
            e.printStackTrace();
        }

        //Reads the edited file and replace the new graph
        readFromFile();
    }

    public void readFromFile() {
        //Getting the network graph's detail by reading the File.
        //First line of the FILE consists of number of nodes, source node and the ending node.
        //rest of the FILE consists the capacity of edges(From node x to node y)
        try {
            File obj = new File(fileName);
            Scanner get = new Scanner(obj);

            n = Integer.parseInt(get.next()); // number of nodes
           /* s = Integer.parseInt(get.next()); // source node
            t = Integer.parseInt(get.next()); // ending node*/

            t = n - 1;
            //Instance of DincsSolver class where the unique methods are defined(dfs, bfs etc..).
            solver = new DincsSolver(n, s, t);

            // in each line -> [from node]<SPACE>[to node]<SPACE>[capacity of that edge]
            while (get.hasNextLine()) {
                int startNode = Integer.parseInt(get.next());
                int endNode = Integer.parseInt(get.next());
                long capacity = Double.valueOf(get.next()).longValue();

                //'addEdge()' method takes from node,to node,capacity and assign it to the Data structure(ArrayList)
                solver.addEdge(startNode, endNode, capacity);
            }

        } catch (FileNotFoundException e) {
            System.out.println("FILE not found.. Please check the FILE name and try again..");
            runCode();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something wrong with the FILE... Please check the file and try again..");
            runCode();
        }
    }

    /**
     * To validate a number within a range
     *
     * @param start - starting value of the range
     * @param end   - ending value of the range
     * @return -returns the validated input.
     */
    public int validate(int start, int end) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the node's value (" + start + " - " + end + ")");
        int user_startNode = validateInteger();

        if (user_startNode >= start && user_startNode <= end) {
            return user_startNode;
        } else {
            System.out.println("The node is not available ! try again! ");
            return validate(start, end);
        }
    }

    /**
     * To validate if the chosed edge's to value is available in the graph
     *
     * @param availableEdges - edges which are vailable in the graph
     * @return - returns the valid edge's id chosen by the user
     */

    public int validate(List<Edge> availableEdges) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the ending node's value of the Edge");
        int user_endNode = validateInteger();


        for (int i = 0; i < availableEdges.size(); i++) {
            if (user_endNode == availableEdges.get(i).to)
                return i;
        }

        System.out.println("This node is not available in the flow network try again !!!! ");
        return validate(availableEdges);
    }

    /**
     * To validate a Long number (when getting the Capacity from the user)
     *
     * @return returns the validated Long value from the user
     */
    public long validateLong() {
        Scanner sc = new Scanner(System.in);

        try {
            long number = sc.nextInt();
            return number;
        } catch (Exception e) {
            System.out.println("+++++++++++Please enter a acceptable numeric value+++++++++++");
            return validateLong();
        }
    }

    /**
     * To validate an Integer(To avoid InputMismatch exception)
     *
     * @return - returns the validated integer
     */
    public int validateInteger() {
        Scanner sc = new Scanner(System.in);

        try {
            int num = sc.nextInt();
            return num;
        } catch (Exception e) {
            System.out.println("+++++++++++Please enter a numeric value+++++++++++");
            return validateInteger();
        }
    }

    /**
     * Display all the available nodes and edges in the graph
     */
    public void displayAvailableNodes() {
        System.out.println("+++++++++++Available edges of the flow network+++++++++++");
        for (List<Edge> edges : solver.graph) {
            for (Edge e : edges) {
                System.out.println(e.from + " - " + e.to + " capacity of the edge: " + e.capacity);
            }
        }
    }

    /**
     * To delete an edge from the graph. It will also delete the residual edge.
     *
     * @param startNode - the starting node's value (which is chosen by the user)
     * @param ID        - the ending node's value's position in the List.(returned from validate method)
     */
    public void deleteEdge(int startNode, int ID) {
        int endNode = solver.graph[startNode].get(ID).to;

        //Deleting the edge
        solver.graph[startNode].remove(ID);

        //Deleting the Residual edge
        for (int i = 0; i < solver.graph[endNode].size(); i++) {
            if (solver.graph[endNode].get(i).to == startNode) {
                solver.graph[endNode].remove(i);
                break;
            }
        }
    }

    /**
     * To display all the available edges starting from a particular node
     *
     * @param startNode - the starting node chosen by the user
     * @return - returns all the available edges found from startNode
     */
    public List<Edge> displayAvailableEdges(int startNode) {
        System.out.println("Available edges are: ");
        List<Edge> availableEdges = solver.graph[startNode];
        ArrayList<Integer> availableEnds = new ArrayList<>();
        for (Edge e : availableEdges) {
            System.out.println(e.from + " - " + e.to + " : capacity - " + e.capacity);
        }
        return availableEdges;
    }
}

//References used for this Course Work
// Source :https://github.com/williamfiset/Algorithms/tree/master/src/main/java/com/williamfiset/algorithms/graphtheory/networkflow/examples

