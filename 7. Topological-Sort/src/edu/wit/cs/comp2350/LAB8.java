package edu.wit.cs.comp2350;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

/**
 * 
 * @author kreimendahl
 */

// Provides a solution to the topological sorting problem
public class LAB8 {
	
	// TODO: javadocs
	public static Node[] FindTopo(Graph g) {
		LinkedList<Node> returnNodes = new LinkedList<>();
		
		for(Node u : g.GetNodes()){
			u.marker = 0;
		}
		for(Node u : g.GetNodes()){
			if(u.marker == 0)
				DFSVisit(g, u, returnNodes);
		}
		return returnNodes.toArray(new Node[0]);
	}
	
	//TODO: javadocs
	private static void DFSVisit(Graph g, Node u, LinkedList<Node> nodes){
		u.marker = 1;
		for(int x = 0; x < u.GetEdges().length; x++){
			Node v = g.GetNodes()[u.GetEdges()[x]];
			if(v.marker == 0)
				DFSVisit(g, v, nodes);
		}
		u.marker = 2;
		nodes.addFirst(u);
	}
	
	/************************************************************
	 * A naive implementation of topological sort. This implementation
	 * uses the 'marker' field of a node to maintain a count of the number
	 * of unresolved dependencies. Then there is a double-for loop over
	 * all of the nodes, each time adding a node with 0 unresolved
	 * dependencies to the output list.
	 ************************************************************/
	public static Node[] FindNaive(Graph g) {
		
		int numNodes = g.size();
		int numFinished = 0;
		Node[] ret= new Node[numNodes];
		
		MarkDeps(g);
		while (numFinished < numNodes) {
			for (Node n : g.GetNodes()) {
				if (n.marker == 0) {
					UnmarkDeps(n, g);
					n.marker = -1;
					ret[numFinished] = n;
					numFinished++;
				}
			}
		}
		
		return ret;
	}
	
	// use each node's marker to count how many nodes depend on it
	private static void MarkDeps(Graph g) {
		
		for (Node n: g.GetNodes()) {
			for (Node next: g.GetEdges(n))
				next.marker++;
		}
	}
	
	// reduce dependency count for all dependencies of a specific node
	private static void UnmarkDeps(Node n, Graph g) {
		
		for (Node next: g.GetEdges(n))
			next.marker--;
	}
	
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1;
		System.out.printf("Enter <dependencies file> <algorithm>, ([n]aive, [t]opological sort).\n");
		System.out.printf("(e.g: deps/small n)\n");
		file1 = s.next();

		// read in dependencies
		Graph g = InputGraph(file1);

		String algo = s.next();
		Node[] result = {};

		switch (algo.charAt(0)) {
		case 'n':
			result = FindNaive(g);
			break;
		case 't':
			result = FindTopo(g);
			break;
		default:
			System.out.println("Invalid algorithm");
			System.exit(0);
			break;
		}

		s.close();

		System.out.printf("Order of files: ");
		for (int i = 0; i < result.length; i++)
			System.out.println(result[i].toString());
	}


	// reads in the graph from a specific file formatted with lines like:
	// FILENAME 1 2 10
	// This means that the file FILENAME depends on files on lines 1, 2,
	// and 10 of the file to be completed first.
	// This reads through the input file twice, first to get names for the
	// nodes and next to set up edges in the correct direction
	private static Graph InputGraph(String file1) {
		
		Graph g = new Graph();
		try (Scanner f = new Scanner(new File(file1))) {
			int i = 0;
			while(f.hasNextLine()) { // each file listing
				String line = f.nextLine();
				Scanner lineScan = new Scanner(line);
				Node n = new Node(i, lineScan.next());
				lineScan.close();
				g.AddNode(n);
				i++;
			}
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}
		
		try (Scanner f = new Scanner(new File(file1))) {
			int i = 0;
			while(f.hasNextLine()) { // each file listing
				String line = f.nextLine();
				Scanner lineScan = new Scanner(line);
				lineScan.next();	// skip over file name
				while (lineScan.hasNextInt())	// for each dependency
					g.GetNodes()[lineScan.nextInt()].AddEdge(i);
				lineScan.close();
				i++;
			}
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}
		
		return g;
	}

}
