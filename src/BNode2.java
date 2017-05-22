import java.util.ArrayList;

/**
 * Created by Eyal on 5/22/2017.
 */
public class BNode2 <T> {

    private ArrayList<TreeItem<T>> items;
    private int maxItems;

    public BNode2(int maxItems) {
        items = new ArrayList<>();
        this.maxItems = maxItems;
    }

    public BNode2(int maxItems, ArrayList<TreeItem<T>> items)
    {
        this.maxItems = maxItems;
        this.items = items;
    }

    public TreeItem<T> Add(TreeItem itemToAdd)
    {
        if (items.size() == 0)
        {
            insertAt(itemToAdd, 0);
            return null;
        }

        int indexToInsertAt = findFirstGreaterIndex(itemToAdd.getKey());
        if (indexToInsertAt == items.size()
                && items.get(indexToInsertAt - 1).getRight() != null)
        {
            TreeItem<T> itemBubbled = items.get(indexToInsertAt - 1).getRight().Add(itemToAdd);
            if (itemBubbled != null)
            {
                return this.Add(itemBubbled);
            }
            return null;
        }

        return insertAt(itemToAdd, indexToInsertAt);
    }


    private TreeItem<T> insertAt(TreeItem<T> itemToAdd, int index)
    {
        items.add(index, itemToAdd);

        if (index > 0)
            items.get(index - 1).setRight(itemToAdd.getLeft());

        if (items.size() > index + 1) // if the item is not inserted at the end of the array
            items.get(index + 1).setLeft(itemToAdd.getRight());

        if (items.size() > maxItems)
        {
            return Split();
        }

    }

    private int findFirstGreaterIndex(int key)
    {
        for(int i = 0 ; i < items.size(); i++)
            if (items.get(i).getKey() > key)
                return i;

        return items.size();
    }


    private TreeItem<T> Split()
    {
        ArrayList<TreeItem<T>> lowerThan = new ArrayList<>(items.subList(0, items.size()/2 - 1));
        ArrayList<TreeItem<T>> greaterThan = new ArrayList<>(items.subList(items.size()/2 + 1, items.size() + 1));
        TreeItem<T> median = items.get(items.size()/2);

        median.setLeft(new BNode2(maxItems, lowerThan));
        median.setRight(new BNode2(maxItems, greaterThan));

        return median;
    }




}
