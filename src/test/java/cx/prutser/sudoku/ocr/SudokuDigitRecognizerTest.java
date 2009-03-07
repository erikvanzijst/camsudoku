package cx.prutser.sudoku.ocr;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author Erik van Zijst
 */
public class SudokuDigitRecognizerTest {

    @Test
    public void classifyResult() {

        final SudokuDigitRecognizer ocr = new SudokuDigitRecognizer();

        assertEquals(0, ocr.classifyResult(new double[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        assertEquals(0, ocr.classifyResult(new double[]{.5, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        assertEquals(0, ocr.classifyResult(new double[]{.5, 0, .49, .49, 0, 0, .49, .49, .49, 0}));

        assertEquals(1, ocr.classifyResult(new double[]{0, 1, 0, 0, 0, 0, 0, 0, 0, 0}));
        assertEquals(2, ocr.classifyResult(new double[]{0, 0, 1, 0, 0, 0, 0, 0, 0, 0}));
        assertEquals(3, ocr.classifyResult(new double[]{0, 0, 0, 1, 0, 0, 0, 0, 0, 0}));
        assertEquals(4, ocr.classifyResult(new double[]{0, 0, 0, 0, 1, 0, 0, 0, 0, 0}));
        assertEquals(5, ocr.classifyResult(new double[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0}));
        assertEquals(6, ocr.classifyResult(new double[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0}));
        assertEquals(7, ocr.classifyResult(new double[]{0, 0, 0, 0, 0, 0, 0, 1, 0, 0}));
        assertEquals(8, ocr.classifyResult(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 1, 0}));
        assertEquals(9, ocr.classifyResult(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1}));

        assertEquals(-1, ocr.classifyResult(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
        assertEquals(-1, ocr.classifyResult(new double[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 1}));
        assertEquals(-1, ocr.classifyResult(new double[]{0, 0, .5, 0.5, 0, 0, 0, 0, 0, 0}));
    }
}
