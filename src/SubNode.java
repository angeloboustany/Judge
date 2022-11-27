public class SubNode {
    String problemId;
    String filePath;
    String language;
    String username;
    long timeInMillis;
    String testout;
    String inputFile;
    public SubNode next;
    public SubNode prev;

    public SubNode(){
        this.problemId = null;
        this.filePath = null;
        this.language = null;
        this.username = null;
        this.timeInMillis = 0;
        this.testout = null;
        this.inputFile = null;
        this.next = null;
        this.prev = null;
    };

    public SubNode(String problemId, String filePath, String language, String username, String testout, String inputFile, long timeInMillis) {
        this.problemId = problemId;
        this.filePath = filePath;
        this.next = null;
        this.language = language;
        this.username = username;
        this.timeInMillis = timeInMillis;
        this.testout = testout;
        this.inputFile = inputFile;
    }
    public String getProblemId() {
        return problemId;
    }
    public String getFilePath() {
        return filePath;
    }
    public String getLanguage() {
        return language;
    }
    public String getUsername() {
        return username;
    }
    public long getTimeInMillis() {
        return timeInMillis;
    }
    public void setNext(SubNode next) {
        this.next = next;
    }
    public SubNode getNext() {
        return next;
    }
    public String getTestout() {
        return testout;
    }
    public String getInputFile() {
        return inputFile;
    }
    public String toString() {
        return problemId + " " + filePath + " " + language + " " + username + " " + timeInMillis + " " + testout + " " + inputFile;
    }
}
