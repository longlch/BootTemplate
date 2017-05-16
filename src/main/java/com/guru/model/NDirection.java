package com.guru.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class NDirection {
    private static Object mock = new Object();

    public List <List <String>> findAllShortestPaths(Graph graph, String from, String to) {
        LinkedHashMap <Node, Object> queue = new LinkedHashMap <> (); queue.clear();
        Set <Node> visited = new HashSet <> (); visited.clear();
        queue.put(new Node(from, 0), mock);

        Node nodeTo = null;
        while (queue.keySet().size() > 0) {
            Node next = queue.keySet().iterator().next();

            if (next.getKey().equals(to)) {
                nodeTo = next;
                break;
            }

            for (Node n: graph.adjacents(next.getKey())) {
                if (!visited.contains(n)) {
                    if (queue.get(n) == null) {
                        queue.put(n, mock);
                    }
                    n.addParent(next);
                }
            }
            queue.remove(next);
            visited.add(next);
        }
        if (nodeTo == null) {
            return Collections.emptyList();
        }
        List <List <String>> result = new ArrayList <> ();
        List<String> _list = new ArrayList<>();
        dfs(nodeTo, result, _list);

        return result;
    }

    private void dfs(Node n, List <List <String>> result, List <String> path) {
        path.add(0, n.getKey());
        if (n.getParents().size() == 0) {
            List<String> newArr = new ArrayList<>();
            newArr.addAll(path);
            result.add(newArr);
        }
        for (Node p: n.getParents()) {
            dfs(p, result, path);
        }
        path.remove(0);
    }
}