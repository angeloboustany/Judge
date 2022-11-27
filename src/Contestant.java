import java.nio.file.Files;
import java.nio.file.Paths;

public class Contestant {

    private int id;
    private String name;
    private String email;
    private String username;
    private int score;
    private int numberOfincorrectAttempts;
    private int nbOfSolvedProblems;
    public Contestant next;
    public Contestant prev;

    public Contestant(){};

    public Contestant(String name, String email, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.id = generateId();
        next = null;
        prev = null;
    }

    public void numberOfincorrectAttempts() {
        numberOfincorrectAttempts++;
    }

    public void solvedProblem() {
        nbOfSolvedProblems++;
    }

    public int getNumberOfincorrectAttempts() {
        return numberOfincorrectAttempts;
    }

    public int getNbOfSolvedProblems() {
        return nbOfSolvedProblems;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public int getScore(){
        return score;
    }

    public int generateId() {
        return (int) (Math.random() * 1000)+1;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumberOfincorrectAttempts(int numberOfincorrectAttempts) {
        this.numberOfincorrectAttempts = numberOfincorrectAttempts;
    }

    public void setNbOfSolvedProblems(int nbOfSolvedProblems) {
        this.nbOfSolvedProblems = nbOfSolvedProblems;
    }
    
    public void saveContestant() {
        String path = "contestant" + "/" + username;
        
        if(!Files.exists(Paths.get(path))) {
            try {
                Files.createDirectory(Paths.get(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Files.write(Paths.get(path + "/" + username+"-info.txt"), (id + " " + username + " " + name + " " + email + " " + score + " " + numberOfincorrectAttempts + " " + nbOfSolvedProblems).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override

    public String toString() {
        return "Contestant{" + "id=" + id + ", name=" + name + ", email=" + email + ", username=" + username + '}';
    }

    public void loadContestant(String username) {
        String path = "contestant" + "/" + username;
        try {
            String[] data = Files.readAllLines(Paths.get(path + "/" + username+"-info.txt")).get(0).split(" ");
            id = Integer.parseInt(data[0]);
            this.username = data[1];
            name = data[2];
            email = data[3];
            score = Integer.parseInt(data[4]);
            numberOfincorrectAttempts = Integer.parseInt(data[5]);
            nbOfSolvedProblems = Integer.parseInt(data[6]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
