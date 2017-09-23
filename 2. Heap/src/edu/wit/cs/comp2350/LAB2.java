package edu.wit.cs.comp2350;

import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

/**Lab 2
 * @author kreimendahl
 * @author Andrew Mellen
*/
public class LAB2 {

	/**Adds an array of floats using a heap algorithm
	 * @param a  the input array of floats
	 * @return the sum of the floats*/
	public static float heapAdd(float[] a) {
		//Return 0 if a is an empty array
		if(a.length == 0)
			return 0;
			
		createHeap(a);
		int lastIndex = a.length - 1; //last index in heap
		
		//Run through the summation algorithm
		while(lastIndex > 0){
			float result = getMin(a, lastIndex) + getMin(a, lastIndex - 1);
			lastIndex -= 2; //lost two elements
			insert(result, a, lastIndex);
			lastIndex++; //added an element
		}
		
		return a[0];
	}
	
	/**Turns a into a heap using bottom up creation
	 * @param a  the input array to be turned into a heap*/
	private static void createHeap(float[] a){
		for(int x = ((a.length/2) - 1); x >= 0; x--){
			pushDown(x, a, a.length-1); //last index is a.length-1
		}
	}
	
	/**Returns min value from heap and re-establishes the invariant
	 * @param a  the heap array
	 * @param lastIndex  last index of the heap in the array
	 * @return the min*/
	private static float getMin(float[] a, int lastIndex){
		float min = a[0];
		a[0] = a[lastIndex];
		pushDown(0, a, lastIndex);
		return min;
	}
	
	/**Inserts new number into the heap
	 * @param num  the number to be inserted
	 * @param a  the heap array
	 * @param lastIndex  the lastIndex in the heap*/
	private static void insert(float num, float[] a, int lastIndex){
		a[lastIndex + 1] = num;
		pullUp(lastIndex + 1, a);
	}
	
	/**Pushes down element at i until its children are bigger than it
	 * @param i  the index of the element to be pushed down
	 * @param a  the heap array
	 * @param lastIndex  the lastIndex in the heap*/
	private static void pushDown(int i, float[] a, int lastIndex){
		int minI = findMinI(i, a, lastIndex);
		if(minI != i){
			swap(minI, i, a);
			pushDown(minI, a, lastIndex);
		}
	}
	
	/**@param i  a parent in the heap
	 * @param a  the heap array
	 * @param lastIndex  the lastIndex in the heap
	 * @return the min value between i and its 2 children in a heap*/
	private static int findMinI(int i, float[] a, int lastIndex){
		int minI = i; //Start by assuming min val is the parent
		
		//Checks both right and left children to find min
		//Short circuit both if statements if no element is there (has no child there)
		if((2*i) + 1 <= lastIndex && a[minI] > a[(2*i) + 1])
			minI = (2*i) + 1;
		if((2*i) + 2 <= lastIndex && a[minI] > a[(2*i) + 2])
			minI = (2*i)+2;
		
		return minI;
	}
	
	/**Pulls an element up until its parent is smaller than it
	 * @param i  the index of the element to be pulled up
	 * @param a  the heap array*/
	private static void pullUp(int i, float[] a){
		int parentIndex = (i-1)/2;
		//Short circuit if checking an index < 0
		if(parentIndex >= 0 && a[i] < a[parentIndex]){
			swap(i, parentIndex, a);
			pullUp(parentIndex, a);
		}
	}
	
	/**Swap the values in array a from pos1 and pos2
	 * @param pos1  the index of the first element
	 * @param pos2  the index of the second element
	 * @param a  the array*/
	private static void swap(int pos1, int pos2, float[] a){
		float temp = a[pos1];
		a[pos1] = a[pos2];
		a[pos2] = temp;
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	// sum an array of floats sequentially
	public static float seqAdd(float[] a) {
		float ret = 0;
		
		for (int i = 0; i < a.length; i++)
			ret += a[i];
		
		return ret;
	}

	// sort an array of floats and then sum sequentially
	public static float sortAdd(float[] a) {
		Arrays.sort(a);
		return seqAdd(a);
	}

	// scan linearly through an array for two minimum values,
	// remove them, and put their sum back in the array. repeat.
	public static float min2ScanAdd(float[] a) {
		int min1, min2;
		float tmp;
		
		if (a.length == 0) return 0;
		
		for (int i = 0, end = a.length; i < a.length - 1; i++, end--) {
			
			if (a[0] < a[1]) { min1 = 0; min2 = 1; }	// initialize
			else { min1 = 1; min2 = 0; }
			
			for (int j = 2; j < end; j++) {		// find two min indices
				if (a[min1] > a[j]) { min2 = min1; min1 = j; }
				else if (a[min2] > a[j]) { min2 = j; }
			}
			
			tmp = a[min1] + a[min2];	// add together
			if (min1<min2) {			// put into first slot of array
				a[min1] = tmp;			// fill second slot from end of array
				a[min2] = a[end-1];
			}
			else {
				a[min2] = tmp;
				a[min1] = a[end-1];
			}
		}
		
		return a[0];
	}

	// read floats from a Scanner
	// returns an array of the floats read
	private static float[] getFloats(Scanner s) {
		ArrayList<Float> a = new ArrayList<Float>();

		while (s.hasNextFloat()) {
			float f = s.nextFloat();
			if (f >= 0)
				a.add(f);
		}
		return toFloatArray(a);
	}

	// copies an ArrayList to an array
	private static float[] toFloatArray(ArrayList<Float> a) {
		float[] ret = new float[a.size()];
		for(int i = 0; i < ret.length; i++)
			ret[i] = a.get(i);
		return ret;
	}


	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);

		System.out.printf("Enter the adding algorithm to use ([h]eap, [m]in2scan, se[q], [s]ort): ");
		char algo = s.next().charAt(0);

		System.out.printf("Enter the positive floats that you would like summed: ");
		float[] values = getFloats(s);
		float sum = 0;

		s.close();

		if (values.length == 0) {
			System.out.println("You must enter at least one value");
			System.exit(0);
		}
		else if (values.length == 1) {
			System.out.println("Sum is " + values[0]);
			System.exit(0);
			
		}
		
		switch (algo) {
		case 'h':
			sum = heapAdd(values);
			break;
		case 'm':
			sum = min2ScanAdd(values);
			break;
		case 'q':
			sum = seqAdd(values);
			break;
		case 's':
			sum = sortAdd(values);
			break;
		default:
			System.out.println("Invalid adding algorithm");
			System.exit(0);
			break;
		}

		System.out.printf("Sum is %f\n", sum);		

	}

}
