/**
 * Created by Eyal on 5/22/2017.
 */
public class TreeItem<T> {

    private T data;
    private int key;
    private BNode2 left;
    private BNode2 right;

    public TreeItem(T data, int key) {
        this.data = data;
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public BNode2 getLeft() {
        return left;
    }

    public BNode2 getRight() {
        return right;
    }

    public void setLeft(BNode2 left) {
        this.left = left;
    }

    public void setRight(BNode2 right) {
        this.right = right;
    }

    public T getData() {
        return data;
    }
}
