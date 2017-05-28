import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

//SUBMIT
public class BNode implements BNodeInterface {

	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	private final int t;
	private int numOfBlocks;
	private boolean isLeaf;
	private ArrayList<Block> blocksList;
	private ArrayList<BNode> childrenList;

	/**
	 * Constructor for creating a node with a single child.<br>
	 * Useful for creating a new root.
	 */
	public BNode(int t, BNode firstChild) {
		this(t, false, 0);
		this.childrenList.add(firstChild);
	}

	/**
	 * Constructor for creating a <b>leaf</b> node with a single block.
	 */
	public BNode(int t, Block firstBlock) {
		this(t, true, 1);
		this.blocksList.add(firstBlock);
	}

	public BNode(int t, boolean isLeaf, int numOfBlocks) {
		this.t = t;
		this.isLeaf = isLeaf;
		this.numOfBlocks = numOfBlocks;
		this.blocksList = new ArrayList<Block>();
		this.childrenList = new ArrayList<BNode>();
	}

	// For testing purposes.
	public BNode(int t, int numOfBlocks, boolean isLeaf,
			ArrayList<Block> blocksList, ArrayList<BNode> childrenList) {
		this.t = t;
		this.numOfBlocks = numOfBlocks;
		this.isLeaf = isLeaf;
		this.blocksList = blocksList;
		this.childrenList = childrenList;
	}

	@Override
	public int getT() {
		return t;
	}

	@Override
	public int getNumOfBlocks() {
		return numOfBlocks;
	}

	@Override
	public boolean isLeaf() {
		return isLeaf;
	}

	@Override
	public ArrayList<Block> getBlocksList() {
		return blocksList;
	}

	@Override
	public ArrayList<BNode> getChildrenList() {
		return childrenList;
	}

	@Override
	public boolean isFull() {
		return numOfBlocks == 2 * t - 1;
	}

	@Override
	public boolean isMinSize() {
		return numOfBlocks == t - 1;
	}
	
	@Override
	public boolean isEmpty() {
		return numOfBlocks == 0;
	}
	
	@Override
	public int getBlockKeyAt(int indx) {
		return blocksList.get(indx).getKey();
	}
	
	@Override
	public Block getBlockAt(int indx) {
		return blocksList.get(indx);
	}

	@Override
	public BNode getChildAt(int indx) {
		return childrenList.get(indx);
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((blocksList == null) ? 0 : blocksList.hashCode());
		result = prime * result
				+ ((childrenList == null) ? 0 : childrenList.hashCode());
		result = prime * result + (isLeaf ? 1231 : 1237);
		result = prime * result + numOfBlocks;
		result = prime * result + t;
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BNode other = (BNode) obj;
		if (blocksList == null) {
			if (other.blocksList != null)
				return false;
		} else if (!blocksList.equals(other.blocksList))
			return false;
		if (childrenList == null) {
			if (other.childrenList != null)
				return false;
		} else if (!childrenList.equals(other.childrenList))
			return false;
		if (isLeaf != other.isLeaf)
			return false;
		if (numOfBlocks != other.numOfBlocks)
			return false;
		if (t != other.t)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "BNode [t=" + t + ", numOfBlocks=" + numOfBlocks + ", isLeaf="
				+ isLeaf + ", blocksList=" + blocksList + ", childrenList="
				+ childrenList + "]";
	}

	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////

	public void addKey(Block toAdd, int index){
		this.getBlocksList().add(index, toAdd);
	}
	
	
	@Override
	public Block search(int key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertNonFull(Block d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int key) {
		// if the deleted value is in the current node which is a leaf
		if(this.isLeaf())
			this.getBlocksList().remove(key);


		if(!this.isLeaf() && this.getBlocksList().size() >= t){
			if(this.getBlocksList().size() >= t){
				int i = 0;
				Block blockToSwitch = this.getChildrenList().get(this.childrenList.size()).findSuccessor(key);
				while(i < this.getNumOfBlocks() && this.getBlockKeyAt(i) < key)
					i = i + 1;
				shift(this, this.getBlockAt(i), blockToSwitch); // shift with null means - needs to be deleted
			}

//			if(this.getBlocksList() < t)
		}
	}

	private void mergeChildWithSibling(int childIndex){
		BNode childNode = this.getChildAt(childIndex);
		if(this.getChildrenList().get(childIndex + 1) != null){
			BNode rightSibling = this.getChildAt(childIndex + 1);
			merge(childNode, rightSibling);
		}
		else {
			BNode leftSibling = this.getChildAt(childIndex - 1);
			merge(childNode, leftSibling);
		}

	}

	// need to be checked I think that this is ok
	private void shiftFromLeftSibling(int childIndex){
		BNode parent = this;
		Block rightBlockParent = parent.getBlockAt(this.getNumOfBlocks());
		parent.delete(rightBlockParent.getKey());

		BNode childToShiftTo = this.getChildAt(childIndex);
		childToShiftTo.addKey(rightBlockParent, 0);

		BNode LeftChildToShiftFrom = this.getChildAt(childIndex-1);
		Block blockToTakeFromLeft = LeftChildToShiftFrom.getBlockAt(LeftChildToShiftFrom.getNumOfBlocks());

		LeftChildToShiftFrom.delete(blockToTakeFromLeft.getKey());
	}

	private void shiftFromRightSibiling(int childIndex){

	}



	private int getBlockIndex(Block b1){
		int i;
		for(i = 0; i < this.getNumOfBlocks(); i++)
			i = i + 1;
		return i;
	}

	// This function suppose to start with the right child of the block that I try to find
	// successor for a key in it
	private Block findSuccessor(int key){
		if(this.isLeaf()){
			return this.getBlocksList().get(0);
		}
		else{ //for now it's only else, it was else if
			 return this.getChildrenList().get(0).findSuccessor(key);
		}
	}

	private void merge(BNode node1, BNode node2){
		// put 2 arrays of 2 nodes in one array, sort it, and put them together
		BNode mergedNode = new BNode(t, node1.getNumOfBlocks()+node2.getNumOfBlocks(), node1.isLeaf(),
									 node1.getBlocksList(), node1.getChildrenList());
		int node1BlockNum = node1.getNumOfBlocks();
		for(int i = 1; i < node2.getNumOfBlocks(); i++){
			mergedNode.addKey(node2.getBlockAt(i), node1BlockNum+i);
		}

		node1 = mergedNode;
		node2 = null;
	}

	private void shift(BNode node, Block block1, Block block2){

		// The element block2 is being deleted. First I delete the block to be removed
		// Then I put the content of block1 into the place of block2

		// The condition that the block that I need to switch with is one index before
		if(block2 == (this.getBlockAt(getBlockIndex(block1) - 1))){
			Block tempBlock = block1;
			this.getBlocksList().remove(getBlockIndex(block1));
			this.getBlocksList().set(getBlockIndex(block2), tempBlock);
		}

		// This is for inserting block - block2 instead of some other block - block1
		else if(!this.getBlocksList().contains(block2)){
			this.getBlocksList().set(getBlockIndex(block1), block2);
		}

		// regular switch - block1 instead of block2 and block2 instead of block1
		else if(node.getBlocksList().contains(block2)){
			int indexOf2 = node.getBlocksList().indexOf(block2);
			node.addKey(block2, node.getBlocksList().indexOf(block1));
			node.addKey(block1, indexOf2);
		}

		// Inserting last element
		else {
			int lastIndex = node.getNumOfBlocks();
			node.addKey(block1, lastIndex);
		}

	}

	public class blockComparator implements Comparator<Block>{

		public int compare(Block block1, Block block2){
			  return block1.getKey()-block2.getKey();
		}
	}

	@Override
	public MerkleBNode createHashNode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
