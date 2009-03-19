package cx.prutser.sudoku.ocr;

import java.awt.image.BufferedImage;

/**
 * @author  Erik van Zijst
 */
public interface LetterResolver {

    /**
     * Resolves the letter in the supplied image. The letter MUST be capital
     * A-Z. Null is returned when the image was not recognized.
     *
     * @param image
     * @return
     */
    Character resolveLetter(BufferedImage image);
}
