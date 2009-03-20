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

    // image taken from http://www.ajaxload.info
    private static final String ANIMATION_FILE = "wait1.gif";

    public SnapshotDialog(Image image, final GraphicalSolver solver) {
        setTitle("Snapshot");
        final JPanel glass = createAnimationOverlay();

        final BufferedImage bi = CaptureUtils.createBufferedImage(image, BufferedImage.TYPE_INT_RGB);
        final int width = bi.getWidth();
        final int height = bi.getHeight();

        final ApertureImage apertureImage = new ApertureImage(bi);
        final JButton button = new JButton("Solve");

        final JList jlist = new JList(new String[]{"foo", "bar"});
        jlist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jlist.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        jlist.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(jlist);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        //Create a container so that we can add a title around
        //the scroll pane.  Can't add a title directly to the
        //scroll pane because its background would be white.
        //Lay out the label and scroll pane from top to bottom.
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel("Words Found:");
        label.setLabelFor(jlist);
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Solve!");
                glass.setVisible(true);
                apertureImage.setFixed(true);
                button.setEnabled(false);


                Thread t = new Thread(new Runnable() {
                    public void run() {

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

                        // write stretched image to the screen:
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                apertureImage.setImage(target);
                            }
                        });

                        final java.util.List<String> solution = solver.solve(target);

                        // write solution to the screen:
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                jlist.setListData(solution.toArray());
                                glass.setVisible(false);
                            }
                        });
                    }
                }, "SolverThread");
                t.setDaemon(true);
                t.start();
            }
        });


        // do the layout:
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(layout);

        // add image:
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(apertureImage, c);
        add(apertureImage);

        // add solve button:
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(button, c);
        add(button);

        // add solutions list:
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(listPane, c);
        add(listPane);


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
            Font font = label.getFont().deriveFont(Font.BOLD, 20);
            label.setFont(font);
            label.setVerticalTextPosition(JLabel.BOTTOM);
            label.setHorizontalTextPosition(JLabel.CENTER);
            glass.add(label);
        }
        return glass;
    }
}
