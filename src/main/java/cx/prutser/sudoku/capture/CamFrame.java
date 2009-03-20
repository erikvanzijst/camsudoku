package cx.prutser.sudoku.capture;

import cx.prutser.sudoku.ocr.GraphicalSolver;

import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
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

/**
 * @author Erik van Zijst
 */
public class CamFrame extends JFrame {

    public CamFrame(String... args) throws Exception {

//        loadNativeLibs();

        setLayout(new BorderLayout());

        final CaptureDeviceInfo device = CaptureUtils.getDeviceInfo();
        if (device == null) {
            System.err.println("Capture device not found.");
            System.exit(1);

        } else {
            DataSource dataSource = Manager.createDataSource(device.getLocator());
            CaptureDevice cd = (CaptureDevice)dataSource;

            for(FormatControl fc : cd.getFormatControls()) {
                RGBFormat rgb = CaptureUtils.findMatchingFormat(fc.getSupportedFormats(), new Dimension(640, 480));
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

    private void loadNativeLibs() {
        
        System.loadLibrary("jmfjawt");
        System.loadLibrary("jmmpx");
        System.loadLibrary("jmxlib");
        System.loadLibrary("jmv4l");
        System.loadLibrary("jmmpegv");
        System.loadLibrary("jmutil");
        System.loadLibrary("jmmpa");
        System.loadLibrary("jmdaud");
        System.loadLibrary("jmcvid");
        System.loadLibrary("jmjpeg");
        System.loadLibrary("jmh263enc");
        System.loadLibrary("jmh261");
        System.loadLibrary("jmg723");
        System.loadLibrary("jmgsm");
    }

    public static void main(String[] args) throws Exception {
        JFrame f = new CamFrame(args);
    }
}
