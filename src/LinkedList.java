public class LinkedList {
    public Node head;
    public Node tail;
    public Object data;
    public int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    //add a node to the begining of the list
    public void addFirst(Object data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
            size++;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
            size++;
        }
    }

    //remove a node from the begining of the list
    public void removeFirst() {
        if (head == null) {
            System.out.println("The list is empty");
        } else {
            head = head.next;
            head.prev = null;
            size--;
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int getSize() {
        return size;
    }

    public void printList() {
        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    public void printListBackwards() {
        Node current = tail;
        while (current != null) {
            System.out.println(current.data);
            current = current.prev;
        }
    }

    public Object getFirst() {
        return null;
    }
}
