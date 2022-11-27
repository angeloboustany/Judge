public class Queue {
    
    private LinkedList list;
    
    public Queue() {
        list = new LinkedList();
    }
    
    public void enqueue(Object data) {
        list.addFirst(data);
    }
    
    public void dequeue() {
        list.removeFirst();
    }

    //peek at the first element
    public Object peek() {
        return list.getFirst();
    }
    
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    public int getSize() {
        return list.getSize();
    }
    
    public void printQueue() {
        list.printList();
    }
    
}
