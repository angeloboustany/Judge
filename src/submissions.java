import java.io.File;
import java.nio.file.Paths;

public class submissions {
    
    //read files from submission directory filtered by time continuesly and add them to a queue
    public static void main(String[] args){
        Queue q = new Queue();
        File folder = new File(Paths.get("submissions").toAbsolutePath().toString());
        File[] listOfFiles = folder.listFiles();
        while (true) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    q.enqueue(file);
                }
            }
            //read files from queue and send them to the server
            while (!q.isEmpty()) {
                File file = (File)q.peek();
                //send file to server
                q.dequeue();
            }
            //wait for 5 seconds
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
