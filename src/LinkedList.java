public class LinkedList {
    public SubNode head;
    public SubNode tail;
    public SubNode data;
    public int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }
    public LinkedList(SubNode data) {
        this.data = data;
        head = null;
        tail = null;
        size = 0;
    }

    //add a node to the begining of the list
    public void addFirst(SubNode newNode) {
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
    //add a node to the end of the list
    public void addLast(SubNode newNode) {
        if (head == null) {
            head = newNode;
            tail = newNode;
            size++;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            size++;
        }
    }
    //remove a node from the begining of the list
    public void removeFirst() {
        if (head == null) {
            return;
        } else if (head == tail) {
            head = null;
            tail = null;
            size--;
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
        SubNode current = head;
        while (current != null) {
            System.out.println(current.toString());
            current = current.next;
        }
    }

    public void printListBackwards() {
        SubNode current = tail;
        while (current != null) {
            System.out.println(current.toString());
            current = current.prev;
        }
    }

    public SubNode getFirst() {
        return head;
    }
}
