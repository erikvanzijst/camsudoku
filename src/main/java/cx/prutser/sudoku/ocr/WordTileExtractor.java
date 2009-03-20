package cx.prutser.sudoku.ocr;

import cx.prutser.sudoku.capture.CaptureUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * This class extracts the 9 tiles of the Target Word puzzle of the Sydney
 * Morning Herald as individual images, in the following order:
 *
 * <PRE>
 * +--+--+--+
 * | 0| 1| 2|
 * +--+--+--+
 * | 3| 4| 5|
 * +--+--+--+
 * | 6| 7| 8|
 * +--+--+--+
 * </PRE>
 *
 * @author  Erik van Zijst
 */
public class WordTileExtractor implements TileExtractor {

    public static final int TILE_SIZE = 16;
    public static final int TILE_MARGIN = 2;
    private static final int THRESHOLD_SIZE = 10;
    private static final int THRESHOLD_CONSTANT = 7;

    public List<BufferedImage> extractTiles(BufferedImage image) {

        final BufferedImage[] tiles = new BufferedImage[9];
        BufferedImage buf;

        // resize:
        buf = normalizeDimensions(image);

        // make gray scale:
        buf = OCRUtils.toGrayScale(buf);

        // crop center tile:
        BufferedImage center = OCRUtils.crop(buf, BufferedImage.TYPE_BYTE_GRAY,
                TILE_SIZE + 3 * TILE_MARGIN,
                TILE_SIZE + 3 * TILE_MARGIN,
                TILE_SIZE, TILE_SIZE);

        // threshold outer tiles:
        buf = OCRUtils.threshold(buf, BufferedImage.TYPE_BYTE_GRAY, THRESHOLD_SIZE, THRESHOLD_CONSTANT);

        // cut outer tiles:
        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                tiles[index++] = OCRUtils.toGrayScale(buf.getSubimage(
                        col * (TILE_SIZE + 2 * TILE_MARGIN) + TILE_MARGIN,
                        row * (TILE_SIZE + 2 * TILE_MARGIN) + TILE_MARGIN,
                        TILE_SIZE, TILE_SIZE));
            }
        }

        // invert the center tile:
        center = OCRUtils.invert(center, BufferedImage.TYPE_BYTE_GRAY);

        // threshold center tile:
        center  = OCRUtils.threshold(center, BufferedImage.TYPE_BYTE_GRAY, THRESHOLD_SIZE, THRESHOLD_CONSTANT);

        // store the center tile:
        tiles[4] = center;

        return Arrays.asList(tiles);
    }

    private BufferedImage normalizeDimensions(BufferedImage source) {

        if (source == null || source.getWidth() != source.getHeight()) {
            throw new IllegalArgumentException("Invalid dimensions.");

        } else {
            final int rib = (TILE_SIZE + 2 * TILE_MARGIN) * 3;
            return CaptureUtils.createBufferedImage(
                    source.getScaledInstance(rib, rib, Image.SCALE_SMOOTH), BufferedImage.TYPE_INT_RGB);
        }
    }
}
