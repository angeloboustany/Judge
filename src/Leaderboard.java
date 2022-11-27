import java.io.File;

public class Leaderboard {

    //create a leaderboard linkedlist
    private Contestant head;
    private int size;

    public Leaderboard() {
        head = null;
        size = 0;
    }

    public void addContestant(Contestant contestant) {
        if (head == null) {
            head = contestant;
            size++;
        } else {
            Contestant current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = contestant;
            contestant.prev = current;
            size++;
        }
    }

    public void removeContestant(Contestant contestant) {
        if (head == null) {
            System.out.println("The list is empty");
        } else {
            Contestant current = head;
            while (current != null) {
                if (current == contestant) {
                    if (current.prev == null) {
                        head = current.next;
                        current.next.prev = null;
                        size--;
                    } else if (current.next == null) {
                        current.prev.next = null;
                        size--;
                    } else {
                        current.prev.next = current.next;
                        current.next.prev = current.prev;
                        size--;
                    }
                }
                current = current.next;
            }
            //delete the file
            File file = new File("contestant/" + contestant.getUsername());
            file.delete();
        }
    }

    public String printLeaderboard() {
        if (head == null) {
            System.out.println("The list is empty");
        } else {
            Contestant current = head;
            String leaderboard = "";
            while (current != null) {
                leaderboard += current.getUsername() + " " + current.getName() + " " + current.getScore() + " " + current.getNbOfSolvedProblems() + " " + current.getNumberOfincorrectAttempts() + " \n";
                current = current.next;
            }
            return leaderboard;
        }
        return null;
    }

    public void sortLeaderboard() {
        if (head == null) {
            System.out.println("The list is empty");
        } else {
            Contestant current = head;
            Contestant index = null;
            Contestant temp;
            while (current != null) {
                index = current.next;
                while (index != null) {
                    if (current.getScore() < index.getScore()) {
                        temp = current;
                        current = index;
                        index = temp;
                    }
                    index = index.next;
                }
                current = current.next;
            }
        }
    }

    public void loadLeaderboard() {
        File folder = new File("contestant");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isDirectory()) {
                Contestant contestant = new Contestant();
                contestant.loadContestant(listOfFiles[i].getName());
                addContestant(contestant);
                sortLeaderboard();
            }
        }
    }

    // search for a contestant if he exists
    public boolean searchContestant(String username) {
        if (head == null) {
            System.out.println("The list is empty");
        } else {
            Contestant current = head;
            while (current != null) {
                if (current.getUsername().equals(username)) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    // get a contestant if he exists
    public Contestant getContestant(String username) {
        if (head == null) {
            System.out.println("The list is empty");
        } else {
            Contestant current = head;
            while (current != null) {
                if (current.getUsername().equals(username)) {
                    return current;
                }
                current = current.next;
            }
        }
        return null;
    }

    //save the leaderboard
    public void saveLeaderboard() {
        if (head == null) {
            System.out.println("The list is empty");
        } else {
            Contestant current = head;
            while (current != null) {
                current.saveContestant();
                current = current.next;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Contestant getHead() {
        return head;
    }

    public void setHead(Contestant head) {
        this.head = head;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
