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

/**
 * @author  Erik van Zijst
 */
public class AnagramApplet extends JApplet {

    @Override
    public void init() {
        setLayout(new BorderLayout());

        final CaptureDeviceInfo device = CaptureUtils.getDeviceInfo();
        if (device == null) {
            System.err.println("Capture device not found.");
//            System.exit(1);

        } else {
            try {
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

                add(player.getVisualComponent(), BorderLayout.NORTH);
                add(snapshot, BorderLayout.SOUTH);
                player.start();
                setVisible(true);
                
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}
