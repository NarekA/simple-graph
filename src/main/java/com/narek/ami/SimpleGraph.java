package com.narek.ami;


import java.util.*;

public class SimpleGraph {
    private String name;
    private HashMap<Long, Node> nodes = new HashMap<Long, Node>();
    private HashMap<Long, HashSet<Long>> edges = new HashMap<Long, HashSet<Long>>();
    private HashMap<Long, HashSet<Long>> edgesReverse = new HashMap<Long, HashSet<Long>>();

    public SimpleGraph(String graphName) {
        name = graphName;
    }

    private void addNode(Node node) {
        nodes.put(node.id, node);
        System.out.println(node);
        System.out.println("Graph size: " + nodes.size());
        System.out.println("Num edges: " + edges.size());
    }

    private void addNode(Long id, String name) {
        addNode(new Node(id, name));
    }

    private void addEdge(Long from, Long to) {
        // Might require some locks eventually
        if (!nodes.containsKey(to))
            addNode(to, null);
        if (!nodes.containsKey(from))
            addNode(from, null);

        HashSet<Long> connections = edges.containsKey(from) ? edges.get(from) : new HashSet<Long>();
        connections.add(to);
        edges.put(from, connections);

        HashSet<Long> reverseConnections = edgesReverse.containsKey(from) ? edgesReverse.get(from) : new HashSet<Long>();
        reverseConnections.add(to);
        edgesReverse.put(from, reverseConnections);
    }

    private void processStdin(String line) {
        String[] input = line.split("\\s+");
        if (input[0].equals("node")) {
            addNode(Long.parseLong(input[1]), input[2]);
        }
        else if (input[0].equals("edge")) {
            addEdge(Long.parseLong(input[1]), Long.parseLong(input[2]));
        }
    }

    public static void main( String[] args ) {
        System.out.println( "DB started..." );
        SimpleGraph graph = new SimpleGraph("graph");
        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()){
            graph.processStdin(sc.nextLine());
        }
    }
}
