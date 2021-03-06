import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eyal on 5/31/2017.
 */
public class MoreTests {

    public static void main(String[] args)
    {
        int t = 3;
        BTree tree = new BTree(t);
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

        // Addition of mine
        map.put(12, blocks.get(12));
        map.put(15, blocks.get(15));
        map.put(17, blocks.get(17));
        map.put(28, blocks.get(28));
        map.put(25, blocks.get(25));
        map.put(54, blocks.get(54));
        map.put(87, blocks.get(87));
        map.put(39, blocks.get(39));

        for (Map.Entry<Integer, Block> item : map.entrySet())
        {
            tree.insert(item.getValue());
        }

        tree.createMBT();

        System.out.println("Testing node sizes: " + testNodeSizes(tree.getRoot(), t * 2 - 1));
        System.out.println("Testing node orders: " + testNodesOrdered(tree.getRoot()));
        System.out.println("Testing node values: " + testNodeValues(tree.getRoot(), Integer.MIN_VALUE, Integer.MAX_VALUE));
        System.out.println("Testing search: " + testSearch(tree, map));
        System.out.println("Testing numOfBlocks: " + testNumOfBlocks(tree.getRoot()));

        for (Map.Entry<Integer, Block> item : map.entrySet())
        {
            int keyToRemove = item.getKey();
            tree.delete(keyToRemove);

            if (tree.search(keyToRemove) != null)
                System.out.println("failed to remove " + keyToRemove);

            if (!testNodeSizes(tree.getRoot(), t * 2 - 1))
                System.out.println("Node sizes fucked up, yo");

            if (!testNodesOrdered(tree.getRoot()))
                System.out.println("Nodes order fucked up, yo");

            if (!testNodeValues(tree.getRoot(), Integer.MIN_VALUE, Integer.MAX_VALUE))
                System.out.println("node values fucked up, yo");

            if(!testNumOfBlocks(tree.getRoot()))
                System.out.println("numOfBlocks fucked up, yo");

        }

        System.out.println("Test block number: " + testBlockNumber(tree.getRoot(), tree.getRoot()));
        System.out.println("\n\n\n");
        System.out.println(tree);

    }

    public static boolean testBlockNumber(BNode nody, BNode rootNode){
        if(nody == null){
            return true;
        }
        int Tnumber = nody.getT();
        int sizeOfCurrentBlockList = nody.getBlocksList().size();
        boolean tester = true;
        if(nody != rootNode && nody.getBlocksList().size() < Tnumber - 1){
            tester= false;
        }

        if(tester == false){
            System.out.println("there are not enough blocks in this node");
            System.out.println("\n"+nody);
        }
        if(tester == true){
            for(int i = 0; i < nody.getChildrenList().size(); i++){
                testBlockNumber(nody.getChildAt(i), rootNode);
            }
        }

        return tester;
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

        return testNodeValues(node.getChildAt(node.getChildrenList().size() - 1),
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

    public static boolean testNumOfBlocks(BNode node)
    {
        if (node == null)
            return true;

        if (node.getNumOfBlocks() != node.getBlocksList().size())
            return false;

        for (BNode child : node.getChildrenList())
        {
            if (!testNumOfBlocks(child))
                return false;
        }

        return true;
    }




}
