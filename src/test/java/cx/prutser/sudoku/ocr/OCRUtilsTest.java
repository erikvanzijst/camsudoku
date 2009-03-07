package cx.prutser.sudoku.ocr;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author Erik van Zijst
 */
public class OCRUtilsTest {

    @Test
    public void oneDimensionalCenterOfGravity() {

        assertEquals(Pair.newInstance(1, 0),
                OCRUtils.centerOfGravity(new byte[]{1, 2, 1}, 3, 1, false));

        assertEquals(Pair.newInstance(1, 0),
                OCRUtils.centerOfGravity(new byte[]{1, 0, 1}, 3, 1, false));

        assertEquals(Pair.newInstance(0, 0),
                OCRUtils.centerOfGravity(new byte[]{10, 1, 1}, 3, 1, false));

        assertEquals(Pair.newInstance(0, 0),
                OCRUtils.centerOfGravity(new byte[]{0, 0, 0}, 3, 1, false));

        assertEquals(Pair.newInstance(2, 0),
                OCRUtils.centerOfGravity(new byte[]{0, 0, 1}, 3, 1, false));
    }

    @Test
    public void twoDimensionalCenterOfGravity() {

        assertEquals(Pair.newInstance(1, 1), OCRUtils.centerOfGravity(new byte[]
                {1, 0, 0,
                0, 0, 0,
                0, 0, 1}, 3, 3, false));

        assertEquals(Pair.newInstance(1, 1), OCRUtils.centerOfGravity(new byte[]
                {1, 0, 1,
                0, 0, 0,
                1, 0, 1}, 3, 3, false));

        assertEquals(Pair.newInstance(2, 0), OCRUtils.centerOfGravity(new byte[]
                {0, 1, 1,
                0, 0, 1,
                0, 0, 0}, 3, 3, false));

        assertEquals(Pair.newInstance(2, 0), OCRUtils.centerOfGravity(new byte[]
                {-1, 0, 0,
                -1, -1, 0,
                -1, -1, -1}, 3, 3, true));
    }

    @Test
    public void invert() {
        assertEquals(255, 0xFF & (int) OCRUtils.invert((byte)0));
        assertEquals(0, 0xFF & (int) OCRUtils.invert((byte)255));
        assertEquals(1, 0xFF & (int) OCRUtils.invert((byte)254));
        assertEquals(2, 0xFF & (int) OCRUtils.invert((byte)253));
        assertEquals(155, 0xFF & (int) OCRUtils.invert((byte)100));
    }
}
