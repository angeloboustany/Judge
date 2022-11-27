import java.net.Socket;

public class HashMap {
    private HashNode[] buckets;
    private int size;
    private int capacity;
    public HashMap() {
        this.capacity = 100;
        this.size = 0;
        this.buckets = new HashNode[capacity];
    }
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size() == 0;
    }
    public int getIndex(String key) {
        int hashCode = key.hashCode();
        int index = hashCode % capacity;
        return index;
    }
    public Socket get(String key) {
        int index = getIndex(key);
        HashNode head = buckets[index];
        while (head != null) {
            if (head.getKey().equals(key)) {
                return head.getValue();
            }
            head = head.getNext();
        }
        return null;
    }
    public void put(String key, Socket value) {
        int index = getIndex(key);
        HashNode head = buckets[index];
        while (head != null) {
            if (head.getKey().equals(key)) {
                head.setValue(value);
                return;
            }
            head = head.getNext();
        }
        size++;
        head = buckets[index];
        HashNode newNode = new HashNode(key, value);
        newNode.setNext(head);
        buckets[index] = newNode;
        if ((1.0 * size) / capacity >= 0.7) {
            HashNode[] temp = buckets;
            buckets = new HashNode[2 * capacity];
            capacity *= 2;
            size = 0;
            for (HashNode headNode : temp) {
                while (headNode != null) {
                    put(headNode.getKey(), headNode.getValue());
                    headNode = headNode.getNext();
                }
            }
        }
    }
    public Socket remove(String key) {
        int index = getIndex(key);
        HashNode head = buckets[index];
        HashNode prev = null;
        while (head != null) {
            if (head.getKey().equals(key)) {
                break;
            }
            prev = head;
            head = head.getNext();
        }
        if (head == null) {
            return null;
        }
        size--;
        if (prev != null) {
            prev.setNext(head.getNext());
        } else {
            buckets[index] = head.getNext();
        }
        return head.getValue();
    }  
}
