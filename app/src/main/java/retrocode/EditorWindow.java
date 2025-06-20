package retrocode;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class EditorWindow {
    private JFrame frame;
    private RetroTextArea editor;

    public void showEditor() {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("Could not load dark theme.");
        }

        frame = new JFrame("RetroCode - Untitled");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        editor = new RetroTextArea();
        frame.add(editor.getScrollPane(), BorderLayout.CENTER);

        frame.setJMenuBar(FileManager.buildMenu(frame, editor));
        frame.setVisible(true);
    }
}
