package cx.prutser.sudoku.capture;

import javax.media.Manager;
import javax.media.Player;
import java.awt.*;
import java.io.File;

/**
 * Simple JMF video player.
 */
public class VideoPlayer extends Frame {

    public static void main(String[] args) throws Exception {
        Frame f = new VideoPlayer();
        f.pack();
        f.setVisible(true);
    }

    public VideoPlayer() throws java.io.IOException,
            javax.media.MediaException {
        FileDialog fd = new FileDialog
                (this, "TrivialJMFPlayer", FileDialog.LOAD);
        fd.setVisible(true);
        File f = new File(fd.getDirectory(), fd.getFile());
        Player p = Manager.createRealizedPlayer
                (f.toURI().toURL());
        Component c = p.getVisualComponent();
        add(c);
        p.start();
    }
}
