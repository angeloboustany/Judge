import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static DataOutputStream out = null;
    private static DataInputStream in = null;

    public static void main(String[] args) {
        run();
    }
    
    private static void run(){
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        try {
            socket = new Socket("127.0.0.1", 5000);

            System.out.println("<< Connected to server");
            System.out.println("<< Enter 'end' to close the connection");
            System.out.println("<< Enter 'help' to see the list of commands");
            
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            label:
            while (true) {
                System.out.print(">> ");
                String message = scanner.nextLine();
                out.writeUTF(message);
                out.flush();

                switch (message) {
                    case "end":
                        System.out.println("<< Connection closed");
                        break label;
                    case "submit": {
                        String isReg = in.readUTF();
                        if (isReg.equals("false")) {
                            System.out.println("<< You are not registered");
                            System.out.println("<< Please register first by using the 'register' command");
                            continue;
                        }

                        System.out.println("<< Enter the problem id");
                        System.out.print(">> ");
                        String problemId = scanner.nextLine();
                        System.out.println("<< Your file class should be in the following format: P" + problemId + "U<username>.<ext>");
                        System.out.print("<< Enter the path to your submission file: ");
                        String path = scanner.nextLine();
                        // get file extention
                        String ext = path.substring(path.lastIndexOf(".") + 1);
                        if (!ext.equals("java") && !ext.equals("c") && !ext.equals("cpp")) {
                            System.out.println("<< Invalid file type");
                            out.writeUTF("noExt");
                            out.flush();
                            continue;
                        }
                        //send problem id
                        out.writeUTF(problemId);
                        out.flush();

                        //send file extention
                        out.writeUTF(ext);
                        out.flush();

                        //send file
                        sendFile(path);
                        System.out.println("<< File sent");
                        System.out.println("<< Waiting for the result...");
                        String result = in.readUTF();
                        System.out.println("<< Result: " + result);
                        continue;
                    }
                    case "register":
                        System.out.print("<< Enter your username: ");
                        String username = scanner.nextLine();
                        out.writeUTF(username);
                        out.flush();
                        String isTaken = in.readUTF();
                        if (isTaken.equals("#")) {
                            System.out.println("<< Welcome back " + username);
                            continue;
                        }
                        System.out.print("<< Enter your name:");
                        String name = scanner.nextLine();
                        out.writeUTF(name);
                        out.flush();
                        System.out.print("<< Enter your email: ");
                        String email = scanner.nextLine();
                        out.writeUTF(email);
                        out.flush();
                        continue;
                    case "create": {
                        System.out.print("<< Enter the problem id: ");
                        String problemId = scanner.nextLine();
                        out.writeUTF(problemId);
                        out.flush();
                        System.out.print("<< Enter the problem name: ");
                        String problemName = scanner.nextLine();
                        out.writeUTF(problemName);
                        out.flush();
                        System.out.print("<< Enter the problem description: ");
                        String problemDescription = scanner.nextLine();
                        out.writeUTF(problemDescription);
                        out.flush();
                        System.out.print("<< Enter the problem input file Name: ");
                        String problemInput = scanner.nextLine();
                        out.writeUTF(problemInput);
                        out.flush();
                        //send the input file
                        System.out.print("<< Enter the path to the input file: ");
                        String pathIn = scanner.nextLine();
                        sendFile(pathIn);

                        System.out.print("<< Enter the problem output file Name: ");
                        String problemOutput = scanner.nextLine();
                        out.writeUTF(problemOutput);
                        out.flush();
                        //send the output file
                        System.out.print("<< Enter the path to the output file: ");
                        String pathOut = scanner.nextLine();
                        sendFile(pathOut);

                        System.out.print("<< Enter the problem time limit: ");
                        String problemTimeLimit = scanner.nextLine();
                        out.writeUTF(problemTimeLimit);
                        out.flush();
                        System.out.print("<< Enter the problem memory limit: ");
                        String problemMemoryLimit = scanner.nextLine();
                        out.writeUTF(problemMemoryLimit);
                        out.flush();
                        System.out.println("<< Problem created");
                        continue;
                    }
                    case "deleteProblem": {
                        System.out.print("<< Enter the problem id: ");
                        String problemId = scanner.nextLine();
                        out.writeUTF(problemId);
                        out.flush();
                        System.out.println("<< Problem deleted");
                        continue;
                    }
                    case "search": {
                        System.out.print("<< Enter the problem id: ");
                        String problemId = scanner.nextLine();
                        out.writeUTF(problemId);
                        out.flush();
                        String response = in.readUTF();
                        System.out.println("<< " + response);
                        continue;
                    }
                    default: {
                        String response = in.readUTF();
                        System.out.println("<< " + response);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        scanner.close();
    }

    private static void sendFile(String path) throws Exception{
        int bytes;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        // send file size
        out.writeLong(file.length());
        // break file into chunks
        byte[] buffer = new byte[4*1024];
        while ((bytes=fileInputStream.read(buffer))!=-1){
            out.write(buffer,0,bytes);
            out.flush();
        }
        fileInputStream.close();
    }
}