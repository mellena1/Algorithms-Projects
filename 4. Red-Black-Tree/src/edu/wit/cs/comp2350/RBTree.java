package edu.wit.cs.comp2350;

public class RBTree extends LocationHolder {
	
	private DiskLocation nil = new DiskLocation(-1, -1);
	private DiskLocation root = nil;

	/**Sets up the tree*/
	public RBTree(){
		setBlack(nil);
		nil.left = nil;
		nil.right = nil;
		nil.parent = nil;
	}
	
	/**Sets z to red*/
	private void setRed(DiskLocation z) {
		if (z != nil)
			z.color = RB.RED;
	}
	
	/**Sets z to black*/
	private void setBlack(DiskLocation z){
		z.color = RB.BLACK;
	}
	
	/**@return the found node or nil if not found*/
	@Override
	public DiskLocation find(DiskLocation d) {
		DiskLocation checking = root;
		do{
			if(d.equals(checking))
				return checking;
			else if(d.isGreaterThan(checking))
				checking = checking.right;
			else
				checking = checking.left;
		}
		while(!checking.equals(nil));
		return nil;
	}

	/**@return the element after d*/
	@Override
	public DiskLocation next(DiskLocation d) {
		if(d.right != nil)
			return min(d.right);
		else
			return up(d, true);
	}
	
	/**@param d  starting location
	 * @param next  true if using for next method, false if using for prev
	 * @return nil if hit top of tree, parent of d if it is a left/right child depending on next bool*/
	private DiskLocation up(DiskLocation d, boolean next){
		DiskLocation p = d.parent;
		if(p == nil || d == (next?p.left:p.right)) //left child if looking for next, right if looking for prev
			return p;
		else
			return up(p, next);
	}

	/**@return the element previous of d*/
	@Override
	public DiskLocation prev(DiskLocation d) {
		if(d.left != nil)
			return max(d.left);
		else
			return up(d, false);
	}

	/**Insert an element d*/
	@Override
	public void insert(DiskLocation d) {
		d.parent = findParent(d, root, nil);
		if(d.parent == nil){
			root = d;
			root.parent = nil;
		}
		else{
			if(d.isGreaterThan(d.parent))
				d.parent.right = d;
			else
				d.parent.left = d;
		}
		d.left = nil;
		d.right = nil;
		setRed(d);
		fixInsert(d);
	}
	
	/**Helper to fix the insertion*/
	private void fixInsert(DiskLocation d){
		while(d.parent.color == RB.RED){
			if(d.parent == d.parent.parent.left){ //if d's parent is a left child
				DiskLocation y = d.parent.parent.right;
				if(y.color == RB.RED){
					setBlack(d.parent);
					setBlack(y);
					setRed(d.parent.parent);
					d = d.parent.parent;
				}//end if
				else{
					if(d == d.parent.right){ //d is a right child
						d = d.parent;
						rotateLeft(d);
					}
					setBlack(d.parent);
					setRed(d.parent.parent);
					rotateRight(d.parent.parent);
				}//end else
			}//end if
			else{
				DiskLocation y = d.parent.parent.left;
				if(y.color == RB.RED){
					setBlack(d.parent);
					setBlack(y);
					setRed(d.parent.parent);
					d = d.parent.parent;
				}//end if
				else{
					if(d == d.parent.left){ //d is a right child
						d = d.parent;
						rotateRight(d);
					}
					setBlack(d.parent);
					setRed(d.parent.parent);
					rotateLeft(d.parent.parent);
				}//end else
			}//end else
		}//end while
		setBlack(root);
	}

	/**@return the node of what parent d should have*/
	private DiskLocation findParent(DiskLocation d, DiskLocation curr, DiskLocation parent) {
		if(curr == nil)
			return parent;
		if(d.isGreaterThan(curr))
			return findParent(d, curr.right, curr);
		else
			return findParent(d, curr.left, curr);
	}
	
	/**Rotates the subtree of d to the left
	 * @param d  the starting node*/
	private void rotateLeft(DiskLocation d){
		DiskLocation y = d.right;
		d.right = y.left; //turn y's left subtree into d's right subtree
		
		if(y.left != nil)
			y.left.parent = d;
		
		y.parent = d.parent; //link parents
		
		if(d.parent == nil)
			root = y;
		else if(d == d.parent.left)
			d.parent.left = y;
		else
			d.parent.right = y;
		
		y.left = d; //put d on y's left
		d.parent = y;
	}
	
	/**Rotates the subtree of d to the right
	 * @param d  the starting node*/
	private void rotateRight(DiskLocation d){
		DiskLocation y = d.left;
		d.left = y.right; //turn y's right subtree into d's left subtree
		
		if(y.right != nil)
			y.right.parent = d;
		
		y.parent = d.parent; //link parents
		
		if(d.parent == nil)
			root = y;
		else if(d == d.parent.right)
			d.parent.right = y;
		else
			d.parent.left = y;
		
		y.right = d; //put d on y's right
		d.parent = y;
	}

	/**@return the height of the tree*/
	@Override
	public int height() {
		return _height(root);
	}
	
	/**Helper for height*/
	private int _height(DiskLocation d) {
		if(d == nil)
			return 0;
		return 1 + Math.max(_height(d.right), _height(d.left));
	}
	
	/**@return the min node in the subtree of d*/
	public DiskLocation min(DiskLocation d){
		while(d.left != nil)
			d = d.left;
		return d;
	}
	
	/**@return the max node in the subtree of d*/
	public DiskLocation max(DiskLocation d){
		while(d.right != nil)
			d = d.right;
		return d;
	}
	
	/**@return a string of the tree in order of nodes*/
	@Override
	public String toString(){
		DiskLocation temp = min(root);
		
		String str = "";
		while(temp != nil){
			str += "d: " + temp + " left: " + temp.left + " right: " + temp.right + "\n";
			temp = next(temp);
		}
		return str;
	}
}
