package cx.prutser.capture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * @author Erik van Zijst
 */
public class SnapshotDialog extends EscapeDialog {

    public SnapshotDialog(Frame owner, Image image) {
        super(owner, true);
        setTitle("Snapshot");
        setLayout(new BorderLayout());

        final BufferedImage bi = Util.createBufferedImage(image);

        ImageIcon icon = new ImageIcon(bi);
        JLabel label = new JLabel(icon);
        JButton button = new JButton("Solve");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Solve!");

                TileExtractor extractor = new LoggingTileExtractor(new SimpleTileExtractor(bi), "snapshots");
                
            }
        });
        add(label, BorderLayout.NORTH);
        add(button, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
            }
        });
        pack();
        setResizable(false);
        setVisible(true);
    }
}
