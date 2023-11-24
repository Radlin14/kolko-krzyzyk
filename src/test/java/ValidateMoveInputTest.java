import model.Move;
import model.MoveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


public class ValidateMoveInputTest {

    @ParameterizedTest
    @ValueSource(strings = {"A1", "C5", "d9", "b11"})
    public void testCorrectInput(String input) throws MoveException {
        Assertions.assertTrue(Move.getMoveFromString(input) != null);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1a", "C", "", "aa2"})
    public void testIncorrectInput(String input)  {
        try {
            Assertions.assertThrows(MoveException.class, (Executable) Move.getMoveFromString(input));
        } catch (MoveException e) {

        }
    }

    @Test
    public void MoveFromStringCorrectValueTest() throws MoveException {
        Move move = Move.getMoveFromString("b3");
        Assertions.assertTrue(move.getX() == 2 && move.getY() == 1);
    }

}
