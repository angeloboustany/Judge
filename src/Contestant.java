import java.nio.file.Files;
import java.nio.file.Paths;

public class Contestant {

    private int id;
    private String name;
    private String email;
    private String username;
    private int score;
    private int WA;
    private int AC;
    private int TLE;
    private int CE;
    private int RE;
    private int CS;
    private int RS;
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

    public int getNbOfSolvedProblems() {
        return AC;
    }

    public int getNumberOfincorrectAttempts() {
        return WA + TLE + CE + RE + CS + RS;
    }

    public void setWA(int WA) {
        this.WA = WA;
    }

    public void setAC(int AC) {
        this.AC = AC;
    }

    public void setTLE(int TLE) {
        this.TLE = TLE;
    }

    public void setCE(int CE) {
        this.CE = CE;
    }

    public void setRE(int RE) {
        this.RE = RE;
    }

    public void setCS(int CS) {
        this.CS = CS;
    }

    public void setRS(int RS) {
        this.RS = RS;
    }

    public int getWA() {
        return WA;
    }

    public int getAC() {
        return AC;
    }

    public int getTLE() {
        return TLE;
    }

    public int getCE() {
        return CE;
    }

    public int getRE() {
        return RE;
    }

    public int getCS() {
        return CS;
    }

    public int getRS() {
        return RS;
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
            Files.write(Paths.get(path + "/" + username+"-info.txt"), (
                "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Username: " + username + "\n" +
                "Score: " + score + "\n" +
                "Number of solved problems: " + AC + "\n" +
                "Number of incorrect attempts: " + (WA + TLE + CE + RE + CS + RS) + "\n" +
                "Number of WA: " + WA + "\n" +
                "Number of AC: " + AC + "\n" +
                "Number of TLE: " + TLE + "\n" +
                "Number of CE: " + CE + "\n" +
                "Number of RE: " + RE + "\n" +
                "Number of CS: " + CS + "\n" +
                "Number of RS: " + RS + "\n"
            ).getBytes());
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
            String[] data = Files.readAllLines(Paths.get(path + "/" + username+"-info.txt")).toArray(new String[0]);
            this.name = data[0].split(": ")[1];
            this.email = data[1].split(": ")[1];
            this.username = data[2].split(": ")[1];
            this.score = Integer.parseInt(data[3].split(": ")[1]);
            this.AC = Integer.parseInt(data[4].split(": ")[1]);
            this.WA = Integer.parseInt(data[5].split(": ")[1]);
            this.TLE = Integer.parseInt(data[6].split(": ")[1]);
            this.CE = Integer.parseInt(data[7].split(": ")[1]);
            this.RE = Integer.parseInt(data[8].split(": ")[1]);
            this.CS = Integer.parseInt(data[9].split(": ")[1]);
            this.RS = Integer.parseInt(data[10].split(": ")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String printProfile(){
        return "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Username: " + username + "\n" +
                "Score: " + score + "\n" +
                "Number of solved problems: " + AC + "\n" +
                "Number of incorrect attempts: " + (WA + TLE + CE + RE + CS + RS) + "\n" +
                "Number of WA: " + WA + "\n" +
                "Number of AC: " + AC + "\n" +
                "Number of TLE: " + TLE + "\n" +
                "Number of CE: " + CE + "\n" +
                "Number of RE: " + RE + "\n" +
                "Number of CS: " + CS + "\n" +
                "Number of RS: " + RS + "\n";
    }
}
