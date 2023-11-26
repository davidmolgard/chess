import RequestResultClasses.loginClasses.LoginRequest;
import RequestResultClasses.loginClasses.LoginResult;
import RequestResultClasses.registerClasses.RegisterRequest;
import RequestResultClasses.registerClasses.RegisterResult;
import com.mysql.cj.log.Log;

import java.util.Scanner;

public class Main {
    private static ServerFacade serverFacade = new ServerFacade();

    public static void main(String[] args) {
        preLogin();
    }

    private static void preLogin() {
        System.out.print("Welcome to chess. Type HELP to get started.\n");
        while (true) {
            boolean validInput = false;
            String errorMessage = "Please try again.";
            System.out.print("Input: ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.equals("QUIT")) {
                System.exit(0);
            }
            else if (line.equals("HELP")) {
                helpPreLogin();
                validInput = true;
            }
            String[] words = line.split(" ");
            if (words[0].equals("LOGIN")) {
                if (words.length < 3) {
                    errorMessage = "Not enough parameters.";
                }
                else {
                    LoginRequest loginRequest = new LoginRequest(words[1], words[2]);
                    LoginResult loginResult = serverFacade.login(loginRequest);
                    if (loginResult.getResponseCode() != 200) {
                        System.out.print(loginResult.getMessage());
                    } else {
                        postLogin(loginResult.getUsername(), loginResult.getAuthToken().getAuthToken());
                        System.out.print("Welcome to chess. Type HELP to get started.\n");
                        validInput = true;
                    }
                }
            }
            else if (words[0].equals("REGISTER")) {
                if (words.length < 4) {
                    errorMessage = "Not enough parameters.";
                }
                else {
                    RegisterRequest registerRequest = new RegisterRequest(words[1], words[2], words[3]);
                    RegisterResult registerResult = serverFacade.register(registerRequest);
                    if (registerResult.getResponseCode() != 200) {
                        System.out.print(registerResult.getMessage());
                    }
                    else {
                        postLogin(registerResult.getUsername(), registerResult.getAuthToken().getAuthToken());
                        System.out.print("Welcome to chess. Type HELP to get started.\n");
                        validInput = true;
                    }
                }
            }
            if (!validInput) {
                System.out.print("Invalid input. " + errorMessage + "\n");
                System.out.print("Input HELP for a list of options.\n");
            }
        }
    }

    private static void postLogin(String username, String authToken) {

    }

    private static void helpPreLogin() {
        System.out.print("Type an option EXACTLY as listed\n");
        System.out.print("--------------------------------------\n");
        System.out.print("HELP - displays available options\n");
        System.out.print("QUIT - exits the program\n");
        System.out.print("LOGIN <USERNAME> <PASSWORD> - login existing user\n");
        System.out.print("REGISTER <USERNAME> <PASSWORD> <EMAIL> - create new user\n");
    }
}
