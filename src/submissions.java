import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Submissions {
    private static DataOutputStream out = null;
    
    public Submissions(){}

    public static void addSubmission(String id, String path, String user, String extention) throws IOException{
        String problemId = id;
        String filePath = path;
        String language = extention;
        String username = user;
        String testout;
        String inputFile;
        long timeInMillis;

        ProblemNode problem = Server.problems.getProblem(problemId);
        if (problem == null) {
            System.out.println("Problem not found");
            return;
        }
        inputFile = "subIn/" + problem.getInputFile() + ".txt";
        testout = "subTest/"+ problem.getTestout() + ".txt";
        timeInMillis = problem.getTimeInMillis();

        SubNode sub = new SubNode(problemId, filePath, language, username, testout, inputFile, timeInMillis);
        Server.submissionsQueue.enqueue(sub);
        System.out.println("Submission added to queue");
        System.out.println("Checking queue..." + (Server.submissionsQueue.peek().getUsername()));
        System.out.println("Verdict Judge is running");
        VerdictJudge();
    }

    public void scanDirectory(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (ext.equals("java") || ext.equals("py") || ext.equals("cpp")) {
                        String problemId = fileName.substring(1, fileName.indexOf("U"));
                        String filePath = Paths.get(path, fileName).toString();
                        System.out.println("problemId: " + problemId + " filePath: " + filePath);
                    }
                }
            }
        }
    }

    public static String compile(String l,String problemId, String username) {
        System.out.println("Code compilation started...");
        ProcessBuilder p;
        boolean compiled = true;
        if (l.equals("java")) {
            p = new ProcessBuilder("javac", "-d", "." ,"P" + problemId + "U" + username + ".java");
        } else if (l.equals("c")) {
            p = new ProcessBuilder("gcc", "-w", "-o", "Main", "P" + problemId + "U" + username + ".c");
        } else {
            p = new ProcessBuilder("g++", "-w", "-o", "Main", "P" + problemId + "U" + username + ".cpp");
        }
        p.directory(new File("submissions"));
        p.redirectErrorStream(true);

        try {
            Process pp = p.start();
            InputStream is = pp.getInputStream();
            String temp;
            try (BufferedReader b = new BufferedReader(new InputStreamReader(is))) {
                while ((temp = b.readLine()) != null) {
                    compiled = false;
                    System.out.println(temp);
                }
                pp.waitFor();
            }

            if (!compiled) {
                is.close();
                return verdict.COMPILE_ERROR;
            }
            is.close();
            return verdict.COMPILE_SUCCESS;

        } catch (IOException | InterruptedException e) {
            System.out.println("in compile() " + e);
        }

        return verdict.COMPILE_ERROR;
    }

    public static String execute(String l, String n, long timeInMillis, String problemId, String username) {
        System.out.println("Code started executing.");
        ProcessBuilder p;
        if (l.equals("java")) {
            p = new ProcessBuilder("java", "submissions.P" + problemId + "U" + username);
        } else if (l.equals("c")) {
            p = new ProcessBuilder("Main.exe");
        } else {
            p = new ProcessBuilder("Main.exe");
        }
        p.directory(new File("submissions"));
        File in = new File(n);
        p.redirectInput(in);
        if (in.exists())
            System.out.println("Input file " + in.getAbsolutePath());
        p.redirectErrorStream(true);
        System.out.println("Current directory " + System.getProperty("user.dir"));
        File out = new File("subOut" + "/out(" + username + ").txt");

        p.redirectOutput(out);
        if (out.exists())
            System.out.println("Output file generated " + out.getAbsolutePath());
        try {
            Process pp = p.start();
            if (!pp.waitFor(timeInMillis, TimeUnit.MILLISECONDS)) {
                return verdict.TLE;
            }
            int exitCode = pp.exitValue();
            System.out.println("Exit Value = " + pp.exitValue());
            if (exitCode != 0)
                return verdict.RUN_ERROR;
        } catch (IOException ioe) {
            System.err.println("in execute() " + ioe);
        } catch (InterruptedException ex) {
            System.err.println(ex);
        }
        System.out.println("Code execution finished!");
        // delete executables
        deleteExecutables(l,problemId,username);
        return verdict.RUN_SUCCESS;
    }

    private static void deleteExecutables(String l,String problemId, String username) {
        if (l.equals("java")) {
            File f = new File("submissions/submissions/" + "P" + problemId + "U" + username + ".class");
            f.delete();
        } else {
            File f = new File("submissions/Main.exe");
            f.delete();
        }
    }

    public static String match(String testout,String username) {
        BufferedReader b1 = null, b2 = null;
        File f1, f2;
        try {
            System.out.println("Matching process started.");
            f1 = new File(testout);
            System.out.println("Test output exists? [" + f1.exists() + "] Path=" + f1.getAbsolutePath());
            f2 = new File("subOut" + "/" + "out(" + username + ").txt");
            System.out.println("Output exists? [" + f2.exists() + "] Path=" + f2.getAbsolutePath());
            b1 = new BufferedReader(new FileReader(f1));
            b2 = new BufferedReader(new FileReader(f2));

            String s1 = "", s2 = "", temp = "";
            while ((temp = b2.readLine()) != null) {
                s2 += temp.trim() + "\n";
            }
            System.out.println(f2.getName() + ":\n" + s2);
            while ((temp = b1.readLine()) != null) {
                s1 += temp.trim() + "\n";
            }
            System.out.println(f1.getName() + ":\n" + s1);
            System.out.println("Matching ended.");

            if (s1.equals(s2)) {
                return verdict.RIGHT;
            } else {
                return verdict.WRONG;
            }

        } catch (FileNotFoundException ex) {
            System.err.println("in match() " + ex);
        } catch (IOException ex) {
            System.err.println("in match() " + ex);
        } finally {
            try {
                if (b1 != null) {
                    b1.close();
                }
                if (b2 != null) {
                    b2.close();
                }
            } catch (IOException ex) {
                System.err.println("in match() " + ex);
            }
        }
        return verdict.WRONG;
    }

    public void deleteFiles(String id, String username) {
        File f = new File("submissions/" + "P" + id + "U" + username + ".java");
        f.delete();
        f = new File("submissions/" + "P" + id + "U" + username + ".c");
        f.delete();
        f = new File("submissions/" + "P" + id + "U" + username + ".cpp");
        f.delete();
        f = new File("subOut/" + "out(" + username + ").txt");
        f.delete();
    }

    public static String run(String language, long timeInMillis, String problemId, String username, String testout, String inputFile) {
        String VERDICT = "";
        String compileStatus = compile(language, problemId, username);
        System.out.println("Compile status: " + compileStatus);
        if (compileStatus.equals(verdict.COMPILE_SUCCESS)) {
            String executeStatus = execute(language, inputFile, timeInMillis, problemId, username);
            System.out.println("Execute status: " + executeStatus);
            if (executeStatus.equals(verdict.RUN_SUCCESS)) {
                String matchStatus = match(testout, username);
                VERDICT = matchStatus;
                System.out.println("Match status: " + matchStatus);
            }
        }
        return VERDICT;
    }
    
    public static void VerdictJudge() {
        String language = "";
        long timeInMillis = 1000;
        String problemId = "";
        String username = "";
        String testout = "";
        String inputFile = "";
        String VERDICT = "Error";

        while(!Server.submissionsQueue.isEmpty()){
            System.out.println("entered 1 while");
            try{
                SubNode sub;
                sub = Server.submissionsQueue.peek();
                language = sub.getLanguage();
                timeInMillis = sub.getTimeInMillis();
                problemId = sub.getProblemId();
                username = sub.getUsername();
                testout = sub.getTestout();
                inputFile = sub.getInputFile();

                VERDICT = run(language, timeInMillis, problemId, username, testout, inputFile);
                
                try{
                    System.out.println("Updating Leaderboard...");
                    Contestant c = Server.leaderboard.getContestant(username);
                    if(VERDICT.equals(verdict.RIGHT)){
                        c.setScore(c.getScore() + 1);
                        c.setAC(c.getAC() + 1);
                    }
                    else if(VERDICT.equals(verdict.WRONG)){
                        c.setWA(c.getWA() + 1);
                    }
                    else if(VERDICT.equals(verdict.TLE)){
                        c.setTLE(c.getTLE() + 1);
                    }
                    else if(VERDICT.equals(verdict.RUN_ERROR)){
                        c.setRE(c.getRE() + 1);
                    }
                    else if(VERDICT.equals(verdict.COMPILE_ERROR)){
                        c.setCE(c.getCE() + 1);
                    }
                    else if(VERDICT.equals(verdict.COMPILE_SUCCESS)){
                        c.setCS(c.getCS() + 1);
                    }
                    else if(VERDICT.equals(verdict.RUN_SUCCESS)){
                        c.setRS(c.getRS() + 1);
                    }
                    else{
                        c.setScore(c.getScore() + 0);
                    }
                    c.saveContestant();
                }catch(Exception e){
                    System.out.println("Error in updating leaderboard" + e);
                }
            }
            catch(Exception e){
                System.out.println("Error in VerdictJudge" + e);
            }
            finally{
                System.out.println("Verdict: " + VERDICT);
                try {
                    System.out.println("Sending verdict to user...");
                    Socket clientSocket = Server.connections.get(username);
                    out = new DataOutputStream(clientSocket.getOutputStream());
                    out.writeUTF("Verdict: " + VERDICT);
                    out.flush();
                } catch (Exception e) {
                    System.out.println("Error in sending verdict to client" + e);
                }
            }

            Server.leaderboard.sortLeaderboard();
            Server.submissionsQueue.dequeue();
        }
        // check if the queue has any submissions and then run the verdict judge
        while(true){
            if (!Server.submissionsQueue.isEmpty()) {
                System.out.println("entered 2 while");
                VerdictJudge();
                break;
            }
        }
    }
}