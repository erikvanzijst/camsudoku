package cx.prutser.ocr;

import java.awt.image.BufferedImage;

/**
 * @author Erik van Zijst
 */
public interface DigitResolver {

    /**
     * [0-9] or null if not recognized.
     * 
     * @param tile
     * @return
     */
    Integer resolveInt(BufferedImage tile);
}
