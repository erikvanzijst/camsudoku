package cx.prutser.sudoku.ocr;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author Erik van Zijst
 */
interface TileExtractor {

    /**
     * Takes the source image and extracts the sudoku tiles. {@link cx.prutser.sudoku.ocr.TileExtractor}s
     * can be stacked.
     * 
     * @param image
     * @return
     */
    List<BufferedImage> extractTiles(BufferedImage image);
}
