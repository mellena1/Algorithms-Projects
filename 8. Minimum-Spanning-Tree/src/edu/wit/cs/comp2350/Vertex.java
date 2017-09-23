package edu.wit.cs.comp2350;

// represents a vertex in a graph, including a unique ID to keep track of vertex
public class Vertex {
	public double x;
	public double y;
	public int ID;
	public double distFromOrigin;
	
	public Vertex parent;
	public int treeSize; //Only for parent
}
