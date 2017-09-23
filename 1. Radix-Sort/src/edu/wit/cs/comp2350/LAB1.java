package edu.wit.cs.comp2350;

/* Sorts integers from command line using various algorithms 
 * 
 * Wentworth Institute of Technology
 * COMP 2350
 * Programming Assignment 0
 * 
 */

import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class LAB1 {

	public final static int MAX_INPUT = 524287;
	public final static int MIN_INPUT = 0;

	/**Runs a Counting Sort on a given array
	 * @param a  the array that needs to be sorted
	 * @return the sorted array*/
	public static int[] countingSort(int[] a) {
		int k = 0; //Highest number in the array
		for(int n : a)
			if(n > k)
				k = n;
		
		int[] count = new int[k+1]; //Array ranges from 0 to k
		
		//Count the instances of each number in the array
		for(int x : a)
			count[x]++;
		
		//Add the elements back into a new array in sorted order
		int[] sortedArray = new int[a.length];
		int nextIndex = 0;
		for(int x = 0; x <= k; x++){
			for(int y = count[x]; y > 0; y--){
				sortedArray[nextIndex] = x;
				nextIndex++;
			}
		}
		
		return sortedArray;
	}

	/**Runs a Radix Sort on a given array
	 * @param a  the array that needs to be sorted
	 * @return the sorted array*/
	public static int[] radixSort(int[] a) {
		//Find the max number of digits
		int d = 0; //Highest number of digits of a single number in the array
		for(int n : a){
			int length = Integer.toString(n).length();
			if(length > d)
				d = length;
		}
		
		
		int[][] digits = toDigitArray(a, d); //Array of numbers broken up into digits
		int[][] output = new int[a.length][d+1]; //output of each iteration of the radix loop
		
		//Radix Sort
		for(int currentDigit = d; currentDigit >= 1; currentDigit--){//Run through each digit; don't include index 0, it is numOfLeadingZeros
			//Both size 10 because numbers are in decimal
			int count[] = new int[10];
			int pos[] = new int[10];
			
			//set count[x] to number of items = x
			for(int currentNum = 0; currentNum < digits.length; currentNum++){
				count[ digits[currentNum][currentDigit] ]++;
			}
			
			int sum = 0;
			//set pos[x] to number of items < x
			for(int z = 0; z < pos.length; z++){
				pos[z] = sum;
				sum += count[z];
			}

			//write x in output array at index pos[x]
			for(int currentNum = 0; currentNum < digits.length; currentNum++){
				int indexFromPos = pos[ digits[currentNum][currentDigit] ];
				pos[ digits[currentNum][currentDigit] ]++; //increment pos[x]
				
				//Copy all digits to new spot in output array
				for(int j = 0; j < digits[currentNum].length; j++){
					output[indexFromPos][j] = digits[currentNum][j];
				}
			}
			
			//copy output into digits for next loop iteration
			for(int x = 0; x < output.length; x++) 
				for(int y = 0; y < output[x].length; y++)
					digits[x][y] = output[x][y];
		}
		
		return backToIntArray(digits);
	}
	
	/**Turns a single int array into a 2D array of [number index][digits]<br/><br/>
	 * It adds leading zeros to all numbers to make sure every number in the array<br/>
	 * has the same amount of digits<br/><br/>
	 * 
	 * <b>NOTE: The return array's index 0 is the number of the leading zeros that were added<br/>
	 * to the number!</b><br/><br/>
	 * 
	 * <b>EXAMPLE:</b> if it handles 450 with a d of 6, it would return [3000450], where 3 says that<br/>
	 * 3 leading 0s were added, and the rest is the number.
	 * @param a  the array to be broken up
	 * @param d  the max number of digits (every number will have this many after execution)
	 * @return the array (int[][])*/
	private static int[][] toDigitArray(int[] a, int d){
		//int[number index][number of leading zeros | individual digits of number]
		int[][] numberArray = new int[a.length][d + 1];
		
		//Add leading zeros to make sure every number has same number of digits
		for(int x = 0; x < a.length; x++){
			char[] asChar = Integer.toString(a[x]).toCharArray();
			
			int leadingZeros = d - asChar.length;
			numberArray[x][0] = leadingZeros;
			int y = 1; //Index in numberArray
			//Add the zeros
			while(leadingZeros > 0){
				numberArray[x][y] = 0;
				y++;
				leadingZeros--;
			}
			int asCharIndex = 0;
			//Fill the rest of the array with the number (asChar)
			while(asCharIndex < asChar.length){
				numberArray[x][y] = asChar[asCharIndex] - 48; //convert from ascii char
				asCharIndex++;
				y++;
			}
		}
		return numberArray;
	}
	
	/**Takes a 2D array converted with toDigitArray back into a 1D array*/
	private static int[] backToIntArray(int[][] a){
		int[] output = new int[a.length];
		for(int x = 0; x < a.length; x++){
			for(int y = a[x][0] + 1; y < a[x].length; y++){
				output[x] += ( a[x][y] * Math.pow(10, a[x].length - y - 1) );
			}
		}
		return output;
	}

	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	********************************************/
	
	// example sorting algorithm
	public static int[] insertionSort(int[] a) {

		for (int i = 1; i < a.length; i++) {
			int tmp = a[i];
			int j;
			for (j = i-1; j >= 0 && tmp < a[j]; j--)
				a[j+1] = a[j];
			a[j+1] = tmp;
		}
		
		return a;
	}

	/* Implementation note: The sorting algorithm is a Dual-Pivot Quicksort by Vladimir Yaroslavskiy,
	 *  Jon Bentley, and Joshua Bloch. This algorithm offers O(n log(n)) performance on many data 
	 *  sets that cause other quicksorts to degrade to quadratic performance, and is typically 
	 *  faster than traditional (one-pivot) Quicksort implementations. */
	public static int[] systemSort(int[] a) {
		Arrays.sort(a);
		return a;
	}

	// read ints from a Scanner
	// returns an array of the ints read
	private static int[] getInts(Scanner s) {
		ArrayList<Integer> a = new ArrayList<Integer>();

		while (s.hasNextInt()) {
			int i = s.nextInt();
			if ((i <= MAX_INPUT) && (i >= MIN_INPUT))
				a.add(i);
		}

		return toIntArray(a);
	}

	// copies an ArrayList to an array
	private static int[] toIntArray(ArrayList<Integer> a) {
		int[] ret = new int[a.size()];
		for(int i = 0; i < ret.length; i++)
			ret[i] = a.get(i);
		return ret;
	}

	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		System.out.printf("Enter the sorting algorithm to use ([c]ounting, [r]adix, [i]nsertion, or [s]ystem): ");
		char algo = s.next().charAt(0);
		
		System.out.printf("Enter the integers that you would like sorted: ");
		int[] unsorted_values = getInts(s);
		int[] sorted_values = {};

		s.close();

		switch (algo) {
		case 'c':
			sorted_values = countingSort(unsorted_values);
			break;
		case 'r':
			sorted_values = radixSort(unsorted_values);
			break;
		case 'i':
			sorted_values = insertionSort(unsorted_values);
			break;
		case 's':
			sorted_values = systemSort(unsorted_values);
			break;
		default:
			System.out.println("Invalid sorting algorithm");
			System.exit(0);
			break;
		}
		
		System.out.println(Arrays.toString(sorted_values));
		
	}

}
