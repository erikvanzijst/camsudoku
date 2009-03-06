package cx.prutser.ocr;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: erik
 * Date: 1/03/2009
 * Time: 18:41:09
 * To change this template use File | Settings | File Templates.
 */
public interface TileExtractor {
    int getNumberOfTiles();

    List<BufferedImage> getTiles();
}
