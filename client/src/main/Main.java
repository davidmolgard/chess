import RequestResultClasses.createClasses.CreateRequest;
import RequestResultClasses.createClasses.CreateResult;
import RequestResultClasses.joinClasses.JoinRequest;
import RequestResultClasses.joinClasses.JoinResult;
import RequestResultClasses.listClasses.ListRequest;
import RequestResultClasses.listClasses.ListResult;
import RequestResultClasses.loginClasses.LoginRequest;
import RequestResultClasses.loginClasses.LoginResult;
import RequestResultClasses.logoutClasses.LogoutRequest;
import RequestResultClasses.logoutClasses.LogoutResult;
import RequestResultClasses.registerClasses.RegisterRequest;
import RequestResultClasses.registerClasses.RegisterResult;
import chess.ChessGame;
import com.mysql.cj.log.Log;
import models.AuthToken;

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
                        System.out.print(loginResult.getMessage() + "\n");
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
                        System.out.print(registerResult.getMessage() + "\n");
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
    private static void helpPreLogin() {
        System.out.print("Type an option EXACTLY as listed\n");
        System.out.print("--------------------------------------\n");
        System.out.print("HELP - displays available options\n");
        System.out.print("QUIT - exits the program\n");
        System.out.print("LOGIN <USERNAME> <PASSWORD> - login existing user\n");
        System.out.print("REGISTER <USERNAME> <PASSWORD> <EMAIL> - create new user\n");
    }
    private static void postLogin(String username, String authTokenString) {
        System.out.print("Logged in as " + username + "\n");
        System.out.print("Input HELP for a list of options.\n");
        AuthToken authToken = new AuthToken(authTokenString, username);
        while(true) {
            boolean validInput = false;
            String errorMessage = "Please try again.";
            System.out.print("Input: ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.equals("QUIT")) {
                System.exit(0);
            }
            else if (line.equals("HELP")) {
                helpPostLogin();
                validInput = true;
            }
            else if (line.equals("LOGOUT")) {
                LogoutRequest logoutRequest = new LogoutRequest(authToken);
                LogoutResult logoutResult = serverFacade.logout(logoutRequest);
                System.out.print("Logged out.\n");
                return;
            }
            else if (line.equals("LIST")) {
                ListRequest listRequest = new ListRequest(authToken);
                ListResult listResult = serverFacade.list(authToken);
                validInput = true;
                if (listResult.getResponseCode() != 200) {
                    System.out.print(listResult.getMessage() + "\n");
                }
            }
            String[] words = line.split(" ");
            switch (words[0]) {
                case "CREATE" -> {
                    if (words.length < 2) {
                        errorMessage = "Not enough parameters.";
                    }
                    else {
                        validInput = true;
                        CreateRequest createRequest = new CreateRequest(authToken, words[1]);
                        CreateResult createResult = serverFacade.create(createRequest);
                        if (createResult.getResponseCode() != 200) {
                            System.out.print(createResult.getMessage() + "\n");
                        }
                        else {
                            System.out.print("New game created with game ID: " + createResult.getGameID() + "\n");
                        }
                    }
                }
                case ("JOIN") -> {
                    if (words.length < 3) {
                        errorMessage = "Not enough parameters.";
                    }
                    else {
                        validInput = true;
                        ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
                        if (words[2].equals("BLACK")) {
                            teamColor = ChessGame.TeamColor.BLACK;
                        }
                        int gameID = Integer.parseInt(words[1]);
                        JoinRequest joinRequest = new JoinRequest(authToken, teamColor, gameID);
                        JoinResult joinResult = serverFacade.join(joinRequest);
                        if (joinResult.getResponseCode() != 200) {
                            System.out.print(joinResult.getMessage() + "\n");
                        }
                        else {
                            playGame(authToken, username, gameID);
                        }
                    }
                }
                case "OBSERVE" -> {
                    if (words.length < 2) {
                        errorMessage = "Not enough parameters.";
                    }
                    else {
                        validInput = true;
                        int gameID = Integer.parseInt(words[1]);
                        JoinRequest joinRequest = new JoinRequest(authToken, gameID);
                        JoinResult joinResult = serverFacade.join(joinRequest);
                        if (joinResult.getResponseCode() != 200) {
                            System.out.print(joinResult.getMessage() + "\n");
                        }
                        else {
                            observeGame(authToken, username, gameID);
                        }
                    }
                }
            }
            if (!validInput) {
                System.out.print("Invalid input. " + errorMessage + "\n");
                System.out.print("Input HELP for a list of options.\n");
            }
        }
    }

   private static void helpPostLogin() {
       System.out.print("Type an option EXACTLY as listed\n");
       System.out.print("--------------------------------------\n");
       System.out.print("HELP - displays available options\n");
       System.out.print("QUIT - exits the program\n");
       System.out.print("LOGOUT - logs out user\n");
       System.out.print("LIST - lists all games with ID value\n");
       System.out.print("CREATE <NAME> - creates new game with NAME\n");
       System.out.print("JOIN <ID> [WHITE|BLACK] - join game as player with given color\n");
       System.out.print("OBSERVE <ID> - join game as observer\n");
   }

   private static void playGame(AuthToken authToken, String username, int gameID) {
        System.out.print("Joined game " + gameID + " as " + username + "\n");

   }

   private static void observeGame(AuthToken authToken, String username, int gameID) {
        playGame(authToken, username, gameID);
   }
}
