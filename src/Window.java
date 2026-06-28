import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Window {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    
    void main() {
        var frame = new JFrame();

        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        var tools = Toolkit.getDefaultToolkit();
        var midWidth = (tools.getScreenSize().width-frame.getWidth())/2;
        var midHeight = (tools.getScreenSize().height-frame.getHeight())/2;
        frame.setLocation(midWidth, midHeight);

        var panel = new GamePanel();

        frame.add(panel, BorderLayout.CENTER);
        
        frame.setVisible(true);
        panel.requestFocusInWindow();
    }
}