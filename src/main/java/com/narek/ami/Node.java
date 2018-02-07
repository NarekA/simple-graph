package com.narek.ami;

public class Node {
    Long id;
    private String name;

    public Node(Long nodeID, String nodeName) {
        id = nodeID;
        name = nodeName;
    }

    public String toString() {
        return "< Node id: " + Long.toString(id) + " Node named: " + name + " >";
    }
}
