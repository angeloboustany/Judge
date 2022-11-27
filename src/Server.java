import java.net.ServerSocket;

public class Server {
    public static Leaderboard leaderboard = new Leaderboard();
    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(5000);
            System.out.println("listening to port:5000");
            leaderboard.loadLeaderboard();

            while (true) {
                // This thread will handle the client separately
                new Thread(new ClientHandler(server.accept())).start();
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
