import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static Leaderboard leaderboard = new Leaderboard();
    public static Queue submissionsQueue = new Queue();
    public static HashMap connections = new HashMap();
    public static ProblemHandler problems = new ProblemHandler();
    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(5000);
            server.setReuseAddress(true);
            System.out.println("listening to port:5000");
            leaderboard.loadLeaderboard();
            System.out.println("Leaderboard loaded");
            problems.loadProblems();
            System.out.println("Problems loaded");
            problems.printProblem();

            while (true){
                Socket socket = server.accept();
                System.out.println("New Client connected: " + socket.getInetAddress().getHostAddress());
                ClientHandler client = new ClientHandler(socket);
                Thread thread = new Thread(client);
                thread.start();
                //new Thread(new ClientHandler(server.accept())).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
