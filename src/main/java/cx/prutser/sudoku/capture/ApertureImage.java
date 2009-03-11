package cx.prutser.sudoku.capture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Erik van Zijst
 */
public class ApertureImage extends JPanel implements MouseListener, MouseMotionListener {

    private static final int RADIUS = 6;
    private final int width;
    private final int height;
    private BufferedImage image;
    private int[] x = new int[4];
    private int[] y = new int[4];
    private int selected = -1;
    private boolean fixed = false;

    public ApertureImage(BufferedImage image) {

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();

        x[0] = (int)(width * 0.1);
        y[0] = (int)(height * 0.1);
        x[1] = (int)(width * 0.9);
        y[1] = (int)(height * 0.1);
        x[2] = (int)(width * 0.9);
        y[2] = (int)(height * 0.9);
        x[3] = (int)(width * 0.1);
        y[3] = (int)(height * 0.9);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public java.util.List<Point> getCorners() {
        final java.util.List points = new ArrayList<Point>();
        for (int i = 0; i < x.length; i++) {
            points.add(new Point(x[i], y[i]));
        }
        return points;
    }

    public void mouseDragged(MouseEvent e) {

        if (selected > -1 && !fixed) {
            x[selected] = Math.max(0, Math.min(width, e.getX()));
            y[selected] = Math.max(0, Math.min(height, e.getY()));
            repaint();
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (!fixed) {
            selected = -1;
            for (int i = 0; i < x.length; i++) {
                if (isHit(x[i], y[i], e.getX(), e.getY())) {
                    selected = i;
                }
            }
            repaint();
        }
    }

    private boolean isHit(int x, int y, int mouseX, int mouseY) {
        return mouseX > x - RADIUS && mouseX < x + RADIUS &&
                mouseY > y - RADIUS && mouseY < y + RADIUS;
    }

    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(image, 0, 0, null);
        if (!fixed) {
            g2.setPaint(Color.white);
            g2.drawPolygon(x, y, 4);
        }
    }
}
