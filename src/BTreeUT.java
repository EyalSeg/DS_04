import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Eyal on 6/5/2017.
 */
public class BTreeUT {

    @Test
    public void testInsert_leafNotFull()
    {
        throw new NotImplementedException();
    }

    @Test
    public void testInsert_leafIsFull_ShouldSplit()
    {
        throw new NotImplementedException();
    }

    @Test
    public void testInsert_internalNodeFull_ShouldSplit()
    {
        throw new NotImplementedException();
    }

    @Test
    public void testInsert_rootIsFull_ShouldSplit()
    {
        throw new NotImplementedException();
    }

    @Test
    public void testSearch_nonExistingItem()
    {
        int t = 3;
        BNode child1 =  createNode(t, new int[]{1, 2, 3, 4}, true, null);
        BNode child2 = createNode(t, new int[]{12, 13, 14, 15}, true, null);

        BTree testTree = new BTree(t, createNode(t, new int[]{10}, false, new BNode[]{
                child1, child2
        } ));

        assertEquals(null, testTree.search(8));
    }

    @Test
    public void testSearch_emptyTree()
    {
        BTree testTree = new BTree(2);

        assertEquals(null, testTree.search(10), "Search yielded a result!");
    }

    @Test
    public void testDelete_emptyTree()
    {
        BTree testTree = new BTree(2);

        testTree.delete(10);

        if (testTree.getRoot() == null)
            return;

        assertEquals(TestHelper.countTreeItems(testTree.getRoot()), 0, "Tree size is not 0");
    }

    @Test
    public void testDelete_nonExistingItem()
    {
        int t = 3;
        BNode child1 =  createNode(t, new int[]{1, 2, 3, 4}, true, null);
        BNode child2 = createNode(t, new int[]{12, 13, 14, 15}, true, null);

        BTree testTree = new BTree(t, createNode(t, new int[]{10}, false, new BNode[]{
                child1, child2
        } ));

        testTree.delete(9);
        assertEquals(9, TestHelper.countTreeItems(testTree.getRoot()), "An item was removed");
        TestHelper.AssertTree(testTree);
    }


    @Test
    public void testDelete_nonEmptyLeaf()
    {
        int t = 3;
        BNode child1 =  createNode(t, new int[]{1, 2, 3, 4}, true, null);
        BNode child2 = createNode(t, new int[]{12, 13, 14, 15}, true, null);

        BTree testTree = new BTree(t, createNode(t, new int[]{10}, false, new BNode[]{
                child1, child2
        } ));

        TestHelper.deleteAndTest(testTree, 1);
        TestHelper.deleteAndTest(testTree, 15);

    }

    @Test
    public void testDelete_emptyLeaf_shiftRight()
    {
        int t = 3;
        BNode child1 =  createNode(t, new int[]{1, 2, 3, 4}, true, null);
        BNode child2 = createNode(t, new int[]{11, 12}, true, null);
        BNode child3 = createNode(t, new int[]{101, 102, 103, 104}, true, null);


        BTree testTree = new BTree(t, createNode(t, new int[]{10}, false, new BNode[]{
                child1, child2
        } ));


            TestHelper.deleteAndTest(testTree, 12);


        assertEquals(3, child1.getNumOfBlocks(), "Item not shifted right from left child");
    }

    @Test void testDelete_emptyLeaf_shiftLeft()
    {
        int t = 3;
        BNode child1 =  createNode(t, new int[]{1, 2}, true, null);
        BNode child2 = createNode(t, new int[]{11, 12, 13, 14}, true, null);

        BTree testTree = new BTree(t, createNode(t, new int[]{10}, false, new BNode[]{
                child1, child2
        } ));

            TestHelper.deleteAndTest(testTree, 14);

        assertEquals(3, child2.getNumOfBlocks(), "Item not shifted left from right child");
    }

    @Test void testDelete_emptyLeaf_merge()
    {
        int t = 3;
        BNode child1 = createNode(t, new int[]{1, 2}, true, null);
        BNode child2 = createNode(t, new int[]{11, 12}, true, null);
        BNode child3 = createNode(t, new int[]{21, 22}, true, null);

        BTree testTree = new BTree(3, createNode(t, new int[]{10, 20}, false, new BNode[]{
                child1, child2, child3
        } ));

        TestHelper.deleteAndTest(testTree, 11);
        assertEquals(2, testTree.getRoot().getChildrenList().size(), "The children were not merged!");
    }

    @Test void testDelete_internalNode_replaceWithPredecessor()
    {
        int t = 3;
        BNode child1 = createNode(t, new int[]{1, 2, 3}, true, null);
        BNode child2 = createNode(t, new int[]{11, 12, 13}, true, null);

        BTree testTree = new BTree(t, createNode(t, new int[]{10}, false, new BNode[]{
                child1, child2
        } ));

        TestHelper.deleteAndTest(testTree, 10);
        assertEquals(1, testTree.getRoot().getNumOfBlocks(), "Too many items at the node");
        assertEquals(3, testTree.getRoot().getBlockAt(0).getKey(), "Wrong item at the node");
    }

    @Test void testDelete_internalNode_replaceWithSuccessor()
    {
        int t = 3;
        BNode child1 = createNode(t, new int[]{1, 2}, true, null);
        BNode child2 = createNode(t, new int[]{11, 12, 13}, true, null);

        BTree testTree = new BTree(t, createNode(t, new int[]{10}, false, new BNode[]{
                child1, child2
        } ));

        TestHelper.deleteAndTest(testTree, 10);
        assertEquals(1, testTree.getRoot().getNumOfBlocks(), "Too many items at the node");
        assertEquals(11, testTree.getRoot().getBlockAt(0).getKey(), "Wrong item at the node");
    }

    @Test void testDelete_internalNode_mergeChildren()
    {
        int t = 3;
        BNode child1 = createNode(t, new int[]{1, 2}, true, null);
        BNode child2 = createNode(t, new int[]{11, 12}, true, null);
        BNode child3 = createNode(t, new int[]{21, 22}, true, null);

        BTree testTree = new BTree(t, createNode(t, new int[]{10, 20}, false, new BNode[]{
                child1, child2, child3
        } ));

        TestHelper.deleteAndTest(testTree, 10);
        assertEquals(1, testTree.getRoot().getNumOfBlocks(), "Too many items at the node");
        assertEquals(20, testTree.getRoot().getBlockAt(0).getKey(), "Wrong item at the node");
        assertEquals(2, testTree.getRoot().getChildrenList().size(), "children not merged");
    }

    @Test void testDelete_deleteFromRoot_rootHasOneItem_createNewRoot()
    {
        int t = 3;
        BNode child1 = createNode(t, new int[]{1, 2}, true, null);
        BNode child2 = createNode(t, new int[]{11, 12}, true, null);

        BTree testTree = new BTree(t, createNode(t, new int[]{10}, false, new BNode[]{
                child1, child2
        } ));

        TestHelper.deleteAndTest(testTree, 10);
        assertNotEquals(null, testTree.getRoot(), "Root is null!");
        assertNotEquals(0, testTree.getRoot().getNumOfBlocks(), "Root is empty!");
        assertTrue(testTree.getRoot().getChildrenList() == null ||
                testTree.getRoot().getChildrenList().size() == 0, "Root has children although it is not supposed to");
        assertEquals(true, testTree.getRoot().isLeaf(), "Root is not marked as leaf even though it should be");
    }

    @Test void testDelete_internalNodeTooSmallToDeleteFrom_ShiftLeft()
    {
        int t = 2;
        BNode leaf1 = createNode(t, new int[]{5}, true, null);
        BNode leaf2 = createNode(t, new int[]{15}, true, null);
        BNode leaf3 = createNode(t, new int[]{25}, true, null);
        BNode leaf4 = createNode(t, new int[]{35}, true, null);
        BNode leaf5 = createNode(t, new int[]{55}, true, null);


        BNode internalNode1 =createNode(t, new int[]{10}, false, new BNode[]{
                leaf1, leaf2
        } );
        BNode internalNode2 =createNode(t, new int[]{30, 50}, false, new BNode[]{
                leaf3, leaf4, leaf5
        } );

        BTree testTree = new BTree(t, createNode(t, new int[]{20}, false, new BNode[]{
                internalNode1, internalNode2
        } ));

        //  TestHelper.AssertTree(testTree);

        TestHelper.deleteAndTest(testTree, 5);
        assertEquals(1, internalNode2.getNumOfBlocks(), "Node not shifted!");
    }

    @Test void testDelete_internalNodeTooSmallToDeleteFrom_ShiftRight()
    {
        int t = 2;
        BNode leaf1 = createNode(t, new int[]{5}, true, null);
        BNode leaf2 = createNode(t, new int[]{15}, true, null);
        BNode leaf3 = createNode(t, new int[]{35}, true, null);
        BNode leaf4 = createNode(t, new int[]{45}, true, null);
        BNode leaf5 = createNode(t, new int[]{55}, true, null);


        BNode internalNode1 =createNode(t, new int[]{10, 30}, false, new BNode[]{
                leaf1, leaf2, leaf3
        } );
        BNode internalNode2 =createNode(t, new int[]{50}, false, new BNode[]{
                leaf4, leaf5
        } );

        BTree testTree = new BTree(t, createNode(t, new int[]{40}, false, new BNode[]{
                internalNode1, internalNode2
        } ));

        //  TestHelper.AssertTree(testTree);

        TestHelper.deleteAndTest(testTree, 50);
        assertEquals(1, internalNode1.getNumOfBlocks(), "Node not shifted!");

    }

    @Test void testDelete_internalNodeTooSmallToDeleteFrom_Merge()
    {
        int t = 2;
        BNode leaf1 = createNode(t, new int[]{5,6}, true, null);
        BNode leaf2 = createNode(t, new int[]{15, 16}, true, null);
        BNode leaf3 = createNode(t, new int[]{45, 46}, true, null);
        BNode leaf4 = createNode(t, new int[]{55, 56}, true, null);


        BNode internalNode1 =createNode(t, new int[]{10}, false, new BNode[]{
                leaf1, leaf2
        } );
        BNode internalNode2 =createNode(t, new int[]{50}, false, new BNode[]{
                leaf3, leaf4
        } );

        BTree testTree = new BTree(t, createNode(t, new int[]{40}, false, new BNode[]{
                internalNode1, internalNode2
        } ));

        //TestHelper.AssertTree(testTree);
        TestHelper.deleteAndTest(testTree, 10);

        assertEquals(3, testTree.getRoot().getNumOfBlocks(), "Merged node incorrect");
    }



    private static BNode createNode(int t, int[] values, boolean isLeaf, BNode[] children)
    {

        ArrayList<Block> blocks = Block.blockFactory(values[0], values[values.length - 1]);

        Block[] blocksArray = Arrays.stream(values).
                mapToObj(value -> blocks.get(value - values[0])).
                toArray(size -> new Block[size]);

        ArrayList<Block> nodeBlocks = new ArrayList<>(Arrays.asList(blocksArray));

        ArrayList<BNode> childrenArraylist = isLeaf? new ArrayList<>(values.length + 1)
                : new ArrayList<>(Arrays.asList(children));

        BNode node = new BNode(t, nodeBlocks.size(), isLeaf, nodeBlocks, childrenArraylist);
        return node;
    }
}