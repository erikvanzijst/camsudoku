package cx.prutser.capture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Erik van Zijst
 */
public class SimpleTileExtractor implements TileExtractor {

    public static final int TILE_SIZE = 16;
    private final List<BufferedImage> tiles;

    public SimpleTileExtractor(BufferedImage image) {

        if (image == null || image.getWidth() < image.getHeight()) {
            throw new IllegalArgumentException("Invalid dimensions.");

        } else {
            final List<BufferedImage> tiles = new ArrayList<BufferedImage>();
            final BufferedImage bi = Util.createBufferedImage(
                    image.getScaledInstance(-1, TILE_SIZE * 9, Image.SCALE_SMOOTH));

            for(int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    tiles.add(Util.toGrayScale(
                            bi.getSubimage(
                            col * TILE_SIZE, row * TILE_SIZE,
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
