package DaoTests;

import chess.ChessBoardImpl;
import chess.ChessGame;
import chess.ChessGameImpl;
import chess.ChessMoveImpl;
import dataAccess.DataAccessException;
import dataAccess.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.dataAccess.GameDAO;
import server.models.Game;
import spark.utils.Assert;

import java.util.Arrays;

public class GameDaoTests {
    private static Database database = new Database();
    private GameDAO gameDAO = new GameDAO(database);
    private static String gameName = "NewGame";
    private static String whiteUsername = "user1";
    private static String blackUsername = "user2";
    private static Game game = new Game();

    private int gameID = 0;


    @BeforeAll
    public static void initialize() {
        ChessGameImpl chessGame = new ChessGameImpl();
        ChessBoardImpl chessBoard = new ChessBoardImpl();
        chessBoard.resetBoard();
        chessGame.setBoard(chessBoard);
        game.setGame(chessGame);
        game.setGameName(gameName);
        game.setWhiteUsername(whiteUsername);
        game.setBlackUsername(blackUsername);
    }

    @BeforeEach
    public void clear() {
        gameDAO.clearGames();
        game.setGameID(0);
    }

    @Test
    public void insertPositive() {
        initialize();
        try {
            gameID = gameDAO.insert(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Game testGame;
        try {
            testGame = gameDAO.find(gameID);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(testGame, "Game found was null");
        Assertions.assertEquals(gameID, testGame.getGameID(), "GameID did not match");
        Assertions.assertEquals(whiteUsername, testGame.getWhiteUsername(), "white username did not match");
        Assertions.assertEquals(blackUsername, testGame.getBlackUsername(), "black username did not match");
        Assertions.assertEquals(gameName, testGame.getGameName());
        ChessBoardImpl chessBoard = new ChessBoardImpl();
        chessBoard.resetBoard();
        ChessBoardImpl returnBoard = (ChessBoardImpl) testGame.getGame().getBoard();
        Assertions.assertEquals(chessBoard, returnBoard);
    }

    @Test
    public void insertNegative() {
        try {
            gameDAO.insert(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DataAccessException.class, ()-> gameDAO.insert(game), "adding game twice did not throw exception");
    }

    @Test
    public void findPositive() {
        try {
            gameID = gameDAO.insert(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Game gameFound = new Game();
        try {
            gameFound = gameDAO.find(gameID);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(gameFound, "Game not found");
        Assertions.assertEquals(game, gameFound, "found game not equal to game added");
    }

    @Test
    public void findNegative() {
        try {
            gameID = gameDAO.insert(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DataAccessException.class, ()-> gameDAO.find(gameID+1), "invalid gameID did not return DataAccessException");
        Assertions.assertThrows(DataAccessException.class, ()-> gameDAO.find(0), "invalid gameID did not return DataAccessException");
    }

    @Test
    public void claimSpotPositive() {
        game.setBlackUsername(null);
        game.setWhiteUsername(null);
        try {
            gameID = gameDAO.insert(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            gameDAO.claimSpot(gameID, whiteUsername, ChessGame.TeamColor.WHITE);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            gameDAO.claimSpot(gameID, blackUsername, ChessGame.TeamColor.BLACK);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String testName;
        try {
            testName = gameDAO.find(gameID).getWhiteUsername();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(whiteUsername, testName, "white user spot was not successfully claimed");
        try {
            testName = gameDAO.find(gameID).getBlackUsername();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(blackUsername, testName, "black user spot was not successfully claimed");

        game.setBlackUsername(blackUsername);
        game.setWhiteUsername(whiteUsername);
    }

    @Test
    public void claimSpotNegative() {
        try {
            gameID = gameDAO.insert(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DataAccessException.class, ()-> gameDAO.claimSpot(0, blackUsername, ChessGame.TeamColor.BLACK)
                , "invalid gameID did not return DataAccessException");
        Assertions.assertThrows(DataAccessException.class, ()-> gameDAO.claimSpot(gameID, whiteUsername, ChessGame.TeamColor.WHITE)
                , "trying to claim previously claimed spot did not throw DataAccessException");
        Assertions.assertThrows(DataAccessException.class, ()-> gameDAO.claimSpot(gameID, blackUsername, ChessGame.TeamColor.BLACK)
                , "trying to claim previously claimed spot did not throw DataAccessException");
    }

    @Test
    public void updateGamePositive() {
        try {
            gameID = gameDAO.insert(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        game.setGameName("NewName");
        game.setWhiteUsername("white");
        game.setBlackUsername("black");
        game.getGame().setBoard(new ChessBoardImpl());
        try {
            gameDAO.updateGame(gameID, game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Game testGame;
        try {
            testGame = gameDAO.find(gameID);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(testGame.getGameName(), game.getGameName(), "game name not updated");
        Assertions.assertEquals(testGame.getBlackUsername(), game.getBlackUsername(), "black username not updated");
        Assertions.assertEquals(testGame.getWhiteUsername(), game.getWhiteUsername(), "white username not updated");
        Assertions.assertEquals(testGame.getGame().getBoard(), game.getGame().getBoard(), "board not updated");
    }

    @Test
    public void updateGameNegative() {
        try {
            gameID = gameDAO.insert(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DataAccessException.class, ()->gameDAO.updateGame(gameID+1, game), "Invalid gameID did not throw DataAccessException");
    }

    @Test
    public void findAllPositive() {
        Game[] gamesArray = new Game[3];
        Game game2 = new Game();
        game2.setGame(new ChessGameImpl());
        game2.setGameName("2");
        Game game3 = new Game();
        game3.setGameName("3");
        game3.setBlackUsername("black3");
        game3.setWhiteUsername("white3");
        gamesArray[0] = game;
        gamesArray[1] = game2;
        gamesArray[2] = game3;
        try {
            gameDAO.insert(game);
            gameDAO.insert(game2);
            gameDAO.insert(game3);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Game[] findAllArray = gameDAO.findAll();
        Assertions.assertNotNull(findAllArray, "findall returned null");
        Assertions.assertEquals(3, findAllArray.length, "find all did not find correct number of games");
        for (int i=0; i < 3; i++) {
            Assertions.assertEquals(gamesArray[i], findAllArray[i], "difference in game " + i+1);
        }
    }

    @Test
    public void findAllNegative() {
        gameDAO.clearGames();
        Assertions.assertNull(gameDAO.findAll(), "found games when no games added");
    }

    @Test
    public void removeGamePositive() {
        try {
            gameID = gameDAO.insert(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            gameDAO.removeGame(gameID);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DataAccessException.class, ()->gameDAO.find(gameID), "game not properly removed");
    }

    @Test void removeGameNegative() {
        try {
            gameID = gameDAO.insert(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(DataAccessException.class, ()->gameDAO.removeGame(gameID+1), "invalid gameID did not throw DataAccessException");
        Assertions.assertThrows(DataAccessException.class, ()->gameDAO.removeGame(0), "invalid gameID did not throw DataAccessException");
    }

    @Test
    public void clearGames() {
        int gameID = 0;
        try {
            gameID = gameDAO.insert(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        gameDAO.clearGames();
        int finalGameID = gameID;
        Assertions.assertThrows(DataAccessException.class, ()-> gameDAO.find(finalGameID), "Games remain in database after clear");
    }
}
