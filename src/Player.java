import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class Player {
    int x, y;

    double dx;

    double dy = 0; // Current vertical velocity (0 -> vertically still)
    double gravity = .5; // How fast the player accelerates downward every frame
    double jumpStrength = -12.0; // The initial upward burst when jumping (negative moves UP)
    boolean isGrounded = false; // Tracks if the player is standing on a surface
    int floorY = 400; // Temporary screen coordinate for ground level

    Map<PlayerState, List<BufferedImage>> spriteMap;

    List<BufferedImage> idleRightFrames;
    List<BufferedImage> idleLeftFrames;

    List<BufferedImage> walkRightFrames;
    List<BufferedImage> walkLeftFrames;

    List<BufferedImage> attackRightFrames;
    List<BufferedImage> attackLeftFrames;

    List<BufferedImage> airRightFrames;
    List<BufferedImage> airLeftFrames;

    int currentAnimationFrame;

    boolean movingLeft, movingRight;

    int speed = 8;

    BufferedImage currentSprite;

    int animationTick = 0;

    PlayerState currentState = PlayerState.IDLE_RIGHT;

    public Player() {
        spriteMap = new HashMap<>();

        idleRightFrames = new ArrayList<>();

        walkRightFrames = new ArrayList<>();

        airRightFrames = new ArrayList<>();

        this.y = floorY;
        try {
            idleRightFrames.add(
                    createBufferedImage("assets\\knight\\knight-idle.png"));
            spriteMap.put(PlayerState.IDLE_RIGHT, idleRightFrames);

            // Clones the above list's contents
            idleLeftFrames = new ArrayList<>(idleRightFrames);
            spriteMap.put(PlayerState.IDLE_LEFT, idleLeftFrames);

            walkRightFrames.add(
                    createBufferedImage("assets\\knight\\knight-walking1.png"));
            walkRightFrames.add(
                    createBufferedImage("assets\\knight\\knight-walking2.png"));
            walkRightFrames.add(
                    createBufferedImage("assets\\knight\\knight-walking3.png"));
            walkRightFrames.add(
                    createBufferedImage("assets\\knight\\knight-walking4.png"));
            walkRightFrames.add(
                    createBufferedImage("assets\\knight\\knight-walking5.png"));
            walkRightFrames.add(
                    createBufferedImage("assets\\knight\\knight-walking6.png"));
            spriteMap.put(PlayerState.WALK_RIGHT, walkRightFrames);

            // Clones the above list's contents
            walkLeftFrames = new ArrayList<>(walkRightFrames);
            spriteMap.put(PlayerState.WALK_LEFT, walkLeftFrames);

            /*
             * var attackImg1 = createBufferedImage(
             * "D:\\Gustavo\\Joguinho\\jogo-java-swing\\assets\\knight\\knight-attack1.png")
             * ;
             * var attackImg2 = createBufferedImage(
             * "D:\\Gustavo\\Joguinho\\jogo-java-swing\\assets\\knight\\knight-attack2.png")
             * ;
             */

            airRightFrames
                    .add(createBufferedImage("D:\\Gustavo\\Joguinho\\jogo-java-swing\\assets\\knight\\knight-air.png"));
            spriteMap.put(PlayerState.AIR_RIGHT, airRightFrames);

            airLeftFrames = new ArrayList<>(airRightFrames);
            spriteMap.put(PlayerState.AIR_LEFT, airLeftFrames);

            currentSprite = spriteMap.get(PlayerState.IDLE_RIGHT).get(0);

        } catch (IOException e) {
            e.printStackTrace();
            IO.println("Error loading sprite images!");
        }
    }

    public void update() {
        if (currentAnimationFrame >= 5)
            this.currentAnimationFrame = 0;

        if (!isGrounded) {
            dy += gravity;
            this.currentAnimationFrame = 0;
            if (this.currentState.toString().contains("RIGHT"))
                changeSprite(PlayerState.AIR_RIGHT, this.currentAnimationFrame);
            else if (this.currentState.toString().contains("LEFT"))
                changeSprite(PlayerState.AIR_LEFT, this.currentAnimationFrame);
        }
        this.y += dy;

        if (this.y >= floorY) {
            this.y = floorY;
            dy = 0;
            isGrounded = true;
        }

        if (movingRight && movingLeft)
            this.x += 0;
        else if (movingRight) {
            this.x += this.speed;
            changeSprite(PlayerState.WALK_RIGHT, currentAnimationFrame++);
        } else if (movingLeft) {
            this.x -= this.speed;
            changeSprite(PlayerState.WALK_LEFT, currentAnimationFrame++);
        } else {
            this.currentAnimationFrame = 0;
            if (this.currentState == PlayerState.WALK_RIGHT)
                changeSprite(PlayerState.IDLE_RIGHT, this.currentAnimationFrame);
            else if (this.currentState == PlayerState.WALK_LEFT) 
                changeSprite(PlayerState.IDLE_LEFT, this.currentAnimationFrame);
        }

    }

    public void draw(Graphics g) {
        int srcWidth = currentSprite.getWidth();
        int srcHeight = currentSprite.getHeight();
        int width = srcWidth * 2;
        int height = srcHeight * 2;

        if (this.currentState.toString().contains("RIGHT")) {
            g.drawImage(
                    currentSprite,
                    x, y, x + width, y + height,
                    0, 0, srcWidth, srcHeight,
                    null);
        } else if (this.currentState.toString().contains("LEFT")) {
            g.drawImage(
                    currentSprite,
                    x + width, y, x, y + height,
                    0, 0, srcWidth, srcHeight,
                    null);
        }
    }

    public void jump() {
        if (isGrounded) {
            dy = jumpStrength;
            isGrounded = false;
        }
    }

    private static BufferedImage createBufferedImage(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }

    // Helper method to clear out every time I want to animate something in regards to the player
    // inside update()
    private void changeSprite(PlayerState targetState, int index) {
        this.currentState = targetState;
        List<BufferedImage> frames = this.spriteMap.get(this.currentState);
        this.currentSprite = frames.get(index);
    }
}
