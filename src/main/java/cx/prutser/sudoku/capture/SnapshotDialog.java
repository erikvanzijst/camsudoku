package cx.prutser.sudoku.capture;

import com.jhlabs.image.PerspectiveFilter;
import com.jhlabs.image.TransformFilter;
import cx.prutser.sudoku.ocr.GraphicalSolver;

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
public class SnapshotDialog extends JFrame {

    public SnapshotDialog(Image image, final GraphicalSolver solver) {
        setTitle("Snapshot");

        final BufferedImage bi = CaptureUtils.createBufferedImage(image, BufferedImage.TYPE_INT_RGB);
        final int width = bi.getWidth();
        final int height = bi.getHeight();

        final ApertureImage apertureImage = new ApertureImage(bi);
        JButton button = new JButton("Solve");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Solve!");

                java.util.List<Point> corners = apertureImage.getCorners();

                PerspectiveFilter filter = new PerspectiveFilter();
                filter.quadToUnitSquare(
                        (float)(corners.get(0).getX() / width), (float)(corners.get(0).getY() / height),
                        (float)(corners.get(1).getX() / width), (float)(corners.get(1).getY() / height),
                        (float)(corners.get(2).getX() / width), (float)(corners.get(2).getY() / height),
                        (float)(corners.get(3).getX() / width), (float)(corners.get(3).getY() / height));
                filter.setClip(true);
                filter.setInterpolation(TransformFilter.BILINEAR);

                BufferedImage target = new BufferedImage(bi.getWidth(null), bi.getHeight(null), BufferedImage.TYPE_INT_RGB);
                filter.filter(bi, target);
                apertureImage.setImage(target);
                apertureImage.setFixed(true);

                // TODO: do this in a thread
                final int newSize = Math.min(bi.getWidth(), bi.getHeight());
                target = solver.solve(CaptureUtils.createBufferedImage(
                        target.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH),
                        BufferedImage.TYPE_INT_RGB));

                apertureImage.setImage(target);
            }
        });
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(layout);

        c.fill = GridBagConstraints.NONE;
        c.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(apertureImage, c);
        add(apertureImage);

        c.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(button, c);
        add(button);
        
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
