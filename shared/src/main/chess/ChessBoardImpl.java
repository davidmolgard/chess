package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessPiece.PieceType.PAWN;

public class ChessBoardImpl implements ChessBoard{
    ChessPiece[][] chessBoard = new ChessPiece[8][8];
    public ChessBoardImpl() {
        resetBoard();
    }

    public ChessBoardImpl(ChessBoard board) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                addPiece(new ChessPositionImpl(i,j), board.getPiece(new ChessPositionImpl(i,j)));
            }
        }
    }
    private static class EnPassantPossible {
        ChessPosition positionOfPawn;
        ChessPosition attackingPos;
        boolean isPossible = false;
        boolean oneTurnDelay = false;
        public EnPassantPossible() {

        }
        public void setEnPassantPossible(ChessPosition pos, ChessPosition attackingPos) {
            this.attackingPos = attackingPos;
            positionOfPawn = pos;
            isPossible = true;
            oneTurnDelay = false;
        }

        public boolean isPossible() {
            return isPossible;
        }
        public void oneTurnDelay() {
            oneTurnDelay = true;
        }

        public void setFalse() {
            isPossible = false;
        }
    }
    private EnPassantPossible enPassantPossible = new EnPassantPossible();
    @Override
    public boolean getEnPassantOneTurnDelay() {
        return enPassantPossible.oneTurnDelay;
    }
    @Override
    public void enPassantOneTurnDelay() {
        enPassantPossible.oneTurnDelay();
    }
    @Override
    public boolean enPassantPossible() {
        return enPassantPossible.isPossible();
    }
    @Override
    public void setEnPassantPositions(ChessPosition pos, ChessPosition attackPos) {
        enPassantPossible.setEnPassantPossible(pos, attackPos);
    }
    @Override
    public ChessMove getEnPassantMove() {
        return new ChessMoveImpl(enPassantPossible.positionOfPawn, enPassantPossible.attackingPos);
    }
    @Override
    public void makeMove(ChessPosition startPosition, ChessPosition endPosition) {
        ChessPiece piece = getPiece(startPosition);
        if ((piece.getPieceType() == PAWN) && (getPiece(endPosition) == null) && (startPosition.getColumn() != endPosition.getColumn())) {
            addPiece(new ChessPositionImpl(startPosition.getRow(), endPosition.getColumn()), null);
        }
        addPiece(startPosition, null);
        addPiece(endPosition, piece);
        if (enPassantPossible() && getEnPassantOneTurnDelay()) {
            enPassantPossible.setFalse();
        }
        else if (enPassantPossible() && !getEnPassantOneTurnDelay()) {
            enPassantOneTurnDelay();
        }
    }

    @Override
    public void makeMove(ChessMove move) {
        if (move.getPromotionPiece() == null) {
            makeMove(move.getStartPosition(), move.getEndPosition());
        }
        else {
            ChessPiece promotionPiece = new ChessPieceImpl(getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece());
            addPiece(move.getStartPosition(), null);
            addPiece(move.getEndPosition(), promotionPiece);
            if (enPassantPossible()) {
                enPassantPossible.setFalse();
            }
        }
    }

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        chessBoard[position.getColumn() - 1][position.getRow() - 1] = piece;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return chessBoard[position.getColumn() - 1][position.getRow() - 1];
    }

    @Override
    public void resetBoard() {
        for (int i = 0; i < 8; i++) {
            chessBoard[i][1] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, PAWN);
            chessBoard[i][6] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, PAWN);
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 2; j < 6; j++) {
                chessBoard[i][j] = null;
            }
        }
        chessBoard[0][0] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        chessBoard[7][0] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        chessBoard[0][7] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        chessBoard[7][7] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        chessBoard[1][0] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        chessBoard[6][0] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        chessBoard[1][7] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        chessBoard[6][7] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        chessBoard[2][0] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        chessBoard[5][0] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        chessBoard[2][7] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        chessBoard[5][7] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        chessBoard[3][0] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        chessBoard[3][7] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        chessBoard[4][0] = new ChessPieceImpl(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        chessBoard[4][7] = new ChessPieceImpl(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoardImpl that = (ChessBoardImpl) o;
        return (Arrays.deepEquals(chessBoard, that.chessBoard));
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(enPassantPossible);
        result = 31 * result + Arrays.deepHashCode(chessBoard);
        return result;
    }
}
