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
		int i = 1;

		BNode searchForNode = this.root;
		while (i < root.getNumOfBlocks() && key > searchForNode.getBlockKeyAt(i))
			i = i + 1;
		if(i <= root.getNumOfBlocks() && key == searchForNode.getBlockKeyAt(i))
			return searchForNode.getBlockAt(i);
		else if (searchForNode.isLeaf())
			return null;

		return root.getChildAt(i).search(key);
	}

	// this is the main structure of this, but not good yet
	@Override
	public void insert(Block b) {
		BNode r = this.root;
		if(r.getT() == 2*t - 1){
			BNode s = new BNode(t, false, 0); // the 0 here is probably a mistake
			this.root = s;
			s.getChildrenList().add(r);
			splitChild(s,1);
			insertNonFull(s, b);
		}
		else
			insertNonFull(r,b);
	}

	private void addKey(Block toAdd, int index){

		root.getBlocksList().add(index, toAdd);
	}

	private void splitChild(BNode node, int i){
		BNode childAti = node.getChildAt(i);
		BNode parentOfNode = new BNode(t,childAti.isLeaf(), 0); // not sure that this is good - like above
		for(int j = 1; j < t - 1; j++)
			// untill t-1, that is the middle of a node that have 2t-1 blocks, which is a node that we
			// are on because we got into splitChild function
			parentOfNode.addKey(childAti.getBlockAt(j+t),j);

		if(!childAti.isLeaf()){
			for(int j = 1; j < t; j++){
				parentOfNode.getChildrenList().add(j, childAti.getChildAt(j+t));
			}
		}

		for(int j = node.getNumOfBlocks()+1; j < i + 1; i--)
			switchPlaces(node, node.getBlockAt(i+1), node.getBlockAt(i));
		node.getChildrenList().add(i, parentOfNode);

		for(int j = node.getNumOfBlocks(); j < i; j--)
			switchPlaces(node, node.getBlockAt(i+1), node.getBlockAt(i));

		// Probably not workin but as a slekelton for fixing it later
		node.addKey(childAti.getBlockAt(t), i);


	}

	private void switchPlaces(BNode node, Block block1, Block block2){
		if(node.getBlocksList().contains(block2)){
			int indexOf2 = node.getBlocksList().indexOf(block2);
			node.addKey(block2, node.getBlocksList().indexOf(block1));
			node.addKey(block1, indexOf2);
		}
		else {
			int lastIndex = node.getNumOfBlocks();
			node.addKey(block1, lastIndex);
		}

	}

	private void insertNonFull(BNode node, Block k){
		int i = node.getNumOfBlocks();
		if(node.isLeaf()){
			while (i >= 1 && k.getKey() < node.getBlockKeyAt(i)){
				switchPlaces(node, node.getBlockAt(i+1), node.getBlockAt(i));
				i = i - 1;
			}
			switchPlaces(node, node.getBlockAt(i+1), k);
		}
		else {
			while(i >= 1 && k.getKey() < node.getBlockKeyAt(i))
				i = i - 1;

			i = i + 1;

			if (node.getNumOfBlocks() == 2*t - 1){
				splitChild(node, i);
				if(k.getKey() > node.getBlockKeyAt(i))
					i = i + 1;
			}

			insertNonFull(node.getChildAt(i), k);
		}
		return;
	}

	@Override
	public void delete(int key) {
		// first case - delete from current node - a leaf
		Block blockToDelete = search(key);
		BNode fromNodeToDelete = this.root;

		
	}

//	public boolean remove(Block blockToRemove){
//		if(blockToRemove == null)
//			return false;
//
//		int indexOf = this.
//	}

	@Override
	public MerkleBNode createMBT() {
		// TODO Auto-generated method stub
		return null;
	}


}
