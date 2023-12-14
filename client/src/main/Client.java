import RequestResultClasses.createClasses.CreateRequest;
import RequestResultClasses.createClasses.CreateResult;
import RequestResultClasses.joinClasses.JoinRequest;
import RequestResultClasses.joinClasses.JoinResult;
import RequestResultClasses.listClasses.ListResult;
import RequestResultClasses.loginClasses.LoginRequest;
import RequestResultClasses.loginClasses.LoginResult;
import RequestResultClasses.logoutClasses.LogoutRequest;
import RequestResultClasses.logoutClasses.LogoutResult;
import RequestResultClasses.registerClasses.RegisterRequest;
import RequestResultClasses.registerClasses.RegisterResult;
import chess.ChessGame.TeamColor;
import chess.ChessMoveImpl;
import chess.ChessPositionImpl;
import models.AuthToken;
import models.Game;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class Client implements ServerMessageObserver {
    private static final ServerFacade serverFacade = new ServerFacade();
    private static final ArrayList<Game> games = new ArrayList<>();

    private final WebSocketFacade webSocketFacade;

    {
        try {
            webSocketFacade = new WebSocketFacade(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final String EMPTY = "   ";
    private static final String QUEEN = " Q ";
    private static final String KING = " K ";
    private static final String BISHOP = " B ";
    private static final String KNIGHT = " N ";
    private static final String ROOK = " R ";
    private static final String PAWN = " P ";

    private static int gameIndexUniversal;
    private static TeamColor playerColor = WHITE;

    public Client() {
    }

    public void preLogin() {
        System.out.print("Welcome to chess. Type HELP to get started.\n");
        while (true) {
            boolean validInput = false;
            String errorMessage = "Please try again.";
            System.out.print("Input: ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.equals("QUIT")) {
                System.exit(0);
            } else if (line.equals("HELP")) {
                helpPreLogin();
                validInput = true;
            }
            String[] words = line.split(" ");
            if (words[0].equals("LOGIN")) {
                if (words.length < 3) {
                    errorMessage = "Not enough parameters.";
                } else {
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
            } else if (words[0].equals("REGISTER")) {
                if (words.length < 4) {
                    errorMessage = "Not enough parameters.";
                } else {
                    RegisterRequest registerRequest = new RegisterRequest(words[1], words[2], words[3]);
                    RegisterResult registerResult = serverFacade.register(registerRequest);
                    if (registerResult.getResponseCode() != 200) {
                        System.out.print(registerResult.getMessage() + "\n");
                    } else {
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

    private void postLogin(String username, String authTokenString) {
        System.out.print("Logged in as " + username + "\n");
        System.out.print("Input HELP for a list of options.\n");
        AuthToken authToken = new AuthToken(authTokenString, username);
        while (true) {
            boolean validInput = false;
            String errorMessage = "Please try again.";
            System.out.print("Input: ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.equals("QUIT")) {
                System.exit(0);
            } else if (line.equals("HELP")) {
                helpPostLogin();
                validInput = true;
            } else if (line.equals("LOGOUT")) {
                LogoutRequest logoutRequest = new LogoutRequest(authToken);
                LogoutResult logoutResult = serverFacade.logout(logoutRequest);
                if (logoutResult.getResponseCode() == 200) {
                    System.out.print("Logged out.\n");
                    return;
                } else {
                    errorMessage = logoutResult.getMessage() + "\n";
                }
            } else if (line.equals("LIST")) {
                String listSuccess = populateGameArray(authToken);
                if (listSuccess.equals("OK")) {
                    validInput = true;
                    for (int i = 0; i < games.size(); i++) {
                        System.out.print(i + 1 + " " + games.get(i).getGameName() + " White Player: " + games.get(i).getWhiteUsername()
                                + " Black Player: " + games.get(i).getBlackUsername() + "\n");
                    }
                }
                else {
                    errorMessage = listSuccess + "\n";
                }
            }
            String[] words = line.split(" ");
            switch (words[0]) {
                case "CREATE" -> {
                    if (words.length < 2) {
                        errorMessage = "Not enough parameters.";
                    } else {
                        validInput = true;
                        CreateRequest createRequest = new CreateRequest(authToken, words[1]);
                        CreateResult createResult = serverFacade.create(createRequest);
                        if (createResult.getResponseCode() != 200) {
                            System.out.print(createResult.getMessage() + "\n");
                        } else {
                            ListResult listResult = serverFacade.list(authToken);
                            games.clear();
                            games.addAll(Arrays.asList(listResult.getGames()));
                            System.out.print("New game created\n");
                        }
                    }
                }
                case ("JOIN") -> {
                    if (words.length < 3) {
                        errorMessage = "Not enough parameters.";
                    } else {
                        String listSuccess = null;
                        if (games.isEmpty()) {
                            listSuccess = populateGameArray(authToken);
                        }
                        if (listSuccess == null || listSuccess.equals("OK")) {
                            TeamColor teamColor = WHITE;
                            if (words[2].equals("BLACK")) {
                                teamColor = TeamColor.BLACK;
                            }
                            int gameIndex = Integer.parseInt(words[1]);
                            gameIndex--;
                            int gameID = games.get(gameIndex).getGameID();
                            if (games.isEmpty()) {
                                errorMessage = "no games found.";
                            } else {
                                if (gameIndex < 0 || gameIndex >= games.size()) {
                                    errorMessage = "invalid game ID.";
                                } else {
                                    JoinRequest joinRequest = new JoinRequest(authToken, teamColor, gameID);
                                    JoinResult joinResult = serverFacade.join(joinRequest);
                                    if (joinResult.getResponseCode() != 200) {
                                        errorMessage = joinResult.getMessage();
                                    } else {
                                        playGame(authToken, username, gameID, gameIndex, teamColor);
                                        validInput = true;
                                    }
                                }
                            }
                        }
                        else {
                            errorMessage = listSuccess;
                        }
                    }
                }
                case "OBSERVE" -> {
                    if (words.length < 2) {
                        errorMessage = "Not enough parameters.";
                    } else {
                        String listSuccess = null;
                        if (games.isEmpty()) {
                            listSuccess = populateGameArray(authToken);
                        }
                        if (listSuccess == null || listSuccess.equals("OK")) {
                            int gameIndex = Integer.parseInt(words[1]);
                            gameIndex--;
                            int gameID = games.get(gameIndex).getGameID();
                            JoinRequest joinRequest = new JoinRequest(authToken, gameID);
                            JoinResult joinResult = serverFacade.join(joinRequest);
                            if (joinResult.getResponseCode() != 200) {
                                errorMessage = joinResult.getMessage();
                            } else {
                                observeGame(authToken, username, gameID, gameIndex);
                                validInput = true;
                            }
                        }
                        else {
                            errorMessage = listSuccess;
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

    private String populateGameArray(AuthToken authToken) {
        ListResult listResult = serverFacade.list(authToken);
        if (listResult.getResponseCode() == 200) {
            games.clear();
            games.addAll(Arrays.asList(listResult.getGames()));
            return "OK";
        }
        else {
            return listResult.getMessage();
        }
    }

    private void playGame(AuthToken authToken, String username, int gameID, int gameIndex, TeamColor color) {
        playerColor = color;
        gameIndexUniversal = gameIndex;
        webSocketFacade.joinPlayer(authToken.getAuthToken(), gameID, color);
        System.out.print("Joined game as " + username + "\n");
        System.out.println("Type HELP to see options.");
        label:
        while (true) {
            boolean validInput = false;
            String errorMessage = "Please try again.";
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            switch (line) {
                case "HELP":
                    helpPlayer();
                    validInput = true;
                    break;
                case "LEAVE":
                    webSocketFacade.leave(authToken.getAuthToken(), gameID);
                    return;
                case "RESIGN":
                    webSocketFacade.resign(authToken.getAuthToken(), gameID);
                    validInput = true;
                    break;
                case "REDRAW":
                    drawBoard(gameIndexUniversal, playerColor);
                    validInput = true;
                    break;
            }
            String[] words = line.split(" ");
            switch (words[0]) {
                case "MAKE" -> {
                    if (words.length < 4) {
                        errorMessage = "Not enough parameters.";
                    }
                    else {
                        if (words[1].equals("MOVE")) {
                            char[] pos1 = words[2].toCharArray();
                            char[] pos2 = words[3].toCharArray();
                            if (pos1.length == 2 && pos2.length == 2) {
                                int xpos1 = ((int) pos1[0] - 96);
                                int ypos1 = ((int) pos1[1] - 48);
                                int xpos2 = ((int) pos2[0] - 96);
                                int ypos2 = ((int) pos2[1] - 48);
                                if (xpos1 < 1 || ypos1 < 1 || xpos2 < 1 || ypos2 < 1 || xpos1 > 8 || ypos1 > 8 || xpos2 > 8 || ypos2 > 8) {
                                    errorMessage = "invalid positions.";
                                }
                                else {
                                    ChessPositionImpl position1 = new ChessPositionImpl(ypos1, xpos1);
                                    ChessPositionImpl position2 = new ChessPositionImpl(ypos2, xpos2);
                                    ChessMoveImpl move = new ChessMoveImpl(position1, position2);
                                    webSocketFacade.makeMove(authToken.getAuthToken(), gameID, move);
                                    validInput = true;
                                }
                            }
                        }
                    }
                }
                case "HIGHLIGHT" -> {
                    if (words.length < 2) {
                        errorMessage = "Not enough parameters.";
                    }
                    else {
                        char[] pos = words[2].toCharArray();
                        if (pos.length == 2) {
                            int xpos = ((int) pos[0] - 96);
                            int ypos = ((int) pos[1] - 48);
                            if (xpos < 1 || ypos < 1 || xpos > 8 || ypos > 8) {
                                errorMessage = "invalid position.";
                            }
                            else {
                                ChessPositionImpl position = new ChessPositionImpl(ypos, xpos);
                                highlightMoves(gameID, position);
                                validInput = true;
                            }
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

    private void observeGame(AuthToken authToken, String username, int gameID, int gameIndex) {
        gameIndexUniversal = gameIndex;
        webSocketFacade.joinObserver(authToken.getAuthToken(),gameID);
        System.out.println("Observing Game " + gameIndexUniversal);
        System.out.println("Type HELP to see options.");
        label:
        while (true) {
            boolean validInput = false;
            String errorMessage = "Please try again.";
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            switch (line) {
                case "HELP":
                    helpObserve();
                    validInput = true;
                    break;
                case "LEAVE":
                    webSocketFacade.leave(authToken.getAuthToken(), gameID);
                    return;
                case "REDRAW":
                    drawBoard(gameIndexUniversal, playerColor);
                    validInput = true;
                    break;
            }
            String[] words = line.split(" ");
            if (words[0].equals("HIGHLIGHT")) {
                if (words.length < 2) {
                    errorMessage = "Not enough parameters.";
                }
                else {
                    char[] pos = words[2].toCharArray();
                    if (pos.length == 2) {
                        int xpos = ((int) pos[0] - 96);
                        int ypos = ((int) pos[1] - 48);
                        if (xpos < 1 || ypos < 1 || xpos > 8 || ypos > 8) {
                            errorMessage = "invalid position.";
                        }
                        else {
                            ChessPositionImpl position = new ChessPositionImpl(ypos, xpos);
                            highlightMoves(gameID, position);
                            validInput = true;
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

    private static void helpObserve() {
        System.out.print("Type an option EXACTLY as listed\n");
        System.out.print("--------------------------------------\n");
        System.out.print("HELP - displays available options\n");
        System.out.print("LEAVE - ends observation\n");
        System.out.print("REDRAW - redraws chess board\n");
        System.out.print("HIGHLIGHT <PIECE POSITION> - highlights all legal moves of piece given\n");
        System.out.print("Input piece position in standard chess notation. i.e. a5\n");
    }

    private static void helpPlayer() {
        System.out.print("Type an option EXACTLY as listed\n");
        System.out.print("--------------------------------------\n");
        System.out.print("HELP - displays available options\n");
        System.out.print("LEAVE - leaves the game\n");
        System.out.print("MAKE MOVE <START POSITION> <END POSITION> - moves a piece\n");
        System.out.print("RESIGN - forfeits the game\n");
        System.out.print("REDRAW - redraws chess board\n");
        System.out.print("HIGHLIGHT <POSITION> - highlights all legal moves of piece at position\n");
        System.out.print("Input piece positions in standard chess notation. i.e. a5\n");
    }

    private static void highlightMoves(int gameID, ChessPositionImpl position) {

    }

    private static void drawBoard(int gameIndex, TeamColor color) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        if (color == BLACK) {
            for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
                if (boardRow == 0 || boardRow == BOARD_SIZE_IN_SQUARES - 1) {
                    printOutlineRow(out, color);
                } else {
                    printBoardRow(out, color, boardRow, gameIndex);
                }
            }
        } else {
            for (int boardRow = BOARD_SIZE_IN_SQUARES - 1; boardRow >= 0; boardRow--) {
                if (boardRow == 0 || boardRow == BOARD_SIZE_IN_SQUARES - 1) {
                    printOutlineRow(out, color);
                } else {
                    printBoardRow(out, color, boardRow, gameIndex);
                }
            }
        }
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void printOutlineRow(PrintStream out, TeamColor color) {
        setBoardOutlineColor(out);
        if (color == TeamColor.BLACK) {
            out.print("    h  g  f  e  d  c  b  a    ");
        } else {
            out.print("    a  b  c  d  e  f  g  h    ");
        }
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void printBoardRow(PrintStream out, TeamColor color, int row, int gameIndex) {
        int whiteSquare = 0;
        int currColor = row % 2;
        if (color == TeamColor.BLACK) {
            currColor = (currColor + 1) % 2;
        }
        if (color == WHITE) {
            for (int currSquare = 0; currSquare < BOARD_SIZE_IN_SQUARES; currSquare++) {
                if (currSquare == 0 || currSquare == BOARD_SIZE_IN_SQUARES - 1) {
                    setBoardOutlineColor(out);
                    out.print(" " + row + " ");
                } else {
                    if (currColor == whiteSquare) {
                        setWhiteSquare(out);
                        printPiece(out, row, currSquare, gameIndex);
                    } else {
                        setBlackSquare(out);
                        printPiece(out, row, currSquare, gameIndex);
                    }
                    currColor = (currColor + 1) % 2;
                }
            }
        } else {
            for (int currSquare = BOARD_SIZE_IN_SQUARES - 1; currSquare >= 0; currSquare--) {
                if (currSquare == 0 || currSquare == BOARD_SIZE_IN_SQUARES - 1) {
                    setBoardOutlineColor(out);
                    out.print(" " + row + " ");
                } else {
                    if (currColor == whiteSquare) {
                        setWhiteSquare(out);
                        printPiece(out, row, currSquare, gameIndex);
                    } else {
                        setBlackSquare(out);
                        printPiece(out, row, currSquare, gameIndex);
                    }
                    currColor = (currColor + 1) % 2;
                }
            }
        }
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private static void printPiece(PrintStream out, int row, int col, int gameIndex) {
        Game game = games.get(gameIndex);
        if (game.getGame().getBoard().getPiece(new ChessPositionImpl(row, col)) == null) {
            out.print(EMPTY);
        } else {
            if (game.getGame().getBoard().getPiece(new ChessPositionImpl(row, col)).getTeamColor() == WHITE) {
                setWhitePlayer(out);
            } else {
                setBlackPlayer(out);
            }
            switch (game.getGame().getBoard().getPiece(new ChessPositionImpl(row, col)).getPieceType()) {
                case KING -> out.print(KING);
                case QUEEN -> out.print(QUEEN);
                case BISHOP -> out.print(BISHOP);
                case KNIGHT -> out.print(KNIGHT);
                case ROOK -> out.print(ROOK);
                case PAWN -> out.print(PAWN);
                default -> out.print(EMPTY);
            }
        }

    }

    private static void setBoardOutlineColor(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setWhiteSquare(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
    }

    private static void setBlackSquare(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }

    private static void setWhitePlayer(PrintStream out) {
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlackPlayer(PrintStream out) {
        out.print(SET_TEXT_COLOR_BLUE);
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME ->  {
                games.set(gameIndexUniversal, message.getGame());
                drawBoard(gameIndexUniversal, playerColor);
            }
            case ERROR -> System.out.println(message.getErrorMessage());
            case NOTIFICATION -> System.out.print(message.getMessage());
        }
    }
}
