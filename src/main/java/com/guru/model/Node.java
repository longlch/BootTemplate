package com.guru.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
	private List <Node> parents = new ArrayList <> ();
    private Map <String, Node> map = new HashMap <> ();
	private final String key;
	private int distance = -1;

	public Node get(String str) {
		// inner interning of nodes
		Node res = map.get(str);
		if (res == null) {
			res = new Node(str, -1);
			map.put(str, res);
		}
		return res;
	}

	public Node(String str, int distance) {
		key = str;
		this.distance = distance;
	}

	public void addParent(Node n) {
		if (n.distance == distance) {
			return;
		}
		parents.add(n);

		distance = n.distance + 1;
	}

	public List <Node> getParents() {
		return parents;
	}

	public boolean equals(Object n) {
		return getKey().equals(((Node) n).getKey());
	}

	public int hashCode() {
		return getKey().hashCode();
	}

	@Override
	public String toString() {
		return key+" "+distance;
	}

	public String getKey() {
		return key;
	}
}