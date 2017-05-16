package com.guru.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
    private Map <String, Set <Node>> adjacencyList = new HashMap <> ();

    public void add(String from, String to, int distance) {
        addEdge(from, to);
    }

    private void addEdge(String from, String to) {
        Set <Node> list = adjacencyList.get(from);

        if (list == null) {
            list = new LinkedHashSet <> ();
            adjacencyList.put(from, list);
        }
        Node e = new Node(from, -1);
        list.add(e.get(to));
    }

    public Set <Node> adjacents(String v) {
        Set <Node> nodes = adjacencyList.get(v);
        return nodes == null ? Collections.emptySet() : nodes;
    }
}
