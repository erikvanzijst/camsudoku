package cx.prutser.ocr;

import cx.prutser.capture.CaptureUtils;

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
    private final List<BufferedImage> tiles;

    public SimpleTileExtractor(BufferedImage image) {

        if (image == null || image.getWidth() < image.getHeight()) {
            throw new IllegalArgumentException("Invalid dimensions.");

        } else {
            final List<BufferedImage> tiles = new ArrayList<BufferedImage>();
            final BufferedImage bi = CaptureUtils.createBufferedImage(
                    image.getScaledInstance(-1, (TILE_SIZE + 2 * TILE_MARGIN) * 9, Image.SCALE_SMOOTH));

            for(int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    
                    tiles.add(OCRUtils.toGrayScale(
                            bi.getSubimage(
                            col * (TILE_SIZE + 2 * TILE_MARGIN) + TILE_MARGIN,
                            row * (TILE_SIZE + 2 * TILE_MARGIN) + TILE_MARGIN,
                            TILE_SIZE, TILE_SIZE)));
                }
            }
            this.tiles = Collections.unmodifiableList(tiles);
        }
    }

    public int getNumberOfTiles() {
        return tiles.size();
    }

    public List<BufferedImage> getTiles() {
        return tiles;
    }
}
