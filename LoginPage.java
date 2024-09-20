//2)write a java program where a login page which will allow incorrect user id and password maximum 3 times
import java.util.Scanner;

public class LoginPage {
    public static void main(String[] args) {
        
        String correctUserID = "user123";
        String correctPassword = "pass123";

        Scanner sc = new Scanner(System.in);
        int attempts = 0;
        boolean loggedIn = false;

        while (attempts < 3) {
            System.out.print("Enter User ID: ");
            String userID = sc.nextLine();
            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            if (userID.equals(correctUserID) && password.equals(correctPassword)) {
                System.out.println("Login successful!");
                loggedIn = true;
                break;
            } else {
                attempts++;
                System.out.println("Incorrect User ID or Password. Attempts left: " + (3 - attempts));
            }
        }

        if (!loggedIn) {
            System.out.println("Maximum attempts exceeded. Access denied.");
        }

        sc.close();
    }
}
