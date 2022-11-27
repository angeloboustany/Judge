public class Queue {
    
    private LinkedList list;
    
    public Queue() {
        list = new LinkedList();
    }
    
    public void enqueue(SubNode data) {
        list.addLast(data);
    }
    
    public void dequeue() {
        list.removeFirst();
    }

    //peek at the first element
    public SubNode peek() {
        return list.getFirst();
    }
    
    public boolean isEmpty() {
        return peek() == null;
    }
    
    public int getSize() {
        return list.getSize();
    }
    
    public void printQueue() {
        list.printList();
    }

}
