package retrocode;

import java.awt.Color;
import java.awt.Font;
import java.awt.ScrollPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class RetroTextArea {
     private RSyntaxTextArea textarea;
     private RTextScrollPane scrollPane;

    public RetroTextArea(){
        textarea = new RSyntaxTextArea(20,60);
        textarea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ACTIONSCRIPT);
        textarea.setCodeFoldingEnabled(true);
        
        textarea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        textarea.setBackground(new Color(1,1,1));


        scrollPane = new RTextScrollPane(textarea);

    }

    public RSyntaxTextArea gTextArea() {
        return textarea;
    }

    public RTextScrollPane getSrcollPanel() {
        return scrollPane;
    }


}

