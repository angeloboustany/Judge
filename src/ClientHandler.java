import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    Contestant contestant = null;
    public ClientHandler(Socket socket){
        this.clientSocket = socket;
    }
    //set Contestant
    public void setContestant(Contestant contestant){
        this.contestant = contestant;
    }
    public void run (){
        try{
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            label:
            while (true){
                System.out.println("Waiting for command");
                String request = in.readUTF();
                System.out.println("log: "+ request);

                switch (request) {
                    case "end":
                        System.out.println("Client disconnected");
                        //save leaderboard
                        Server.leaderboard.saveLeaderboard();
                        break label;
                    case "help":
                        out.writeUTF("Available commands: end, help, search, submit, create, problems, leaderboard, profile, deleteProblem, deleteProfile");
                        out.flush();
                        break;
                    case "submit": {
                        // receive file
                        if (contestant == null) {
                            out.writeUTF("false");
                            out.flush();
                            continue;
                        }
                        out.writeUTF("true");
                        out.flush();
                        String problemId = in.readUTF();
                        String ext = in.readUTF();
                        if (ext.equals("noExt")) {
                            continue;
                        }

                        String path = "submissions/" + "P" + problemId + "U" + contestant.getUsername() + "." + ext;
                        receiveFile(path);
                        Submissions.addSubmission(problemId, path, contestant.getUsername(), ext);
                        break;
                    }
                    case "register":

                        System.out.println("Waiting for a username...");
                        String username = in.readUTF();
                        if (Server.leaderboard.searchContestant(username)) {
                            out.writeUTF("#");
                            out.flush();
                            contestant = Server.leaderboard.getContestant(username);
                            Server.connections.put(username, clientSocket);
                            continue;
                        }
                        out.writeUTF(" ");
                        Server.connections.put(username, clientSocket);
                        System.out.println("Waiting for a name...");
                        String name = in.readUTF();
                        System.out.println("Waiting for an email...");
                        String email = in.readUTF();

                        contestant = new Contestant(name, email, username);
                        System.out.println("New contestant registered: " + contestant.getName());
                        Server.leaderboard.addContestant(contestant);
                        contestant.saveContestant();
                        break;
                    case "leaderboard":
                        out.writeUTF(Server.leaderboard.printLeaderboard());
                        out.flush();
                        break;
                    case "profile":
                        Contestant c;
                        if (contestant != null) {
                            c = Server.leaderboard.getContestant(contestant.getUsername());
                            out.writeUTF(c.printProfile());
                            out.flush();
                        }
                        out.writeUTF("no profile found");
                        out.flush();
                        break;
                    case "create": {
                        String problemId = in.readUTF();
                        String problemName = in.readUTF();
                        String problemDescription = in.readUTF();
                        String inputFile = in.readUTF();
                        String pathToInputfile = "subIn/" + inputFile + ".txt";
                        receiveFile(pathToInputfile);
                        String testout = in.readUTF();
                        String pathToTestout = "subTest/" + testout + ".txt";
                        receiveFile(pathToTestout);
                        int timeInMillis = Integer.parseInt(in.readUTF());
                        int memoryInBytes = Integer.parseInt(in.readUTF());

                        ProblemNode problem = new ProblemNode(problemId, problemName, problemDescription, inputFile, testout, timeInMillis, memoryInBytes);
                        Server.problems.addProblem(problem);
                        problem.saveProblem();
                        break;
                    }
                    case "problems":
                        out.writeUTF(Server.problems.printProblems());
                        out.flush();
                        break;
                    case "deleteProblem": {
                        String problemId = in.readUTF();
                        Server.problems.deleteProblem(problemId);
                        break;
                    }
                    case "deleteProfile":
                        if (contestant != null) {
                            Server.leaderboard.removeContestant(contestant);
                            Server.connections.remove(contestant.getUsername());
                        }
                        break label;
                    case "search": {
                        String problem = in.readUTF();
                        out.writeUTF(Server.problems.getProblem(problem).displayProblem());
                        out.flush();
                        break;
                    }
                    default:
                        out.writeUTF("<< Invalid command (Type 'help' for list of commands): ");
                        out.flush();
                        break;
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
