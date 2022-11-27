import java.nio.file.Files;
import java.nio.file.Paths;

public class ProblemNode {
    private String problemId;
    private String problemName;
    private String problemDescription;
    private String inputFile;
    private String testout;
    private int timeInMillis;
    private int memoryInBytes;
    public ProblemNode left;
    public ProblemNode right;

    public ProblemNode() {
    }

    public ProblemNode(String problemId, String problemName, String problemDescription, String inputFile, String testout, int timeInMillis, int memoryInBytes) {
        this.problemId = problemId;
        this.problemName = problemName;
        this.problemDescription = problemDescription;
        this.inputFile = inputFile;
        this.testout = testout;
        this.timeInMillis = timeInMillis;
        this.memoryInBytes = memoryInBytes;
        this.left = null;
        this.right = null;
    }

    //setters and getters

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getTestout() {
        return testout;
    }

    public void setTestout(String testout) {
        this.testout = testout;
    }

    public int getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(int timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public int getMemoryInBytes() {
        return memoryInBytes;
    }

    public void setMemoryInBytes(int memoryInBytes) {
        this.memoryInBytes = memoryInBytes;
    }

    //save the problem to a file 
    public void saveProblem() {
        String path = "problems";
        
        if(!Files.exists(Paths.get(path))) {
            try {
                Files.createDirectory(Paths.get(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Files.write(Paths.get(path + "/" + problemName + ".txt"), (
                    "Problem ID: " + problemId + "\n" +
                    "Problem Name: " + problemName + "\n" +
                    "Problem Description: " + problemDescription + "\n" +
                    "Input File: " + inputFile + "\n" +
                    "Testout: " + testout + "\n" +
                    "Time in Millis: " + timeInMillis + "\n" +
                    "Memory in Bytes: " + memoryInBytes + "\n"
            ).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // load the problem from a file
    public void loadProblems(String problemName) {
        String path = "problems";
        try {
            String[] data = Files.readAllLines(Paths.get(path + "/" + problemName)).toArray(new String[0]);
            this.problemId = data[0].split(": ")[1];
            this.problemName = data[1].split(": ")[1];
            this.problemDescription = data[2].split(": ")[1];
            this.inputFile = data[3].split(": ")[1];
            this.testout = data[4].split(": ")[1];
            this.timeInMillis = Integer.parseInt(data[5].split(": ")[1]);
            this.memoryInBytes = Integer.parseInt(data[6].split(": ")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //display the problem
    public String displayProblem() {
        return "Problem ID: " + problemId + "\n" +
                "Problem Name: " + problemName + "\n" +
                "Problem Description: " + problemDescription + "\n" +
                "Input File: " + inputFile + "\n" +
                "Testout: " + testout + "\n" +
                "Time in Millis: " + timeInMillis + "\n" +
                "Memory in Bytes: " + memoryInBytes + "\n";
    }
}
