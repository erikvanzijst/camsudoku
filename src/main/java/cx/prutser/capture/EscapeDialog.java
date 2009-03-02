package cx.prutser.capture;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;

/**
 */
public class EscapeDialog extends JDialog {

    public EscapeDialog() {
    }

    public EscapeDialog(Dialog owner) {
        super(owner);
    }

    public EscapeDialog(Dialog owner, boolean modal) {
        super(owner, modal);
    }

    public EscapeDialog(Dialog owner, String title) {
        super(owner, title);
    }

    public EscapeDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    public EscapeDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public EscapeDialog(Frame owner) {
        super(owner);
    }

    public EscapeDialog(Frame owner, boolean modal) {
        super(owner, modal);
    }

    public EscapeDialog(Frame owner, String title) {
        super(owner, title);
    }

    public EscapeDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    public EscapeDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public EscapeDialog(Window owner) {
        super(owner);
    }

    public EscapeDialog(Window owner, ModalityType modalityType) {
        super(owner, modalityType);
    }

    public EscapeDialog(Window owner, String title) {
        super(owner, title);
    }

    public EscapeDialog(Window owner, String title, ModalityType modalityType) {
        super(owner, title, modalityType);
    }

    public EscapeDialog(Window owner, String title, ModalityType modalityType, GraphicsConfiguration gc) {
        super(owner, title, modalityType, gc);
    }

    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
            }
        };
        JRootPane rootPane = new JRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return rootPane;
    }
}
