package edu.wit.cs.comp2350;

import java.util.ArrayList;

/**Trie class for looking for words
 * @author Andrew Mellen
 * @date 2/10/2017*/
public class Trie extends Speller {
	private Node rootNode;
	
	/**Creates a new empty Trie*/
	public Trie(){
		rootNode = new Node(((char)0), false);
	}
	
	/**Inserts a string into the trie*/
	@Override
	public void insertWord(String s) {
		Node currentNode = rootNode;
		char[] charArray = s.toCharArray();
		
		for(int x = 0; x < charArray.length; x++){
			char c = charArray[x];
			
			if(currentNode.array[c-97] == null) //node hasn't been created yet
				currentNode.array[c-97] = new Node(c, x==charArray.length-1);
			else if(x==charArray.length-1) //end of word and node created
					currentNode.array[c-97].isWord = true;
			
			currentNode = currentNode.array[c-97];
		}
	}

	/**@return true if the string is in the trie*/
	@Override
	public boolean contains(String s) {
		Node currentNode = rootNode;
		char[] charArray = s.toCharArray();
		
		for(int x = 0; x < charArray.length; x++){
			char c = charArray[x];
			
			if(currentNode.array[c-97] == null)
				return false;
			if(x == charArray.length-1 && !currentNode.array[c-97].isWord)
				return false;
			
			currentNode = currentNode.array[c-97];
		}
		
		return true;
	}

	/**@return suggestions with 2 char difference for the given string*/
	@Override
	public String[] getSugg(String s) {
		if(s.isEmpty()) //if s is an empty string just return empty array
			return new String[0];
		
		ArrayList<Node> nodes = new ArrayList<>();
		nodes.add(rootNode);
		ArrayList<String> words = new ArrayList<String>();
		words.add("");
		ArrayList<Integer> diff = new ArrayList<Integer>();
		diff.add(0);
		return _getSugg(nodes, words, s, diff, 0);
	}
	
	/**Helper for getSugg
	 * @param nodes  all nodes in a row that will be looked at
	 * @param words  words corresponding with each node
	 * @param s  the word being looked at
	 * @param diff  the difference for each node
	 * @param height  the row of nodes currently looking at (starting at 0)
	 * @return  string of suggestions*/
	private String[] _getSugg(ArrayList<Node> nodes, ArrayList<String> words, String s, ArrayList<Integer> diff, int height){
		//Initialize return Arrays
		ArrayList<Node> retNodes = new ArrayList<>();
		ArrayList<Integer> retDiff = new ArrayList<>();
		ArrayList<String> retWords = new ArrayList<>();
		
		//Go through nodes that worked in row above
		for(int x = 0; x < nodes.size(); x++){
			Node n = nodes.get(x);
			for(int y = 0; y < 26; y++){ //go through whole array in each node
				
				if(n.array[y] != null){ //there is no node made here
					
					if(s.charAt(height) != n.array[y].c){ //need to increment diff
						if(diff.get(x)+1 <= 2){ //if diff is going to be too high just ignore that node
							retDiff.add(diff.get(x)+1);
							retNodes.add(n.array[y]);
							retWords.add(words.get(x) + n.array[y].c);
						}
					}
					
					else{ //char is same as this node
						retDiff.add(diff.get(x));
						retNodes.add(n.array[y]);
						retWords.add(words.get(x) + n.array[y].c);
					}
					
				}//end if for node being null
			}//end for
		}
		
		height++; //increment height
		
		if(retNodes.isEmpty()) //no nodes left, should just return empty array
			return new String[0];
		else if(height == s.length()){ //reached the length of the word
			for(int x = 0; x < retWords.size(); x++){ //get rid of nodes that aren't words
				if(!retNodes.get(x).isWord){
					retWords.remove(x);
					retNodes.remove(x);
					x--;
				}
			}
			return retWords.toArray(new String[0]);
		}
		else //keep going down rows
			return _getSugg(retNodes, retWords, s, retDiff, height);
	}

	/**Node class*/
	private class Node{
		/**char that node represents*/
		public char c;
		/**Array of nodes branching down from this node*/
		public Node[] array;
		/**node represents the last char in a word if true*/
		public boolean isWord;
		
		public Node(char c, boolean isWord){
			this.c = c;
			this.isWord = isWord;
			array = new Node[26];
		}
	}
	
}
