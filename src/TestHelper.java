import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Eyal on 6/5/2017.
 */
public class TestHelper {

    public static int countTreeItems(BNode node) {
        if (node == null)
            return 0;

        assertEquals(node.getBlocksList().size(), node.getNumOfBlocks(),
                "getBlockList().size() is not equal to getNumOfBlocks!\n make sure you changed numOfBlocks whenever blocks were added/removed");

        int count = node.getNumOfBlocks();
        for (BNode child : node.getChildrenList())
            count += countTreeItems(child);

        return count;
    }

    public static void AssertTree(BTree tree) {
        AssertNodesOrdered(tree.getRoot());
        AssertNodeSizes(tree.getRoot(), tree.getT(), true);
        AssertNodeValues(tree.getRoot(), Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static void deleteAndTest(BTree tree, int keyToDelete) {
        int itemsCount = countTreeItems(tree.getRoot());

        tree.delete(keyToDelete);
        itemsCount--;

        Assertions.assertEquals(itemsCount, countTreeItems(tree.getRoot()), "Unexpected number of blocks");
        assertEquals(null, tree.search(keyToDelete), "Delete removed the wrong item!");
        AssertTree(tree);
    }


    public static void AssertNodesOrdered(BNode node) {
        if (node == null)
            return;

        for (int i = 0; i < node.getNumOfBlocks() - 1; i++) {
            assertTrue(node.getBlockAt(i).getKey() < node.getBlockAt(i + 1).getKey(),
                    "Items within the node " + node.toString() + "are not ordered");
        }

        node.getChildrenList().forEach(child -> AssertNodesOrdered(child));
    }

    public static void AssertNodeSizes(BNode node, int t, boolean isRoot) {
        if (node == null)
            return;

        Assertions.assertTrue(node.getNumOfBlocks() <= 2 * t - 1, "Node contains too many items!");

        if (!isRoot)
            Assertions.assertTrue(node.getNumOfBlocks() >= t - 1, "Node doesn't contain enough items!");

        node.getChildrenList().forEach(child -> AssertNodeSizes(child, t, false));
    }

    public static void AssertNodeValues(BNode node, int minValue, int maxValue) {
        if (node == null)
            return;

        node.getBlocksList().forEach(child -> assertTrue(child.getKey() >= minValue && child.getKey() <= maxValue,
                "Node value is out of range! Check parent node's values"));

        int newMinValue = minValue;

        if (node.isLeaf())
            return;

        for (int i = 0; i < node.getChildrenList().size() - 1; i++) {
            Block currentBlock = node.getBlockAt(i);
            AssertNodeValues(node.getChildAt(i), newMinValue, currentBlock.getKey());

            newMinValue = currentBlock.getKey();
        }

        AssertNodeValues(node.getChildAt(node.getNumOfBlocks()), newMinValue, maxValue);
    }
}
