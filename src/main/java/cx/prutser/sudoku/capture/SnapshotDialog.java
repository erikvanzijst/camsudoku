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
import java.net.URL;

/**
 * @author Erik van Zijst
 */
public class SnapshotDialog extends JFrame {

    private static final String ANIMATION_FILE = "wait1.gif";
    private static final long SEARCH_TIMEOUT = 5000L;

    public SnapshotDialog(Image image, final GraphicalSolver solver) {
        setTitle("Snapshot");
        final JPanel glass = createAnimationOverlay();
        glass.setVisible(false);

        final BufferedImage bi = CaptureUtils.createBufferedImage(image, BufferedImage.TYPE_INT_RGB);
        final int width = bi.getWidth();
        final int height = bi.getHeight();

        final ApertureImage apertureImage = new ApertureImage(bi);
        final JButton button = new JButton("Solve");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Solve!");
                glass.setVisible(true);

                java.util.List<Point> corners = apertureImage.getCorners();

                PerspectiveFilter filter = new PerspectiveFilter();
                filter.quadToUnitSquare(
                        (float)(corners.get(0).getX() / width), (float)(corners.get(0).getY() / height),
                        (float)(corners.get(1).getX() / width), (float)(corners.get(1).getY() / height),
                        (float)(corners.get(2).getX() / width), (float)(corners.get(2).getY() / height),
                        (float)(corners.get(3).getX() / width), (float)(corners.get(3).getY() / height));
                filter.setClip(true);
                filter.setInterpolation(TransformFilter.BILINEAR);

                BufferedImage corrected = new BufferedImage(bi.getWidth(null), bi.getHeight(null), BufferedImage.TYPE_INT_RGB);
                filter.filter(bi, corrected);
                final int newSize = Math.min(bi.getWidth(), bi.getHeight());
                final BufferedImage target = CaptureUtils.createBufferedImage(
                                corrected.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH),
                                BufferedImage.TYPE_INT_RGB);

                apertureImage.setImage(target);
                apertureImage.setFixed(true);
                button.setEnabled(false);

                Thread t = new Thread(new Runnable() {
                    public void run() {
                        final BufferedImage solution = solver.solve(target, SEARCH_TIMEOUT);

                        // write solution to screen:
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                apertureImage.setImage(solution);
                                glass.setVisible(false);
                            }
                        });
                    }
                }, "SolverThread");
                t.setDaemon(true);
                t.start();
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

    private JPanel createAnimationOverlay() {

        final JPanel glass = (JPanel)getGlassPane();
        glass.setLayout(new GridBagLayout());

        final URL iconUrl = getClass().getResource("/" + ANIMATION_FILE);
        if (iconUrl == null) {
            System.err.println("Animation file " + ANIMATION_FILE + " not found on classpath.");

        } else {
            ImageIcon ii = new ImageIcon(iconUrl);
            JLabel label = new JLabel("Solving...", ii, JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.BOTTOM);
            label.setHorizontalTextPosition(JLabel.CENTER);
            glass.add(label);
        }
        return glass;
    }
}
