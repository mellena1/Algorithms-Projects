package edu.wit.cs.comp2350;

public class UnionSets {
	public static void makeSet(Vertex v){
		v.parent = v;
		v.treeSize = 1;
	}
	
	/**@return the vertex at the top of the Union of this tree*/
	public static Vertex findSet(Vertex v){
		if(v.parent != v){
			return findSet(v.parent);
		}
		return v;
	}
	
	/** Merge two sets
	 * @param u parent of set 1
	 * @param v parent of set 2
	 * @return parent of the new tree*/
	public static Vertex Union(Vertex u, Vertex v){
		if(u.treeSize < v.treeSize){
			u.parent = v;
			v.treeSize += u.treeSize;
			return v;
		}else{
			v.parent = u;
			u.treeSize += v.treeSize;
			return u;
		}
	}
}