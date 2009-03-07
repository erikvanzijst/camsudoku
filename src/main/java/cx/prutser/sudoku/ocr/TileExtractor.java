package cx.prutser.sudoku.ocr;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author Erik van Zijst
 */
interface TileExtractor {
    int getNumberOfTiles();

    List<BufferedImage> getTiles();
}
