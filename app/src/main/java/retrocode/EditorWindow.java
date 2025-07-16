package retrocode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel titleBar = createTitleBar();
        mainPanel.add(titleBar, BorderLayout.NORTH);

        editor = new RetroTextArea();
        mainPanel.add(editor.getScrollPane(), BorderLayout.CENTER);

        frame.setJMenuBar(FileManager.buildMenu(frame, editor));
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createTitleBar() {
        JPanel bar = new JPanel();
        bar.setLayout(new BorderLayout());
        bar.setBackground(new Color(45, 45, 45));
        bar.setPreferredSize(new Dimension(0, 30));
        JLabel titleLabel = new JLabel("RetroCode");
        titleLabel.setForeground(Color.WHITE);
        bar.add(titleLabel, BorderLayout.WEST);
        return bar;
    }
}
