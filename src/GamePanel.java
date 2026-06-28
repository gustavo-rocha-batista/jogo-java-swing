import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {
    // Dimensions
    private final int WIDTH = 600;
    private final int HEIGHT = 600;

    // Number in ms that represents more or less the duration of a single frame
    // considering that the window is going at 60 frames/second
    private final int DELAY = 16;

    Player player = new Player();;

    Timer timer = new Timer(DELAY, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            player.update();
            GamePanel.this.repaint();
        }
    });

    public GamePanel() {
        timer.start();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    player.movingRight = true;
                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    player.movingLeft = true;

                // Jumping
                if (e.getKeyCode() == KeyEvent.VK_UP)
                    player.jump();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    player.movingRight = false;
                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    player.movingLeft = false;
            }
        });

        this.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
    }
}
