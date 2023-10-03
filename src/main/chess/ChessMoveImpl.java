package chess;

import java.util.Objects;

public class ChessMoveImpl implements ChessMove{
    ChessPosition startPosition = null;
    ChessPosition endPosition = null;
    ChessPiece.PieceType promotionPiece = null;
    public ChessMoveImpl(ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionPiece){
        startPosition = start;
        endPosition = end;
        this.promotionPiece = promotionPiece;
    }

    public ChessMoveImpl(ChessPosition start, ChessPosition end){
        startPosition = start;
        endPosition = end;
        promotionPiece = null;
    }
    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImpl chessMove = (ChessMoveImpl) o;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }
}
