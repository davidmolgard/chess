package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessPieceImpl implements ChessPiece{
    private ChessGame.TeamColor teamColor;
    private PieceType pieceType;
    public ChessPieceImpl(ChessGame.TeamColor teamColor, PieceType pieceType) {
        this.teamColor = teamColor;
        this.pieceType = pieceType;
    }
    public ChessPieceImpl() {
        teamColor = null;
        pieceType = null;
    }
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        if (board.getPiece(startPosition) == null) { return validMoves;}
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        ChessGame.TeamColor color = board.getPiece(startPosition).getTeamColor();
        switch(board.getPiece(startPosition).getPieceType()) {
            case KING:
                int[][] edgeTest = new int[3][3];
                if (row == 1) {
                    for (int i = 0; i <= 2; i++) {
                        edgeTest[2][i] = -1;
                    }
                }
                else if (row == 8) {
                    for (int i = 0; i <= 2; i++) {
                        edgeTest[0][i] = -1;
                    }
                }
                if (col == 1) {
                    for (int i = 0; i <= 2; i++) {
                        edgeTest[i][0] = -1;
                    }
                }
                else if (col == 8) {
                    for (int i = 0; i <= 2; i++) {
                        edgeTest[i][2] = -1;
                    }
                }
                edgeTest[1][1] = -1;
                for (int i = 0; i < 3; i++) {
                    int currRow = ((row+1) - i);
                    for (int j = 0; j < 3; j++) {
                        int currCol = ((col-1) + j);
                        if (edgeTest[i][j] != -1) {
                            ChessPosition testPos = new ChessPositionImpl(currRow, currCol);
                            if (board.getPiece(testPos) == null) {
                                validMoves.add(new ChessMoveImpl(startPosition, testPos));
                            }
                            else if (board.getPiece(testPos).getTeamColor() != color) {
                                validMoves.add(new ChessMoveImpl(startPosition, testPos));
                            }
                        }
                    }
                }
                break;
            case QUEEN:
                validMoves.addAll(bishopMoves(board, startPosition));
                validMoves.addAll(rookMoves(board, startPosition));
                break;
            case BISHOP:
                validMoves = bishopMoves(board, startPosition);
                break;
            case KNIGHT: if (color == ChessGame.TeamColor.WHITE) {

            }
            else {

            }
                break;
            case ROOK:
                validMoves = rookMoves(board, startPosition);
                break;
            case PAWN: if (color == ChessGame.TeamColor.WHITE) {
                if (row < 7) {
                    if (board.getPiece(new ChessPositionImpl(row+1,col)) == null) {
                        validMoves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(row+1,col), null));
                    }
                    if (col > 1) {
                        ChessPositionImpl testPos = new ChessPositionImpl(row+1, col-1);
                        if ((board.getPiece(testPos) != null) && (board.getPiece(testPos).getTeamColor() == ChessGame.TeamColor.BLACK)) {
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, null));
                        }
                    }
                    if (col < 8) {
                        ChessPositionImpl testPos = new ChessPositionImpl(row+1, col+1);
                        if ((board.getPiece(testPos) != null) && (board.getPiece(testPos).getTeamColor() == ChessGame.TeamColor.BLACK)) {
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, null));
                        }
                    }
                }
                if (row == 2) {
                    ChessPositionImpl testPos = new ChessPositionImpl(row+2, col);
                    if ((board.getPiece(testPos) == null) && (board.getPiece(new ChessPositionImpl(row-1, col)) == null)) {
                        validMoves.add(new ChessMoveImpl(startPosition, testPos, null));
                    }
                }
                if (row == 7) {
                    if (board.getPiece(new ChessPositionImpl(row+1,col)) == null) {
                        validMoves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(row+1,col), ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(row+1,col), ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(row+1,col), ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(row+1,col), ChessPiece.PieceType.BISHOP));
                    }
                    if (col > 1) {
                        ChessPositionImpl testPos = new ChessPositionImpl(row+1, col-1);
                        if ((board.getPiece(testPos) != null) && (board.getPiece(testPos).getTeamColor() == ChessGame.TeamColor.BLACK)) {
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.BISHOP));
                        }
                    }
                    if (col < 8) {
                        ChessPositionImpl testPos = new ChessPositionImpl(row+1, col+1);
                        if ((board.getPiece(testPos) != null) && (board.getPiece(testPos).getTeamColor() == ChessGame.TeamColor.BLACK)) {
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.BISHOP));
                        }
                    }
                }
            }
            else {
                if (row > 2) {
                    ChessPositionImpl testPos1 = new ChessPositionImpl(row-1, col);
                    if (board.getPiece(testPos1) == null) {
                        validMoves.add(new ChessMoveImpl(startPosition, testPos1, null));
                    }
                    if (col > 1) {
                        ChessPositionImpl testPos = new ChessPositionImpl(row-1, col-1);
                        if ((board.getPiece(testPos) != null) && (board.getPiece(testPos).getTeamColor() == ChessGame.TeamColor.WHITE)) {
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, null));
                        }
                    }
                    if (col < 8) {
                        ChessPositionImpl testPos = new ChessPositionImpl(row-1, col+1);
                        if ((board.getPiece(testPos) != null) && (board.getPiece(testPos).getTeamColor() == ChessGame.TeamColor.WHITE)) {
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, null));
                        }
                    }
                }
                if (row == 7) {
                    ChessPositionImpl testPos = new ChessPositionImpl(row-2, col);
                    if ((board.getPiece(testPos) == null) && (board.getPiece(new ChessPositionImpl(row-1, col)) == null)) {
                        validMoves.add(new ChessMoveImpl(startPosition, testPos, null));
                    }
                }
                if (row == 2) {
                    ChessPositionImpl testPos1 = new ChessPositionImpl(row-1, col);
                    if (board.getPiece(testPos1) == null) {
                        validMoves.add(new ChessMoveImpl(startPosition, testPos1, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMoveImpl(startPosition, testPos1, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMoveImpl(startPosition, testPos1, ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMoveImpl(startPosition, testPos1, ChessPiece.PieceType.BISHOP));
                    }
                    if (col > 1) {
                        ChessPositionImpl testPos = new ChessPositionImpl(row-1, col-1);
                        if ((board.getPiece(testPos) != null) && (board.getPiece(testPos).getTeamColor() == ChessGame.TeamColor.WHITE)) {
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.BISHOP));
                        }
                    }
                    if (col < 8) {
                        ChessPositionImpl testPos = new ChessPositionImpl(row-1, col+1);
                        if ((board.getPiece(testPos) != null) && (board.getPiece(testPos).getTeamColor() == ChessGame.TeamColor.WHITE)) {
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMoveImpl(startPosition, testPos, ChessPiece.PieceType.BISHOP));
                        }
                    }
                }
            }
                break;
            default: break;
        }
        return validMoves;
    }

    @Override
    public ArrayList<ChessMove> rookMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(startPosition).getTeamColor();
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
            int currRow = row + 1;
            while (currRow <= 8) {
                ChessPositionImpl testPos = new ChessPositionImpl(currRow, col);
                if (board.getPiece(testPos) == null) {
                    validMoves.add(new ChessMoveImpl(startPosition, testPos));
                } else {
                    if ((board.getPiece(testPos).getTeamColor()) != color) {
                        validMoves.add(new ChessMoveImpl(startPosition, testPos));
                    }
                    break;
                }
                currRow++;
            }
            currRow = row - 1;
            while (currRow >= 1) {
                ChessPositionImpl testPos = new ChessPositionImpl(currRow, col);
                if (board.getPiece(testPos) == null) {
                    validMoves.add(new ChessMoveImpl(startPosition, testPos));
                } else {
                    if ((board.getPiece(testPos).getTeamColor()) != color) {
                        validMoves.add(new ChessMoveImpl(startPosition, testPos));
                    }
                    break;
                }
                currRow--;
            }
            int currCol = col + 1;
            while (currCol <= 8) {
                ChessPositionImpl testPos = new ChessPositionImpl(row, currCol);
                if (board.getPiece(testPos) == null) {
                    validMoves.add(new ChessMoveImpl(startPosition, testPos));
                } else {
                    if ((board.getPiece(testPos).getTeamColor()) != color) {
                        validMoves.add(new ChessMoveImpl(startPosition, testPos));
                    }
                    break;
                }
                currCol++;
            }
            currCol = col - 1;
            while (currCol >= 1) {
                ChessPositionImpl testPos = new ChessPositionImpl(row, currCol);
                if (board.getPiece(testPos) == null) {
                    validMoves.add(new ChessMoveImpl(startPosition, testPos));
                } else {
                    if ((board.getPiece(testPos).getTeamColor()) != color) {
                        validMoves.add(new ChessMoveImpl(startPosition, testPos));
                    }
                    break;
                }
                currCol--;
            }
            return validMoves;
        }

    @Override
    public ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition startPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(startPosition).getTeamColor();
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        int currRow = row+1;
        int currCol = col+1;
        while ((currRow <= 8) && (currCol <= 8)) {
            ChessPositionImpl testPos = new ChessPositionImpl(currRow, currCol);
            if (board.getPiece(testPos) == null) {
                validMoves.add(new ChessMoveImpl(startPosition, testPos));
            }
            else {
                if ((board.getPiece(testPos).getTeamColor()) != color) {
                    validMoves.add(new ChessMoveImpl(startPosition, testPos));
                }
                break;
            }
            currRow++;
            currCol++;
        }
        currRow = row-1;
        currCol = col-1;
        while ((currRow >= 1) && (currCol >= 1)) {
            ChessPositionImpl testPos = new ChessPositionImpl(currRow, currCol);
            if (board.getPiece(testPos) == null) {
                validMoves.add(new ChessMoveImpl(startPosition, testPos));
            }
            else {
                if ((board.getPiece(testPos).getTeamColor()) != color) {
                    validMoves.add(new ChessMoveImpl(startPosition, testPos));
                }
                break;
            }
            currRow--;
            currCol--;
        }
        currRow = row-1;
        currCol = col+1;
        while ((currRow >= 1) && (currCol <= 8)) {
            ChessPositionImpl testPos = new ChessPositionImpl(currRow, currCol);
            if (board.getPiece(testPos) == null) {
                validMoves.add(new ChessMoveImpl(startPosition, testPos));
            }
            else {
                if ((board.getPiece(testPos).getTeamColor()) != color) {
                    validMoves.add(new ChessMoveImpl(startPosition, testPos));
                }
                break;
            }
            currRow--;
            currCol++;
        }
        currRow = row+1;
        currCol = col-1;
        while ((currRow <= 8) && (currCol >= 1)) {
            ChessPositionImpl testPos = new ChessPositionImpl(currRow, currCol);
            if (board.getPiece(testPos) == null) {
                validMoves.add(new ChessMoveImpl(startPosition, testPos));
            }
            else {
                if ((board.getPiece(testPos).getTeamColor()) != color) {
                    validMoves.add(new ChessMoveImpl(startPosition, testPos));
                }
                break;
            }
            currRow++;
            currCol--;
        }
        return validMoves;
    }
}

