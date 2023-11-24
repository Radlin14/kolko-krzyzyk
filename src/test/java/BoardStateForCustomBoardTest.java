import model.Board;
import model.BoardState;
import model.ModelFileManagerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoardStateForCustomBoardTest {
    @Test
    void testNoughtDiagonalRightDown() throws ModelFileManagerException {
        String[] fields = {
                "O    ",
                " O   ",
                "  O  ",
                "   O ",
                "     ",
        };
        Board board = new Board(fields, 4);
        Assertions.assertEquals(BoardState.NOUGHT_WIN, board.getState());
    }
    @Test
    void testNoughtDiagonalLeftDown() throws ModelFileManagerException {
        String[] fields = {
                "    O  ",
                "   O   ",
                "  O    ",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.NOUGHT_WIN, board.getState());
    }
    @Test
    void testNoughtHorizontal() throws ModelFileManagerException {
        String[] fields = {
                "         ",
                "OOO OOOOO",
                "         ",
        };
        Board board = new Board(fields, 4);
        Assertions.assertEquals(BoardState.NOUGHT_WIN, board.getState());
    }
    @Test
    void testNoughtVertical() throws ModelFileManagerException {
        String[] fields = {
                "  O   ",
                "  O   ",
                "  O   ",
                "  O   ",
                "  O   ",
                "  O   ",
                "  O   ",
        };
        Board board = new Board(fields, 4);
        Assertions.assertEquals(BoardState.NOUGHT_WIN, board.getState());    }
}
