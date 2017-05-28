import sun.jvm.hotspot.debugger.cdbg.basic.BasicVoidType;

// SUBMIT
public class BTree implements BTreeInterface {

	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	private BNode root;
	private final int t;

	/**
	 * Construct an empty tree.
	 */
	public BTree(int t) { //
		this.t = t;
		this.root = null;
	}

	// For testing purposes.
	public BTree(int t, BNode root) {
		this.t = t;
		this.root = root;
	}

	@Override
	public BNode getRoot() {
		return root;
	}

	@Override
	public int getT() {
		return t;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((root == null) ? 0 : root.hashCode());
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
		BTree other = (BTree) obj;
		if (root == null) {
			if (other.root != null)
				return false;
		} else if (!root.equals(other.root))
			return false;
		if (t != other.t)
			return false;
		return true;
	}
	
	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////


	@Override
	public Block search(int key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(Block b) {
		BNode constantRoot = root;
		Block myBlock = new Block(1, new byte[10]);
		BNode childrenNode = new BNode(t, myBlock);
		int keyValue = b.getKey();

		// for case that this is root
		if(this.root == null){
			BNode root = childrenNode; // I've put here numbers it not right
																			  // just wanted it to work
		}
		// for all other cases
		else {
			BNode node = root;
			while(!node.isEmpty()) {
				if (node.getChildrenList().size() == 0) {
					this.addKey(b, 0);
					if (node.getChildrenList().size() <= t) {
						break;
					}

					split(node);
					break;
				}

				// when the new one is less or equals to the first
				int lesser = node.getBlockKeyAt(0);
				if(keyValue <= lesser) {
					node = node.getChildAt(0);
					continue;
				}

				// When the new one is greater of the last
				int numberOfKeys = node.getNumOfBlocks();
				int last = numberOfKeys-1;
				int greater = node.getBlockKeyAt(last);
				if(keyValue > greater){
					node.getChildAt(numberOfKeys);
					continue;
				}

				// when the new one is in the middle of the blocks
				for(int i =0; i < node.getNumOfBlocks(); i++){
					int prevKey = node.getBlockKeyAt(i-1);
					int nextKey = node.getBlockKeyAt(i);
					if(keyValue > prevKey && keyValue < nextKey){
						node = node.getChildAt(i);
						break;
					}
				}
			}


		}

		// some line of code that needs to increase the size of the array by one
		// size++

		return;
	}

	private void addKey(Block toAdd, int index){
		root.getBlocksList().add(index, toAdd);
	}

	private void split(BNode node){
		BNode nodeToSplit = node;
		int numberOfKeys = nodeToSplit.getNumOfBlocks();
		int medianIndex = numberOfKeys/2;
		int medianValue = node.getBlockKeyAt(medianIndex);

		// split the left child
		BNode left = new BNode(t, nodeToSplit.getBlockAt(0));
		for(int i = 0; i < medianIndex; i++){
			left.addKey(nodeToSplit.getBlockAt(i), i);
		}

		if(nodeToSplit.getChildrenList().size() > 0){
			for(int j =0 ; j < medianIndex; j++){
				BNode child = nodeToSplit.getChildAt(j);
				left.getChildrenList().add(child);
			}
		}

		// split the right child
		BNode right = new BNode(t, nodeToSplit.getBlockAt(0)));
		for(int i = medianIndex + 1; i < numberOfKeys; i++){
			right.addKey(nodeToSplit.getBlockAt(i), i);
		}

		if(nodeToSplit.getChildrenList().size() > 0){
			for(int j = medianIndex + 1 ; j < numberOfKeys; j++){
				BNode child = nodeToSplit.getChildAt(j);
				right.getChildrenList().add(child);
			}
		}

		if(node == root){
			BNode newRoot = new BNode(t, node);
			newRoot.addKey(nodeToSplit.getBlockAt(medianIndex), medianIndex);
			this.root = newRoot;
			this.root.getChildrenList().add(left);
			this.root.getChildrenList().add(right);
		}
//		else {
//			BNode parent = node.get
//		}
	}

	@Override
	public void delete(int key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MerkleBNode createMBT() {
		// TODO Auto-generated method stub
		return null;
	}


}
