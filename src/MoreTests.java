import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eyal on 5/31/2017.
 */
public class MoreTests {

    public static void main(String[] args)
    {
        BTree tree = new BTree(2);
        ArrayList<Block> blocks = Block.blockFactory(0, 100);
        HashMap<Integer, Block> map = new HashMap<Integer, Block>();

        map.put(50, blocks.get(50));
        map.put(40, blocks.get(40));
        map.put(20, blocks.get(20));
        map.put(30, blocks.get(30));
        map.put(70, blocks.get(70));
        map.put(90, blocks.get(90));
        map.put(91, blocks.get(91));
        map.put(92, blocks.get(92));
        map.put(93, blocks.get(93));
        map.put(94, blocks.get(94));
        map.put(95, blocks.get(95));
        map.put(96, blocks.get(96));
        map.put(21, blocks.get(21));
        map.put(46, blocks.get(46));
        map.put(47, blocks.get(47));
        map.put(44, blocks.get(44));
        map.put(1, blocks.get(1));
        map.put(6, blocks.get(6));

        for (Map.Entry<Integer, Block> item : map.entrySet())
        {
            tree.insert(item.getValue());
        }

        System.out.println("Testing node sizes: " + testNodeSizes(tree.getRoot(), 3));
        System.out.println("Testing node orders: " + testNodesOrdered(tree.getRoot()));
        System.out.println("Testing node values: " + testNodeValues(tree.getRoot(), Integer.MIN_VALUE, Integer.MAX_VALUE));
        System.out.println("Testing search: " + testSearch(tree, map));

        tree.createMBT();

    }

    public static boolean testNodeSizes(BNode node, int maxSize)
    {
        if (node == null)
            return true;

        if (node.getBlocksList().size() > maxSize)
            return false;

        for (int i = 0; i < node.getChildrenList().size(); i++)
        {
            if (!testNodeSizes(node.getChildAt(i), maxSize))
                return false;
        }

        return true;
    }

    public static boolean testNodesOrdered(BNode node)
    {
        if (node == null)
            return true;

        for (int i = 0 ; i < node.getBlocksList().size() - 1; i++)
        {
            if (node.getBlockAt(i).getKey() > node.getBlockAt(i + 1).getKey())
                return false;
        }

        return true;
    }

    public static boolean testNodeValues(BNode node, int minValue, int maxValue)
    {
        if (node == null)
            return true;

        for (int i = 0; i < node.getBlocksList().size(); i++)
            if (node.getBlockAt(i).getKey() < minValue ||
                    node.getBlockAt(i).getKey() > maxValue)
                return false;

        for (int i = 0; i < node.getBlocksList().size() - 1; i++)
        {
            if (!testNodeValues(
                    node.getChildAt(i),
                    minValue,
                    node.getBlockAt(i).getKey()))
                return false;

            minValue = node.getBlockAt(i).getKey();
        }

        return testNodeValues(node.getChildAt(node.getNumOfBlocks() - 1),
                minValue,
               maxValue);
    }

    public static boolean testSearch(BTree tree, HashMap<Integer, Block> keysToBlocks)
    {
        for (Map.Entry<Integer, Block> item : keysToBlocks.entrySet())
        {
            Block expected = item.getValue();
            Block actual = tree.search(item.getKey());

            if (!expected.equals(actual))
            {
                System.out.println("Key " + item.getKey() + " expected: " + expected.toString() + " actual " + actual);
                return false;
            }
        }

        return true;
    }



}
