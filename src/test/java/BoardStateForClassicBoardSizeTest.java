import model.Board;
import model.BoardState;
import model.ModelFileManagerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoardStateForClassicBoardSizeTest {
    @Test
    void testNoughtDiagonalRightDown() throws ModelFileManagerException {
        String[] fields = {
                "O  ",
                " O ",
                "  O",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.NOUGHT_WIN, board.getState());
    }
    @Test
    void testNoughtDiagonalLeftDown() throws ModelFileManagerException {
        String[] fields = {
                "  O",
                " O ",
                "O  ",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.NOUGHT_WIN, board.getState());
    }
    @Test
    void testNoughtHorizontal() throws ModelFileManagerException {
        String[] fields = {
                "   ",
                "OOO",
                "   ",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.NOUGHT_WIN, board.getState());
    }
    @Test
    void testNoughtVertical() throws ModelFileManagerException {
        String[] fields = {
                "  O",
                "  O",
                "  O",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.NOUGHT_WIN, board.getState());    }
    @Test
    void testDraw() throws ModelFileManagerException {
        String[] fields = {
                "XXO",
                "OXX",
                "XOO",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.DRAW, board.getState());
    }
    @Test
    void testPending() throws ModelFileManagerException {
        String[] fields = {
                "XXO",
                "O X",
                "OXO",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.PENDING, board.getState());
    }
    @Test
    void testFreeBoard() throws ModelFileManagerException {
        String[] fields = {
                "   ",
                "   ",
                "   ",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.PENDING, board.getState());
    }
    @Test
    void testCrossDiagonalRightDown() throws ModelFileManagerException {
        String[] fields = {
                "XOO",
                "OXO",
                "OXX",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.CROSS_WIN, board.getState());
    }
    @Test
    void testCrossDiagonalLeftDown() throws ModelFileManagerException {
        String[] fields = {
                "X X",
                " XO",
                "X  ",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.CROSS_WIN, board.getState());
    }
    @Test
    void testCrossHorizontal() throws ModelFileManagerException {
        String[] fields = {
                "   ",
                "OXO",
                "XXX",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.CROSS_WIN, board.getState());
    }
    @Test
    void testCrossVertical() throws ModelFileManagerException {
        String[] fields = {
                "OXX",
                "XXO",
                "OXO",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.CROSS_WIN, board.getState());
    }
    @Test
    void testDoubleWin() throws ModelFileManagerException {
        String[] fields = {
                "OXO",
                "XXO",
                "OXO",
        };
        Board board = new Board(fields, 3);
        Assertions.assertEquals(BoardState.DOUBLE_WIN, board.getState());
    }

}
