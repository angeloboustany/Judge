package submissions;

import java.util.Scanner;

public class PAUElsa {
    public static void main(String[] args) {
        //read fron std input
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int n = sc.nextInt();
            System.out.println(n*2);
        }
        sc.close();
    }
}