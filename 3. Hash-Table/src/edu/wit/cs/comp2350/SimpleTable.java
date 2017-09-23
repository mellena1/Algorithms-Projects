package edu.wit.cs.comp2350;

public class SimpleTable extends HashTable {
		
	public SimpleTable(int size) {
		super(size);
	}

	@Override
	public int calculateHash(String word) {
		int hash = 0;
		
		for(byte b : word.getBytes()){
			hash = ((hash * 31) + b) % tableSize;
		}
		
		return hash;
	}

}
