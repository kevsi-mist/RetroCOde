package retrocode;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.File;

public class FileTreePanel extends JPanel {
    private JTree tree;
    private DefaultTreeModel treeModel;
    private java.util.function.Consumer<File> onFileOpen;

    public FileTreePanel(java.util.function.Consumer<File> onFileOpen) {
        this.onFileOpen = onFileOpen;
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));

        // 📁 Open Folder button
        JButton openFolderButton = new JButton("📁 Open Folder");
        openFolderButton.setFocusPainted(false);
        openFolderButton.setBackground(new Color(50, 50, 50));
        openFolderButton.setForeground(Color.WHITE);
        openFolderButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        openFolderButton.setHorizontalAlignment(SwingConstants.LEFT);
        openFolderButton.addActionListener(e -> chooseAndOpenFolder());

        add(openFolderButton, BorderLayout.NORTH);

        // Default Tree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("No Folder Open");
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setRootVisible(true);
        tree.setBackground(new Color(30, 30, 30));
        tree.setForeground(Color.WHITE);

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selected == null) return;

            Object userObject = selected.getUserObject();
            if (userObject instanceof File file && file.isFile()) {
                onFileOpen.accept(file);
            }
        });

        JScrollPane scroll = new JScrollPane(tree);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    private void chooseAndOpenFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File folder = chooser.getSelectedFile();
            openFolder(folder);
        }
    }

    private DefaultMutableTreeNode createFileNode(File file) {
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

    public void openFolder(File folder) {
        if (folder == null || !folder.isDirectory()) return;
        DefaultMutableTreeNode root = createFileNode(folder);
        treeModel.setRoot(root);
        tree.setRootVisible(true);
        expandFirstLevel(root);
    }

    private void expandFirstLevel(DefaultMutableTreeNode node) {
        TreePath path = new TreePath(node.getPath());
        tree.expandPath(path);
        for (int i = 0; i < node.getChildCount(); i++) {
            tree.expandPath(path.pathByAddingChild(node.getChildAt(i)));
        }
    }
}
