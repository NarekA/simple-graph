package com.narek.ami;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleGraph {
    private Properties properties;
    private String persisterFile;
    private BufferedWriter dataPersister;
    private HashMap<Long, Node> nodes = new HashMap<Long, Node>();
    private HashMap<Long, HashSet<Long>> edges = new HashMap<Long, HashSet<Long>>();
    private HashMap<Long, HashSet<Long>> edgesReverse = new HashMap<Long, HashSet<Long>>();

    public SimpleGraph(Properties graphProperties) throws IOException {
        properties = graphProperties;
        persisterFile = Paths.get(properties.getProperty("dataDir"), "datalogs.log").toString();
        dataPersister = new BufferedWriter(new FileWriter(persisterFile,true));
        loadGraph();
    }

    private void loadGraph() throws IOException {
        Scanner in = new Scanner(new FileReader(persisterFile));
        while(in.hasNext()) {
            String line = in.nextLine();
            System.out.println(line + "---");
            processInputString(line, false);
        }
        in.close();
    }

    private void close() throws IOException {
        dataPersister.close();
    }

    private void addNode(Node node, Boolean persist) throws IOException {
        if (persist)
            persistOperation(new Object[]{"n", node.id, node.name});
        nodes.put(node.id, node);
        System.out.println(node);
        System.out.println("Graph size: " + nodes.size());
        System.out.println("Num edges: " + edges.size());
    }

    private void addNode(Long id, String name, Boolean persist) throws IOException {
        addNode(new Node(id, name), persist);
    }

    private void addEdge(Long from, Long to, Boolean persist) throws IOException {
        // Might require some locks eventually
        if (!nodes.containsKey(to))
            addNode(to,null, persist);
        if (!nodes.containsKey(from))
            addNode(from,null, persist);

        if (persist)
            persistOperation(new Object[]{"e", to, from});
        HashSet<Long> connections = edges.containsKey(from) ? edges.get(from) : new HashSet<Long>();
        connections.add(to);
        edges.put(from, connections);

        HashSet<Long> reverseConnections = edgesReverse.containsKey(from) ? edgesReverse.get(from) : new HashSet<Long>();
        reverseConnections.add(to);
        edgesReverse.put(from, reverseConnections);
    }

    private void processInputString(String line, Boolean persist) throws IOException {
        String[] input = line.split("\\s+");
        if (input[0].equals("n")) {
            addNode(Long.parseLong(input[1]), input[2], persist);
        }
        else if (input[0].equals("e")) {
            addEdge(Long.parseLong(input[1]), Long.parseLong(input[2]), persist);
        }
    }

    private void persistOperation(Object[] operation) throws IOException {
        String line = Arrays.stream(operation)
                .map(i -> i.toString())
                .collect(Collectors.joining(" "));
        dataPersister.write(line + "\n");
        dataPersister.flush();
    }

    public static void main( String[] args ) throws IOException {
        System.out.println( "DB started..." );
        SimpleGraph graph = new SimpleGraph(App.getProperties());
        try {
            Scanner sc = new Scanner(System.in);
            while(sc.hasNextLine()){
                graph.processInputString(sc.nextLine(),true);
            }
        } catch (Exception e) {
            graph.close();
        }
    }
}
