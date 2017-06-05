import java.util.ArrayList;

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

    public boolean canRemoveFrom()
    {
        return blocksList.size() >= t;
    }

    @Override
    public Block search(int key) {

        for (int i = 0 ; i < blocksList.size(); i++)
        {
            Block currentBlock = getBlockAt(i);

            if (currentBlock.getKey() == key)
                return currentBlock;

            if (getBlockAt(i).getKey() < key)
                continue;

            if (isLeaf())
                return null;

            return getChildAt(i).search(key);
        }

        if (isLeaf())
            return null;

       return getChildAt(blocksList.size()).search(key);
    }


    // Duplicate code, I am not sure that I need to use that
    public int searchBlockIndex(int key){
        for (int i = 0 ; i < blocksList.size(); i++)
        {
            Block currentBlock = getBlockAt(i);

            if (currentBlock.getKey() == key)
                return i;

            if (getBlockAt(i).getKey() < key)
                continue;

            BNode child = getChildAt(i);
            return child != null? child.searchBlockIndex(key) : -1;
        }

        BNode child = getChildAt(blocksList.size());
        return child != null? child.searchBlockIndex(key) : -1;
    }

    @Override
    public void insertNonFull(Block itemToAdd) {
        int indexToInsert = findBlockPosition(itemToAdd.getKey());

        if (isLeaf())
            insertBlockAt(itemToAdd, indexToInsert);
        else
        {
            insertToChild(itemToAdd, indexToInsert);
        }
    }


    @Override
    public void delete(int key) {

        if (blocksList.size() == 0)
            return;

        for (int i = 0 ; i < blocksList.size(); i++) {

            Block currentBlock = getBlockAt(i);
            if (currentBlock.getKey() == key) {
                deleteAtIndex(i);
                return;
            }
            else if (currentBlock.getKey() > key)
            {
                deleteFromChild(i, key);
                return;
            }
        }

        deleteFromChild(blocksList.size(), key);
//        Block lastBlock = getBlockAt(getBlocksList().size() - 1);
//        if (key > lastBlock.getKey())
//        {
//            BNode lastChild = getChildAt(getChildrenList().size() - 1);
//            if (!lastChild.canRemoveFrom())
//            {
//                increaseChildSize(getChildrenList().size() - 1);
//                lastChild = getChildAt(getChildrenList().size() - 1);
//            }
//
//            lastChild.delete(key);
//        }


    }

    // version  - more thing that needs to be done in the functin
    // 1 - the predessecor will need to be deleted
    private Block getPredecessor(int key, int version){
        // Not sure that this is good, neeed to initalize it with some value
        Block blockToReturn = new Block(0, null); // not sure that this is good, the 0 part
        int indexOfCurrentBlock = this.findBlockPosition(key);

        // there is no right child -
        if(this.getChildrenList().size() < indexOfCurrentBlock){
            boolean foundPredecessor = false;
            int i = 0;
            while(!foundPredecessor && i < this.getNumOfBlocks()){
                if(this.getBlockAt(i).getKey() > key){
                    blockToReturn = this.getBlockAt(i);
                    foundPredecessor = true;
                }
                else
                    i++;

            }
            if(version == 1)
                this.delete(key);
        }
        // I send the first block with the left child and then only right
        // so if there are children, i need to go right
        if(this.getChildrenList().size() >= indexOfCurrentBlock){
            blockToReturn = this.getChildAt(indexOfCurrentBlock+1).getPredecessor(key, version);
        }

        return blockToReturn;

    }

    private Block getSuccessor(int key, int version){
        Block blockToReturn = new Block(0, null);
        int indexOfCurrentBlock = this.searchBlockIndex(key);

        if(this.getChildrenList().size() < indexOfCurrentBlock){
            boolean foundSuccessor = false;
            int i = 0;
            while(!foundSuccessor && i < this.getNumOfBlocks()){
                if(this.getBlockAt(i).getKey() > key){
                    blockToReturn = this.getBlockAt(i-1);
                    foundSuccessor = true;
                }
                else
                    i++;
            }
        }
        if(version == 1 && indexOfCurrentBlock != -1)
            this.delete(key);

        return blockToReturn;
    }

    private void replaceAndRemove(Block replaceAndRemove, Block toReplace){
        // this function is applied only on leafs, so only the part of the leaf in the delete sould be applied
        this.delete(replaceAndRemove.getKey());

    }

    private void deleteAtIndex(int index)
    {
        int key = getBlockKeyAt(index);
        if (isLeaf()) {
            blocksList.remove(index);
            numOfBlocks--;
            return;
        }

        Block childBlock = removeSuccessorOrPreccessor(index);
        if (childBlock != null) {
            blocksList.set(index, childBlock);
            return;
        }

        // if reached here, it means that no predecessor/successor could have been remove. Therefore, we must merge the children

        mergeChildren(index);
        getChildAt(index).delete(key); // the merge will insert the current index to the the created child. Therefore we must remove it
    }

    @Override
    public MerkleBNode createHashNode() {
        if (numOfBlocks == 0)
            return null;

        ArrayList<MerkleBNode> childNodes = new ArrayList<>();
        for (BNode child : childrenList) {
            if (child == null)
                continue;

            childNodes.add(child.createHashNode());
        }

        ArrayList<byte[]> itemsToHash = new ArrayList<>();
        for (Block item : blocksList)
            itemsToHash.add(item.getData());

        // insert the children between the blocks
        for (int i = 0 ; i < childNodes.size(); i++)
            itemsToHash.add(i * 2, childNodes.get(i).getHashValue());

        byte[] hashCode = HashUtils.sha1Hash(itemsToHash);
        MerkleBNode currentNode = new MerkleBNode(hashCode, childNodes.isEmpty(), childNodes);

        return currentNode;
    }


    public void splitChild(int index) {
        BNode child = getChildAt(index);
        int childMedian = child.getNumOfBlocks() / 2;
        if (child.getNumOfBlocks() % 2 == 0 )
            childMedian--;

        ArrayList<Block> belowChild = new ArrayList<>(child.blocksList.subList(0, childMedian));
        ArrayList<BNode> belowChildChildren = new ArrayList<>(child.childrenList.subList(0, childMedian + 1));

        ArrayList<Block> aboveChild = new ArrayList<>(child.blocksList.subList(childMedian + 1, child.getNumOfBlocks()));
        ArrayList<BNode> aboveChildChildren = new ArrayList<>(child.childrenList.subList
                (childMedian + 1, child.getChildrenList().size()));

        BNode leftNode = new BNode(t, child.isLeaf(), belowChild.size());
        leftNode.blocksList = belowChild;
        leftNode.childrenList = belowChildChildren;

        BNode rightNode = new BNode(t, child.isLeaf(), aboveChild.size());
        rightNode.blocksList = aboveChild;
        rightNode.childrenList = aboveChildChildren;

        insertBlockAt(child.getBlockAt(childMedian), index);

        childrenList.set(index, leftNode);
        childrenList.set(index + 1, rightNode);
        }

    // returns the index of the first block within blocklist who's key value is greater than or equal to the given key.
    // if all blocks are lesser than the given key, the index of the last block + 1 will be returned
    private int findBlockPosition(int key) {

        for (int i = 0; i < blocksList.size(); i++) {
            if (key <= blocksList.get(i).getKey())
                return i;
        }

        return blocksList.size();
    }

    private void insertBlockAt(Block blockToInsert, int index) {

        blocksList.add(index, blockToInsert);

        for (int i = childrenList.size(); i < blocksList.size(); i++)
            childrenList.add(null);

            childrenList.add(index + 1, null);

        numOfBlocks++;
    }

    private void insertToChild(Block blockToInsert, int childIndex) {
        if (childrenList.get(childIndex) == null)
            childrenList.set(childIndex, new BNode(t, false, 0));

        BNode child = getChildAt(childIndex);

        if (child.isFull())
        {
            splitChild(childIndex);
            if (getBlockAt(childIndex).getKey() < blockToInsert.getKey())
                childIndex++;

            child = getChildAt(childIndex);
        }

        child.insertNonFull(blockToInsert);


    }

    private void deleteFromChild(int childIndex, int keyToRemove)
    {
        BNode child = getChildAt(childIndex);
        if (child == null)
            return;

        if (!child.canRemoveFrom())
        {
            increaseChildSize(childIndex);

            // if the child we are trying to delete from was the last child, and he was removed to to merge
            if (childIndex == getChildrenList().size())
                childIndex--;

            child = getChildAt(childIndex);
        }

        child.delete(keyToRemove);
    }

    public class ShiftResult
    {
            public final Block shiftedBlock;
            public final BNode shiftedChild;

        private ShiftResult(Block shiftedBlock, BNode shiftedChild) {
            this.shiftedBlock = shiftedBlock;
            this.shiftedChild = shiftedChild;
        }
    }

    private ShiftResult shiftRight()
    {
        numOfBlocks--;

        if (isLeaf())
                return new ShiftResult(blocksList.remove(blocksList.size() - 1), null);

        return new ShiftResult(blocksList.remove(blocksList.size() - 1),
                childrenList.remove(childrenList.size() - 1));
    }

    private ShiftResult shiftLeft()
    {
        numOfBlocks--;

        if (isLeaf())
            return new ShiftResult(blocksList.remove(0), null);

        return new ShiftResult(blocksList.remove(0),
                childrenList.remove(0));
    }

    private void increaseChildSize(int childIndex)
    {
        // try shift from left child
        if (childIndex > 0 &&
                tryShiftRightFromChild(childIndex - 1))
            return;

        // try shift from right child
        if (childIndex < childrenList.size() - 1 &&
                tryShiftLeftFromChild(childIndex + 1))
            return;

        if (childIndex == getChildrenList().size() - 1)
            childIndex--;

        mergeChildren(childIndex);
    }

    private boolean tryShiftRightFromChild(int childToShiftFrom){
        BNode child = getChildAt(childToShiftFrom);
        if (!child.canRemoveFrom())
            return false;

        ShiftResult shift =  child.shiftRight();
        Block temp = getBlockAt(childToShiftFrom);

        blocksList.set(childToShiftFrom, shift.shiftedBlock);

        getChildAt(childToShiftFrom + 1).blocksList.add(0, temp);
        getChildAt(childToShiftFrom + 1).numOfBlocks++;

        if (shift.shiftedChild != null)
            getChildAt(childToShiftFrom + 1).childrenList.add(0, shift.shiftedChild);

        return true;
    }

    private boolean tryShiftLeftFromChild(int childToShiftFrom)
    {
        BNode child = getChildAt(childToShiftFrom);
        if (!child.canRemoveFrom())
            return false;

        ShiftResult shift = child.shiftLeft();
        Block temp = getBlockAt(childToShiftFrom - 1);
        blocksList.set(childToShiftFrom - 1, shift.shiftedBlock);

        getChildAt(childToShiftFrom  - 1).blocksList.add(temp);
        getChildAt(childToShiftFrom - 1).numOfBlocks++;

        if (shift.shiftedChild != null)
            getChildAt(childToShiftFrom - 1).childrenList.add(shift.shiftedChild);

        return true;
    }

    private Block removeSuccessorOrPreccessor(int blockIndex)
    {
        BNode leftChild = getChildAt(blockIndex);
        if (leftChild.canRemoveFrom())
        {
            Block predecessor = leftChild.findGreatest();
            leftChild.delete(predecessor.getKey());

            return predecessor;
        }

        BNode rightChild = getChildAt(blockIndex + 1);
        if (rightChild.canRemoveFrom())
        {
            Block successor = rightChild.findSmallest();
            rightChild.delete(successor.getKey());

            return successor;
        }

        return null;
    }

    public void mergeChildren(int leftChildIndex)
    {
        int rightChildIndex = leftChildIndex + 1;
        BNode leftChild = getChildAt(leftChildIndex);
        BNode rightChild = getChildAt(rightChildIndex);

        if (getNumOfBlocks() > 0)
        {
            leftChild.blocksList.add(getBlockAt(leftChildIndex));
            this.getBlocksList().remove(leftChildIndex);
            this.getChildrenList().remove(rightChildIndex);

            numOfBlocks--;
        }

        leftChild.blocksList.addAll(rightChild.blocksList);
        leftChild.childrenList.addAll(rightChild.childrenList);

        leftChild.numOfBlocks = leftChild.getBlocksList().size();
    }

    public Block findSmallest()
    {
        if (this.isLeaf() || getChildAt(0) == null)
            return blocksList.get(0);

        return getChildAt(0).findSmallest();
    }

    public Block findGreatest()
    {
        if (this.isLeaf() || getChildAt(childrenList.size() - 1) == null)
            return getBlockAt(getBlocksList().size() - 1);

        return getChildAt(getChildrenList().size() - 1).findGreatest();
    }

}


