package retrocode;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class FileManger {
    public static JMenuBar buildMenu(JFrame frame, RetroTextArea editor) {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("file");

        JMenuItem open =  new JMenuItem("open");
        JMenuItem save  = new JMenuItem("save");
        JMenuItem New  = new JMenuItem("New");
        

        menuBar.add(file);
        file.add(New);
        file.add(open);
        file.add(save);
        
        return menuBar;
    }
    public static void openfile(JFrame parentFrame , RSyntaxTextArea textArea) {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(parentFrame);
        File file = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
        }
        try {
            if (file != null) {
                textArea.read(new FileReader(file), null);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parentFrame, "It's not worth opening it... ");
        }
    }

    public static void savefile(JFrame parentFrame , RSyntaxTextArea textArea) {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(parentFrame);
        File file = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
        }
        try {
            if (file != null) {
                java.io.FileWriter writer = new java.io.FileWriter(file);
                textArea.write(writer);
                writer.close();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parentFrame, "It's not worth it saving it, your code is trash anyways");
        }
    }

    public static void newfile(JFrame parentFrame , RSyntaxTextArea textArea) {
        textArea.setText("");
    }
}
