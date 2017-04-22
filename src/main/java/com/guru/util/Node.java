package com.guru.util;

import java.util.ArrayList;

import com.guru.model.BusRoute;
import com.guru.model.RouteElement;

public class Node {
	private RouteElement parent;
	private ArrayList<Node> children;
	
	public Node() {
		parent = new RouteElement();
		children = new ArrayList<>();
	}
	public Node(RouteElement e) {
		parent = e;
		children = new ArrayList<>();
	}
	
	public void addChildrent(Node child) {
		children.add(child);
	}
	public void addChildrent(ArrayList<Node> child) {
		children.addAll(child);
	}

	public RouteElement getParent() {
		return parent;
	}
	
	public ArrayList<Node> getChildren() {
		return children;
	}
	@Override
	public String toString() {
		return "Node: parent=" + parent + ", children=" + children + "";
	}


	public void setNode(Node node, ArrayList<RouteElement> list) {
		ArrayList<RouteElement> near = new ArrayList<>();
		near = getNear(node.getParent(), list);
		ArrayList<Node> child = new ArrayList<>();
		if(near.size() > 0) {
			for(RouteElement e : near) {
				Node n = new Node(e);
				setNode(n, list);
				child.add(n);
			}
			node.addChildrent(child);
		}
	}
	public void travelNode(Node node, ArrayList<ArrayList<RouteElement>> out, ArrayList<RouteElement> list) {
		if(node.getParent().getStationToId() == 9999) {
			list.add(node.getParent());
//			System.out.println(list.toString());
			ArrayList<RouteElement> newArr = new ArrayList<>();
			newArr.addAll(list);
			out.add(newArr);
			list.remove(node.getParent());
		} else {
			list.add(node.getParent());
			for(Node n : node.getChildren()) {
				travelNode(n, out, list);
			}
			list.remove(node.getParent());
		}
	}
	public ArrayList<RouteElement> getRouteWithNumberRoute(int number, ArrayList<RouteElement> direction) {
		Node node = new Node(direction.get(0));
//		System.out.println(node.toString());
		setNode(node, direction);
//		System.out.println(node.toString());
		ArrayList<ArrayList<RouteElement>> _out = new ArrayList<>();
		ArrayList<RouteElement> _list = new ArrayList<>();
		travelNode(node, _out, _list);
//		System.out.println(_out.toString());
		ArrayList<ArrayList<RouteElement>> out = new ArrayList<>();
//      for one result only
        int minWalking = 0, id = -1;
		for(ArrayList<RouteElement> arr : _out) {
			int k = 0;
			int count = 0;
			BusRoute b = null;
			int walk = 0;
			for(int i=0, j = arr.size(); i < j; i++) {
				walk+=arr.get(i).getDistanceWalking();
				if(b == null && arr.get(i).getBusRoute() != null) {
					b = arr.get(i).getBusRoute();
					count++;
//					System.out.println(i+ "count "+count);
				}
				else if(b != null && arr.get(i).getBusRoute() != null) {
					if(arr.get(i).getBusRoute().getId().equals(b.getId())) {
						if(arr.get(i).getBusRoute().isTurn() || b.isTurn()) {
							b = arr.get(i).getBusRoute();
							count++;
//							System.out.println(i+ "count "+count);
						}
					} else {
						b = arr.get(i).getBusRoute();
						count++;
//						System.out.println(i+ "count "+count);
					}
				}
			}
			if(minWalking <= walk && count <= number) {
				minWalking = walk;
				id = k;
			}
			k++;
		}
		if(id > -1) out.add(_out.get(id));
		if(out.size() == 0) return null;
		return out.get(0);
	}
	private ArrayList<RouteElement> getNear(RouteElement target, ArrayList<RouteElement> list) {
		ArrayList<RouteElement> out = new ArrayList<RouteElement>();
		for(RouteElement e : list)
			if(e.getStationFromId()!=target.getStationFromId() && e.getStationFromId()==target.getStationToId())
				out.add(e);
		return out;
	}
}