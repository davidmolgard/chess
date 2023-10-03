package chess;

import java.util.Collection;
import java.util.ArrayList;

public class ChessGameImpl implements ChessGame{
    TeamColor teamTurn = TeamColor.WHITE;
    ChessBoard board = new ChessBoardImpl();
    public ChessGameImpl() {
        board.resetBoard();
    }
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> validMoveArray = new ArrayList<>();
        if (board.getPiece(startPosition) == null) { return validMoveArray;} //return empty collection
        return board.getPiece(startPosition).pieceMoves(board, startPosition);
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Not Valid Move");
        }
        else {
            board.makeMove(move.getStartPosition(), move.getEndPosition());
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) { return false;}
        return !kingHasValidMoves(teamColor);
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) { return false;}
        return !kingHasValidMoves(teamColor);
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean kingHasValidMoves(TeamColor teamColor) {
        return false;
    }
}
