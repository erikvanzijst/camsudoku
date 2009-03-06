package cx.prutser.sudoku;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

/**
 * @author Erik van Zijst
 */
public class ClassicSudokuUtilsTest {

    private Integer[] puzzle = null;
    private String rawPuzzle = null;

    @Before
    public void setup() {
        rawPuzzle =
                "- ? 8 ? - 2 - - 3\n" +
                "- 6 - - 5 - - 8 -\n" +
                "9 - - 3 - - 4 - -  \n" +
                "- - - 1 2 - - 9 -\n" +
                "- - 5  - - - 7 - -\n" +
                "- 8 - - 3 6 ? ? -\n" +
                " - - 6 - - 1 - - 8\n" +
                "- 5 - - 9 - - 6 -\n" +
                "3 - - 6 - - 9 - -";
        puzzle = new Integer[] {
                null, null, 8, null, null, 2, null, null, 3,
                null, 6, null, null, 5, null, null, 8, null,
                9, null, null, 3, null, null, 4, null, null,
                null, null, null, 1, 2, null, null, 9, null,
                null, null, 5, null, null, null, 7, null, null,
                null, 8, null, null, 3, 6, null, null, null,
                null, null, 6, null, null, 1, null, null, 8,
                null, 5, null, null, 9, null, null, 6, null,
                3, null, null, 6, null, null, 9, null, null
            };
    }

    @Test
    public void parseRawString() {
        assertTrue(Arrays.equals(puzzle, ClassicSudokuUtils.parseRaw(rawPuzzle)));
    }

    @Test
    public void parseRawReader() {
        try {
            assertTrue(Arrays.equals(puzzle, ClassicSudokuUtils.parseRaw(
                    new StringReader(rawPuzzle))));
        } catch(IOException ioe) {
            fail();
        }
    }
}
