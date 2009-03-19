package cx.prutser.sudoku.ocr;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Arrays;

/**
 * @author  Erik van Zijst
 */
public class LetterRecognizerTest {

    private double[] pattern = new double[26];
    private final LetterRecognizer ocr = new LetterRecognizer();
    private final char[] CHARACTERS = LetterRecognizer.ALPHABET.toCharArray();

    @Before
    public void before() {
        Arrays.fill(pattern, 0D);
    }

    @Test
    public void classifyA() {

        pattern[LetterRecognizer.ALPHABET.indexOf('A')] = 1;
        assertEquals('A', ocr.classifyResult(pattern));

        pattern[LetterRecognizer.ALPHABET.indexOf('B')] = .4D;
        assertEquals('A', ocr.classifyResult(pattern));
    }
}
