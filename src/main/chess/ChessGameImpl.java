package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

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
        if (!(validMoves(move.getStartPosition()).contains(move))) { // || (board.getPiece(move.getStartPosition()).getTeamColor() != teamTurn)
            throw new InvalidMoveException("Not Valid Move");
        }
        else {
            board.makeMove(move.getStartPosition(), move.getEndPosition());
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        return !checkThreats(teamColor).isEmpty();
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) { return false;}
        if (kingSurroundingsNotUnderAttack(teamColor)) { return false;}
        return true;
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
        ChessPosition kingPos = findKingPos(teamColor);
        if (kingPos == null) {
            return false;
        }
        return !validMoves(kingPos).isEmpty();
    }

    @Override
    public ChessPosition findKingPos(TeamColor teamColor) {
        ChessPosition testPos = new ChessPositionImpl(0,0);
        ChessPosition kingPos = new ChessPositionImpl(-1,-1);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                testPos.setPosition(i,j);
                if (board.getPiece(testPos) != null) {
                    if ((board.getPiece(testPos).getPieceType() == ChessPiece.PieceType.KING) && (board.getPiece(testPos).getTeamColor() == teamColor)) {
                        kingPos.setPosition(i, j);
                        return kingPos;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ArrayList<ChessMove> checkThreats(TeamColor teamColor) {
        ArrayList<ChessMove> checkThreats = new ArrayList<>();
        ChessPosition kingPos = findKingPos(teamColor);
        ChessPosition testPos = new ChessPositionImpl(0,0);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                testPos.setPosition(i,j);
                if (board.getPiece(testPos) != null) {
                    if (board.getPiece(testPos).getTeamColor() != teamColor) {
                        Collection<ChessMove> validMoves = validMoves(testPos);
                        for (ChessMove testMove : validMoves) {
                            if ((testMove.getEndPosition().getRow() == kingPos.getRow()) && (testMove.getEndPosition().getColumn() == kingPos.getColumn())) {
                                checkThreats.add(testMove);
                            }
                        }
                    }
                }
            }
        }
        return checkThreats;
    }

    @Override
    public boolean kingSurroundingsNotUnderAttack(TeamColor teamColor) {
        if (!kingHasValidMoves(teamColor)) {
            return false;
        }
        ChessPosition kingPos = findKingPos(teamColor);
        Collection<ChessMove> kingMoves = validMoves(kingPos);
        ArrayList<ChessPosition> kingSurroundings = new ArrayList<>();
        for (ChessMove move : kingMoves) {
            kingSurroundings.add(move.getEndPosition());
        }
        ArrayList<ChessPosition> kingSurroundingsCopy = new ArrayList<>(kingSurroundings);
        ChessPosition testPos = new ChessPositionImpl(0,0);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                testPos.setPosition(i,j);
                if (board.getPiece(testPos) != null) {
                    if (board.getPiece(testPos).getTeamColor() != teamColor) {
                        Collection<ChessMove> validMoves = validMoves(testPos);
                        if (board.getPiece(testPos).getPieceType() == ChessPiece.PieceType.PAWN) {
                            ChessPosition testPos1 = new ChessPositionImpl(testPos.getRow(), testPos.getColumn());
                            if (board.getPiece(testPos).getTeamColor() == TeamColor.BLACK) {
                                testPos1.setPosition(testPos1.getRow()-1, testPos1.getColumn());
                                if (i > 1) {
                                    if (j > 1) {
                                        testPos1.setPosition(testPos1.getRow(), testPos1.getColumn()-1);
                                        if (board.getPiece(testPos1) == null) {
                                            validMoves.add(new ChessMoveImpl(testPos, testPos1));
                                        }
                                        testPos1.setPosition(testPos1.getRow(), testPos1.getColumn()+1);
                                    }
                                    if (j < 8) {
                                        testPos1.setPosition(testPos1.getRow(), testPos1.getColumn()+1);
                                        if (board.getPiece(testPos1) == null) {
                                            validMoves.add(new ChessMoveImpl(testPos, testPos1));
                                        }
                                    }
                                }
                            }
                            else {
                                testPos1.setPosition(testPos1.getRow()+1, testPos1.getColumn());
                                if (i < 8) {
                                    if (j > 1) {
                                        testPos1.setPosition(testPos1.getRow(), testPos1.getColumn()-1);
                                        if (board.getPiece(testPos1) == null) {
                                            validMoves.add(new ChessMoveImpl(testPos, testPos1));
                                        }
                                        testPos1.setPosition(testPos1.getRow(), testPos1.getColumn()+1);
                                    }
                                    if (j < 8) {
                                        testPos1.setPosition(testPos1.getRow(), testPos1.getColumn()+1);
                                        if (board.getPiece(testPos1) == null) {
                                            validMoves.add(new ChessMoveImpl(testPos, testPos1));
                                        }
                                        testPos1.setPosition(testPos1.getRow(), testPos1.getColumn()-1);
                                    }
                                }
                            }
                        }
                        for (ChessMove testMove : validMoves) {
                            for (ChessPosition surroundingPos : kingSurroundingsCopy) {
                                if ((testMove.getEndPosition().getRow() == surroundingPos.getRow()) && (testMove.getEndPosition().getColumn() == surroundingPos.getColumn())) {
                                    kingSurroundings.remove(surroundingPos);
                                    if (kingSurroundings.isEmpty()) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
