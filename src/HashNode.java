import java.net.Socket;

public class HashNode {
    private String key;
    private Socket value;
    private HashNode next;
    public HashNode(String key, Socket value) {
        this.key = key;
        this.value = value;
        this.next = null;
    }
    public String getKey() {
        return key;
    }
    public Socket getValue() {
        return value;
    }
    public void setValue(Socket value) {
        this.value = value;
    }
    public HashNode getNext() {
        return next;
    }
    public void setNext(HashNode next) {
        this.next = next;
    }
}
