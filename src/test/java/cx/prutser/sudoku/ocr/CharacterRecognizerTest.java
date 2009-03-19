package cx.prutser.sudoku.ocr;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.util.Arrays;

/**
 * @author  Erik van Zijst
 */
public class CharacterRecognizerTest {

    private double[] pattern = new double[26];
    private final CharacterRecognizer ocr = new CharacterRecognizer();
    private final char[] CHARACTERS = CharacterRecognizer.ALPHABET.toCharArray();

    @Before
    public void before() {
        Arrays.fill(pattern, 0D);
    }

    @Test
    public void classifyA() {

        pattern[CharacterRecognizer.ALPHABET.indexOf('A')] = 1;
        assertEquals('A', ocr.classifyResult(pattern));

        pattern[CharacterRecognizer.ALPHABET.indexOf('B')] = .4D;
        assertEquals('A', ocr.classifyResult(pattern));
    }
}
