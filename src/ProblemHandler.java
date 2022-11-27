import java.io.File;

public class ProblemHandler {
    //create a bst to store the problems

    private ProblemNode root;
    private int size;

    public ProblemHandler() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addProblem(ProblemNode problem) {
        if (root == null) {
            root = problem;
            size++;
            return;
        }
        ProblemNode current = root;
        while (true) {
            if (problem.getProblemId().compareTo(current.getProblemId()) < 0) {
                if (current.left == null) {
                    current.left = problem;
                    size++;
                    return;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = problem;
                    size++;
                    return;
                }
                current = current.right;
            }
        }
    }

    public ProblemNode getProblem(String problemId) {
        ProblemNode current = root;
        while (current != null) {
            if (problemId.compareTo(current.getProblemId()) == 0) {
                return current;
            }
            if (problemId.compareTo(current.getProblemId()) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null;
    }

    // delete a problem recursively
    public void deleteProblem(String problemId) {
        root = deleteProblem(root, problemId);
    }

    private ProblemNode deleteProblem(ProblemNode node, String problemId) {
        if (node == null) {
            return null;
        }
        if (problemId.compareTo(node.getProblemId()) < 0) {
            node.left = deleteProblem(node.left, problemId);
        } else if (problemId.compareTo(node.getProblemId()) > 0) {
            node.right = deleteProblem(node.right, problemId);
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else {
                ProblemNode temp = node;
                node = minNode(temp.right);
                node.right = deleteMin(temp.right);
                node.left = temp.left;
            }
        }
        //delete the file
        File file = new File("problems/" + problemId);
        file.delete();
        return node;
    }

    // delete the minimum node
    private ProblemNode deleteMin(ProblemNode node) {
        if (node.left == null) {
            return node.right;
        }
        node.left = deleteMin(node.left);
        return node;
    }

    // find the minimum node

    private ProblemNode minNode(ProblemNode node) {
        if (node.left == null) {
            return node;
        }
        return minNode(node.left);
    }

    // update a problem

    public void updateProblem(String problemId, String problemName, String problemDescription, String inputFile, String testout, int timeInMillis, int memoryInBytes) {
        ProblemNode current = root;
        while (current != null) {
            if (problemId.compareTo(current.getProblemId()) == 0) {
                current.setProblemName(problemName);
                current.setProblemDescription(problemDescription);
                current.setInputFile(inputFile);
                current.setTestout(testout);
                current.setTimeInMillis(timeInMillis);
                current.setMemoryInBytes(memoryInBytes);
                return;
            }
            if (problemId.compareTo(current.getProblemId()) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
    }

    public void printProblem() {
        printProblem(root);
    }

    private void printProblem(ProblemNode node) {
        if (node == null) {
            return;
        }
        printProblem(node.left);
        System.out.println(node.getProblemId() + " " + node.getProblemName() + " " + node.getProblemDescription() + " " + node.getInputFile() + " " + node.getTestout() + " " + node.getTimeInMillis() + " " + node.getMemoryInBytes());
        printProblem(node.right);
    }

    //load the problem from the file
    public void loadProblems() {
        File folder = new File("problems");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    ProblemNode problem = new ProblemNode();
                    problem.loadProblems(listOfFile.getName());
                    addProblem(problem);
                }
            }
        }
    }

    public String printProblems() {
        return printProblems(root);
    }

    private String printProblems(ProblemNode node) {
        if (node == null) {
            return "";
        }
        String result = "";
        result += printProblems(node.left);
        result += "\nProblem ID: " + node.getProblemId() + "\nProblem Name: " + node.getProblemName() + "\nProblem Description: " + node.getProblemDescription() + "\nTime Limit: " + node.getTimeInMillis() + "\nMemory Limit: " + node.getMemoryInBytes() + "\n";
        result += printProblems(node.right);
        return result;
    }
        
}
