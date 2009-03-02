package cx.prutser.capture;

import com.digiburo.backprop1.BackProp;

/**
 * OCR engine for recognizing the digits [1-9] and the blank tile in a sudoku.
 *
 * @author Erik van Zijst
 */
public class SudokuDigitRecognizer extends BackProp {
    private static final int WIDTH = 48;
    private static final int HEIGHT = 48;

    public SudokuDigitRecognizer() {
        super(WIDTH * HEIGHT, 10, 10, 0.45, 0.9);
    }
}
