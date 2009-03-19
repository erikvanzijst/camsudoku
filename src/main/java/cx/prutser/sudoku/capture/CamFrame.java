package cx.prutser.sudoku.capture;

import cx.prutser.sudoku.ocr.GraphicalSolver;

import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;
import javax.media.Manager;
import javax.media.Player;
import javax.media.control.FormatControl;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.util.BufferToImage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Erik van Zijst
 */
public class CamFrame extends JFrame {

    private String locator = null;

    public CamFrame(String... args) throws Exception {
        setLayout(new BorderLayout());
        parseArgs(args);

        final CaptureDeviceInfo device = getDeviceInfo();
        if (device == null) {
            System.err.println("Capture device not found.");
            System.exit(1);

        } else {
            DataSource dataSource = Manager.createDataSource(device.getLocator());
            CaptureDevice cd = (CaptureDevice)dataSource;

            for(FormatControl fc : cd.getFormatControls()) {
                RGBFormat rgb = findMatchingFormat(fc.getSupportedFormats(), new Dimension(640, 480));
                if (rgb != null) {
                    System.out.println("Selecting 640x480 resolution.");
                    RGBFormat format = new RGBFormat(new Dimension(640,480), 614400, byte[].class, (float)25.0, 16, 63488, 2016, 31);
                    fc.setFormat(format);
                }
            }

            final GraphicalSolver ocrSolver = new GraphicalSolver();
            final Player player = Manager.createRealizedPlayer(dataSource);
            final FrameGrabbingControl grabber = (FrameGrabbingControl)player.getControl(FrameGrabbingControl.class.getName());

            JButton snapshot = new JButton("Take Snapshot");
            snapshot.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Grabbing current frame.");
                    Buffer buffer = grabber.grabFrame();
                    Image image = new BufferToImage((VideoFormat)buffer.getFormat()).createImage(buffer);
                    new SnapshotDialog(image, ocrSolver);
                }
            });

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

            setTitle("Anagram Solver");
            add(player.getVisualComponent(), BorderLayout.NORTH);
            add(snapshot, BorderLayout.SOUTH);
            pack();
            setResizable(false);
            player.start();
            setVisible(true);
        }
    }

    public static void main(String[] args) throws Exception {
        JFrame f = new CamFrame(args);
    }

    private CaptureDeviceInfo getDeviceInfo() {

        CaptureDeviceInfo device = null;

        Vector devices = CaptureDeviceManager.getDeviceList(null);

        for(Iterator it = devices.iterator(); it.hasNext();) {
            CaptureDeviceInfo di = (CaptureDeviceInfo)it.next();
            if (locator != null && locator.equals(di.getLocator().toString())) {
                device = di;
                break;
            } else {
                if (findMatchingFormat(di.getFormats(), new Dimension(640, 480)) != null) {
                    device = di;
                }
            }
        }
        return device;
    }

    /**
     * Returns the first {@link javax.media.format.RGBFormat} that matches the
     * given resolution.
     *
     * @param formats
     * @param size
     * @return
     */
    private RGBFormat findMatchingFormat(Format[] formats, Dimension size) {

        for (Format f : formats) {
            if (f instanceof RGBFormat) {
                RGBFormat rgb = (RGBFormat)f;
                if (rgb.getSize().equals(new Dimension(640, 480))) {
                    return rgb;
                }
            }
        }
        return null;
    }

    private void parseArgs(String... args) {

    }
}
