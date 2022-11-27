import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private static DataOutputStream out = null;
    private static DataInputStream in = null;
    
    public ClientHandler(Socket socket){
        this.clientSocket = socket;
    }
    public void run (){
        try{
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            Contestant contestant = null;

            while (true){
                String request = in.readUTF();
                System.out.println("log: "+ request);

                if (request.equals("end")){
                    System.out.println("Client disconnected");
                    break;
                }
                else if (request.equals("help")){
                    out.writeUTF("Available commands: end, help, submit, leaderboard, submissions, profile");
                    out.flush();
                    continue;
                }
                else if (request.equals("submit")){
                    // receive file
                    if (contestant == null){
                        out.writeUTF("false");
                        out.flush();
                        continue;
                    }
                    out.writeUTF("true");
                    out.flush();
                    String problemId = in.readUTF();
                    String ext = in.readUTF();
                    if (ext.equals("noExt")){
                        continue;
                    }
                    receiveFile("submissions" + "/" + contestant.getUsername() + "-" + problemId + "."+ ext);
                }
                else if (request.equals("register")){

                    System.out.println("Waiting for a username...");
                    String username = in.readUTF();
                    if (Server.leaderboard.searchContestant(username)) {
                        out.writeUTF("#");
                        out.flush();
                        contestant = Server.leaderboard.getContestant(username);
                        continue;
                    }
                    out.writeUTF(" ");
                    System.out.println("Waiting for a name...");
                    String name = in.readUTF();
                    System.out.println("Waiting for an email...");
                    String email = in.readUTF();

                    contestant = new Contestant(name, email, username);
                    System.out.println("New contestant registered: " + contestant.getName());
                    Server.leaderboard.addContestant(contestant);
                    contestant.saveContestant();
                    continue;
                }
                else if (request.equals("leaderboard")){
                    out.writeUTF(Server.leaderboard.printLeaderboard());
                    out.flush();
                    continue;
                }
                else if (request.equals("submissions")){
                    //TODO: send submissions
                    continue;
                }
                else if (request.equals("profile")){
                    //TODO: implement profile
                }
                else{
                    out.writeUTF("<< Invalid command (Type 'help' for list of commands): ");
                    out.flush();
                    continue;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                clientSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    private void receiveFile(String fileName) throws Exception {
        int bytes;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long size = in.readLong(); // read file size
        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = in.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes; // read upto file size
        }
        fileOutputStream.close();
    }
}
