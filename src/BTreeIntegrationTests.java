import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Eyal on 6/5/2017.
 */
@RunWith(Theories.class)
public class BTreeIntegrationTests {

    public static @DataPoints
    int[] tValues = new int[]{2,3,4, 5, 6, 7, 8, 9, 10};

    public static @DataPoints
    HashMap<Integer, Block>[] blocks = new HashMap[] {
            getBlocksByRange(10, 20),
            getBlocksByRange(10, 50),
            getBlocksByRange(0, 230),
       //     getBlocksByRange(1, 1000),
    };


    @Theory
    public void insert(int t, HashMap<Integer, Block> map) {
        BTree testTree = new BTree(t);
        int itemsCount = 0;

        for (Map.Entry<Integer, Block> item : map.entrySet())
        {
            testTree.insert(item.getValue());
            itemsCount++;

            Assertions.assertEquals(itemsCount, TestHelper.countTreeItems(testTree.getRoot()), "Unexpected number of blocks");
        }

        TestHelper.AssertTree(testTree);

    }

    @Theory
    public void search(int t, HashMap<Integer, Block> blocks) {
        BTree testTree = new BTree(t);
        blocks.values().forEach(block -> testTree.insert(block));

        blocks.forEach((key, expected) -> {
            Block result = testTree.search(key);
            assertTrue(result != null, "Search result is null!");
            assertEquals(expected, result, "Search resulted in the wrong block!");
        });
    }

    @Theory
    public void delete(int t, HashMap<Integer, Block> blocks) {
        BTree testTree = new BTree(t);
        blocks.values().forEach(block -> testTree.insert(block));


        for (Map.Entry<Integer, Block> pair : blocks.entrySet())
        {
                TestHelper.deleteAndTest(testTree, pair.getKey());
        }

    }

    private static HashMap<Integer, Block> getBlocksByRange(int minValue, int maxValue)
    {
        HashMap map = new HashMap<>();
        ArrayList<Block> blocks = Block.blockFactory(minValue, maxValue);

        for (Block block : blocks)
            map.put(block.getKey(), block);

        return map;
    }


    private static HashMap<Integer, Block> getBlocks(){
        HashMap map = new HashMap<>();
        ArrayList<Block> blocks = Block.blockFactory(0, 100);

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

        return map;
    }


}