/**
 * Created by Eyal on 5/22/2017.
 */
public class BTree2 {

    private BNode2<Object> first;
    private int maxItems;

    public BTree2(int maxItems) {
        first = new BNode2<Object>(maxItems);
    }

    public void Add(Object item)
    {
        TreeItem<Object> bubbledItem = first.Add(item);
        if (bubbledItem != null)
        {
            first = new BNode2<Object>(maxItems);
            first.Add(bubbledItem);
        }
    }

}
