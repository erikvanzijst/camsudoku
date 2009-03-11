package cx.prutser.sudoku.ocr;

import cx.prutser.sudoku.capture.CaptureUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Extracts the 81 tile images by simply cutting the source image up in 81
 * equally sized squares. No other image processing is applied.
 *
 * @author Erik van Zijst
 */
class SimpleTileExtractor implements TileExtractor {

    public static final int TILE_SIZE = 16;
    public static final int TILE_MARGIN = 2;

    public List<BufferedImage> extractTiles(BufferedImage image) {

        if (image == null || image.getWidth() != image.getHeight()) {
            throw new IllegalArgumentException("Invalid dimensions.");

        } else {
            final int rib = (TILE_SIZE + 2 * TILE_MARGIN) * 9;
            final List<BufferedImage> tiles = new ArrayList<BufferedImage>();
            final BufferedImage bi = CaptureUtils.createBufferedImage(
                    image.getScaledInstance(rib, rib, Image.SCALE_SMOOTH), BufferedImage.TYPE_INT_RGB);

            for(int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {

                    tiles.add(OCRUtils.toGrayScale(
                            bi.getSubimage(
                            col * (TILE_SIZE + 2 * TILE_MARGIN) + TILE_MARGIN,
                            row * (TILE_SIZE + 2 * TILE_MARGIN) + TILE_MARGIN,
                            TILE_SIZE, TILE_SIZE)));
                }
            }
            return Collections.unmodifiableList(tiles);
        }
    }
}
