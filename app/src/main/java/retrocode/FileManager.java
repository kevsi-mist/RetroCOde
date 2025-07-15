package retrocode;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class FileManager {

    private static File currentFile = null;
    private static JTree fileTree;
    private static DefaultTreeModel treeModel;


    public static File getCurrentFile() {
        return currentFile;
    }

    public static void setCurrentFile(File file) {
        currentFile = file;
    }

    // 📁 FILE MENU
    public static JMenuBar buildMenu(JFrame frame, RetroTextArea editor) {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");

        newItem.addActionListener(e -> newFile(frame, editor.getTextArea()));
        openItem.addActionListener(e -> openFile(frame, editor.getTextArea()));
        saveItem.addActionListener(e -> saveFile(frame, editor.getTextArea()));
        saveAsItem.addActionListener(e -> saveFileAs(frame, editor.getTextArea()));

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        menuBar.add(fileMenu);

        return menuBar;
    }

    // 📂 FILE TREE PANEL
    public static JPanel buildFileTreePanel(JFrame frame, RetroTextArea editor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));

        JButton openFolderButton = new JButton("📁 Open Folder");
        openFolderButton.setFocusPainted(false);
        openFolderButton.setBackground(new Color(50, 50, 50));
        openFolderButton.setForeground(Color.WHITE);
        openFolderButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        openFolderButton.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(openFolderButton, BorderLayout.NORTH);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("No Folder Open");
        treeModel = new DefaultTreeModel(root);
        fileTree = new JTree(treeModel);
        fileTree.setRootVisible(true);
        fileTree.setBackground(new Color(30, 30, 30));
        fileTree.setForeground(Color.WHITE);

        fileTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            if (selected == null) return;

            Object userObject = selected.getUserObject();
            if (userObject instanceof File file && file.isFile()) {
                try (FileReader reader = new FileReader(file)) {
                    editor.getTextArea().read(reader, null);
                    frame.setTitle("RetroCode - " + file.getName());
                    currentFile = file;
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to open file.");
                }
            }
        });

        JScrollPane scroll = new JScrollPane(fileTree);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        openFolderButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File folder = chooser.getSelectedFile();
                openFolder(folder);
            }
        });

        return panel;
    }

    private static void openFolder(File folder) {
        if (folder == null || !folder.isDirectory()) return;
        DefaultMutableTreeNode root = createFileNode(folder);
        treeModel.setRoot(root);
        fileTree.setRootVisible(true);
        expandFirstLevel(root);
    }

    private static DefaultMutableTreeNode createFileNode(File file) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    node.add(createFileNode(child));
                }
            }
        }
        return node;
    }

    private static void expandFirstLevel(DefaultMutableTreeNode node) {
        TreePath path = new TreePath(node.getPath());
        fileTree.expandPath(path);
        for (int i = 0; i < node.getChildCount(); i++) {
            fileTree.expandPath(path.pathByAddingChild(node.getChildAt(i)));
        }
    }

    // 🔁 FILE ACTIONS
    public static void newFile(JFrame frame, RSyntaxTextArea textArea) {
        int option = JOptionPane.showConfirmDialog(frame, "Do you want to save the current file before creating a new one?",
                "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);

        if (option == JOptionPane.CANCEL_OPTION) return;

        if (option == JOptionPane.YES_OPTION) {
            saveFile(frame, textArea);
        }

        textArea.setText("");
        currentFile = null;
        frame.setTitle("RetroCode - Untitled");
    }

    public static void openFile(JFrame frame, RSyntaxTextArea textArea) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Code files", "txt", "java", "py", "js", "cpp"));
        int result = chooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (FileReader reader = new FileReader(file)) {
                textArea.read(reader, null);
                currentFile = file;
                frame.setTitle("RetroCode - " + file.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Failed to open file.");
            }
        }
    }

    public static void saveFile(JFrame frame, RSyntaxTextArea textArea) {
        if (currentFile != null) {
            try (FileWriter writer = new FileWriter(currentFile)) {
                textArea.write(writer);
                frame.setTitle("RetroCode - " + currentFile.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Failed to save file.");
            }
        } else {
            saveFileAs(frame, textArea);
        }
    }

    public static void saveFileAs(JFrame frame, RSyntaxTextArea textArea) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Code files", "txt", "java", "py", "js", "cpp"));
        int result = chooser.showSaveDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(file)) {
                textArea.write(writer);
                currentFile = file;
                frame.setTitle("RetroCode - " + file.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Failed to save file.");
            }
        }
    }
}
