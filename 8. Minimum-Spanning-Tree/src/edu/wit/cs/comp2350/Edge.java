package edu.wit.cs.comp2350;

public class Edge implements Comparable<Edge>{
	public Vertex src;
	public Vertex dst;
	public double cost;
	
	// creates an edge between two vertices
	Edge(Vertex s, Vertex d) {
		src = s;
		dst = d;
		cost = Math.sqrt(Math.pow(s.x-d.x, 2) + Math.pow(s.y-d.y, 2));
	}

	@Override
	public int compareTo(Edge o) {
		if (cost > o.cost)
			return 1;
		else if(o.cost > cost)
			return -1;
		else
			return 0;
	}

}
