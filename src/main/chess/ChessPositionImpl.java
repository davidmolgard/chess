package chess;

public class ChessPositionImpl implements ChessPosition{
    private int row = -1;
    private int column = -1;
    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }
}
