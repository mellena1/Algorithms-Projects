package edu.wit.cs.comp2350;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


/**
 * 
 * @author kreimendahl
 */

// Provides a solution to the minimal spanning tree problem
public class LAB9 {
	
	/**Finds the MST using Kruskal's Algorithm
	 * @param g graph*/
	public static void FindMST(Graph g) {
		Vertex[] vertexs = g.getVertices();
		MinHeap possibleEdges = new MinHeap(vertexs.length);
		
		for(int x = 0; x < vertexs.length; x++){ 
			Vertex u = vertexs[x];
			UnionSets.makeSet(u);
			
			for(int y = x + 1; y < vertexs.length; y++) 
				possibleEdges.insert(new Edge(u, vertexs[y]));
		}
		while(!possibleEdges.isEmpty()){
			Edge e = possibleEdges.getMin();
			Vertex parentU = UnionSets.findSet(e.src);
			Vertex parentV = UnionSets.findSet(e.dst);
			if(parentU != parentV){
				g.addEdge(e.src, e.dst);
				if(UnionSets.Union(parentU, parentV).treeSize == vertexs.length)
					break;
			}
		}
	}
	
	private static class MinHeap{
		private Edge[] a;
		private int lastIndex;
		
		private MinHeap(int numOfVertexs){
			a = new Edge[numOfVertexs * (numOfVertexs-1)];
			lastIndex = -1;
		}
		
		/**Returns min value from heap and re-establishes the invariant
		 * @param a  the heap array
		 * @param lastIndex  last index of the heap in the array
		 * @return the min*/
		private Edge getMin(){
			Edge min = a[0];
			a[0] = a[lastIndex];
			pushDown(0);
			lastIndex--;
			return min;
		}
		
		/**Inserts new number into the heap
		 * @param num  the number to be inserted
		 * @param a  the heap array
		 * @param lastIndex  the lastIndex in the heap*/
		private void insert(Edge e){
			lastIndex++;
			a[lastIndex] = e;
			pullUp(lastIndex);
		}
		
		/**Pushes down element at i until its children are bigger than it
		 * @param i  the index of the element to be pushed down
		 * @param a  the heap array
		 * @param lastIndex  the lastIndex in the heap*/
		private void pushDown(int i){
			int minI = findMinI(i);
			if(minI != i){
				swap(minI, i);
				pushDown(minI);
			}
		}
		
		/**@param i  a parent in the heap
		 * @param a  the heap array
		 * @param lastIndex  the lastIndex in the heap
		 * @return the min value between i and its 2 children in a heap*/
		private int findMinI(int i){
			int minI = i; //Start by assuming min val is the parent
			
			//Checks both right and left children to find min
			//Short circuit both if statements if no element is there (has no child there)
			if((2*i) + 1 <= lastIndex && a[minI].cost > a[(2*i) + 1].cost)
				minI = (2*i) + 1;
			if((2*i) + 2 <= lastIndex && a[minI].cost > a[(2*i) + 2].cost)
				minI = (2*i)+2;
			
			return minI;
		}
		
		/**Pulls an element up until its parent is smaller than it
		 * @param i  the index of the element to be pulled up
		 * @param a  the heap array*/
		private void pullUp(int i){
			int parentIndex = (i-1)/2;
			//Short circuit if checking an index < 0
			if(parentIndex >= 0 && a[i].cost < a[parentIndex].cost){
				swap(i, parentIndex);
				pullUp(parentIndex);
			}
		}
		
		/**Swap the values in array a from pos1 and pos2
		 * @param pos1  the index of the first element
		 * @param pos2  the index of the second element
		 * @param a  the array*/
		private void swap(int pos1, int pos2){
			Edge temp = a[pos1];
			a[pos1] = a[pos2];
			a[pos2] = temp;
		}
		
		/** @return true if heap is empty*/
		private boolean isEmpty(){
			return lastIndex == -1;
		}
		
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1;
		
		System.out.printf("Enter <points file> <edge neighborhood>\n");
		System.out.printf("(e.g: points/small .5)\n");
		file1 = s.next();

		// read in vertices
		Graph g = InputGraph(file1);
		g.epsilon = s.nextDouble();
		
		FindMST(g);

		s.close();

		System.out.printf("Weight of tree: %f\n", g.getTotalEdgeWeight());
	}

	// reads in an undirected graph from a specific file formatted with one
	// x/y node coordinate per line:
	private static Graph InputGraph(String file1) {
		
		Graph g = new Graph();
		try (Scanner f = new Scanner(new File(file1))) {
			while(f.hasNextDouble()) // each vertex listing
				g.addVertex(f.nextDouble(), f.nextDouble());
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}
		
		return g;
	}

}
