package cx.prutser.sudoku.ocr;

import cx.prutser.sudoku.capture.CaptureUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Erik van Zijst
 */
public class SinglePassCenteringTileExtractor implements TileExtractor {

    public static final int TILE_SIZE = 16;
    public static final int TILE_MARGIN = 2;

    public List<BufferedImage> extractTiles(BufferedImage image) {

        if (image == null || image.getWidth() < image.getHeight()) {
            throw new IllegalArgumentException("Invalid dimensions.");

        } else {
            final List<BufferedImage> tiles = new ArrayList<BufferedImage>();
            final BufferedImage bi = CaptureUtils.createBufferedImage(
                    image.getScaledInstance(-1, (TILE_SIZE + 2 * TILE_MARGIN) * 9, Image.SCALE_SMOOTH), BufferedImage.TYPE_BYTE_GRAY);

            for(int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {

                    int x = col * (TILE_SIZE + 2 * TILE_MARGIN) + TILE_MARGIN;
                    int y = row * (TILE_SIZE + 2 * TILE_MARGIN) + TILE_MARGIN;
                    final BufferedImage firstPass =
                            CaptureUtils.createBufferedImage(bi.getSubimage(x, y, TILE_SIZE, TILE_SIZE), BufferedImage.TYPE_BYTE_GRAY);

                    final Pair<Integer,Integer> mass = OCRUtils.centerOfMass(
                            OCRUtils.getPixels(firstPass), TILE_SIZE, TILE_SIZE, true);

                    // TODO: never shift more than n pixels
                    x += (mass.getFirst() - (TILE_SIZE / 2));
                    x = Math.min(bi.getWidth() - TILE_SIZE, Math.max(0, x));
                    y += (mass.getSecond() - (TILE_SIZE / 2));
                    y = Math.min(bi.getHeight() - TILE_SIZE, Math.max(0, y));

                    System.out.println(String.format("shift(%d, %d)", mass.getFirst() - (TILE_SIZE / 2), mass.getSecond() - (TILE_SIZE / 2)));
                    tiles.add(CaptureUtils.createBufferedImage(bi.getSubimage(x, y, TILE_SIZE, TILE_SIZE), BufferedImage.TYPE_BYTE_GRAY));
                }
            }
            return Collections.unmodifiableList(tiles);
        }
    }
}
