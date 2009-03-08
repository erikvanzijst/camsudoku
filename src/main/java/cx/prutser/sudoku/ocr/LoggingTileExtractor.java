package cx.prutser.sudoku.ocr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Erik van Zijst
 */
class LoggingTileExtractor implements TileExtractor {

    private final TileExtractor tileExtractor;
    private final String path;
    private long sequence;

    public LoggingTileExtractor(TileExtractor tileExtractor, String path) {

        this.tileExtractor = tileExtractor;
        this.path = path;
        this.sequence = System.currentTimeMillis();
    }

    public List<BufferedImage> extractTiles(BufferedImage image) {

        final List<BufferedImage> tiles = tileExtractor.extractTiles(image);

        write(image);
        for (BufferedImage tile : tiles) {
            write(tile);
        }
        return tiles;
    }

    private void write(BufferedImage image) {

        File file = new File(String.format("%s/%d.png",path, sequence++));
        try {
            ImageIO.write(image, "png", file);
        } catch(IOException e) {
            System.err.println("Error writing image " + file.getPath() + ": " + e.getMessage());
        }
    }
}
