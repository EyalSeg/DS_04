import java.util.ArrayList;
import java.util.ListIterator;

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


    @Override
    public Block search(int key) {

        for (int i = 0 ; i < blocksList.size(); i++)
        {
            Block currentBlock = getBlockAt(i);

            if (currentBlock.getKey() == key)
                return currentBlock;

            if (getBlockAt(i).getKey() < key)
                continue;

            BNode child = getChildAt(i);
            return child != null? child.search(key) : null;
        }

        BNode child = getChildAt(blocksList.size());
        return child != null? child.search(key) : null;
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
          //  int childIndex = indexToInsert == blocksList.size() ?
          //          indexToInsert + 1 : indexToInsert;

            insertToChild(itemToAdd, indexToInsert);
        }
    }


    @Override
    public void delete(int key) {
        Block toRemoveBlock = this.search(key);
        int toRemoveIndex = this.searchBlockIndex(key);

        // Case 1
        if(this.isLeaf() && this.blocksList.contains(toRemoveBlock)) // not sure that the second part is necessary or right
            this.getBlocksList().remove(toRemoveBlock);

        // Case 2,3,4
        else if(!this.isLeaf()){
            BNode iChild = this.getChildAt(toRemoveIndex-1);
            // I think that this is wrong
            // The problem is with the situation where there is no right child so
            // child in index toRemove+1 doesn't exist
            BNode iPlusChild = this.getChildAt(toRemoveIndex);


            // Case 2
            if(iChild.getNumOfBlocks() >= t){
                Block predecessor = iChild.getPredecessor(key, 1); // version 1 - is with delete
                this.getBlocksList().add(predecessor);
                this.getBlocksList().remove(toRemoveBlock);
            }

            // Case 3, 4
            else if(iChild.getNumOfBlocks() == t - 1) {

                // Case 3
                if (iPlusChild.getNumOfBlocks() >= t) {
                    Block successor = iPlusChild.getSuccessor(key, 1);
                    this.getBlocksList().add(successor);
                    this.getBlocksList().remove(toRemoveBlock);
                }

                // Case 4 // what about the option that there is no more childs to merge?
                // maybe there is a mistake here for the option that there is no toRemove + 1 in indexes of children
                else if(iPlusChild.getNumOfBlocks() == t - 1){
                    this.getBlocksList().add(toRemoveBlock);
                    iChild.getBlocksList().addAll(iPlusChild.getBlocksList());
                    this.getChildrenList().remove(iPlusChild);
                }
            }
        }
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




    @Override
    public MerkleBNode createHashNode() {

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

        BNode child = childrenList.get(childIndex);
        child.insertNonFull(blockToInsert);

        if (child.getNumOfBlocks() > t * 2 - 1)
            splitChild(childIndex);
    }
}


