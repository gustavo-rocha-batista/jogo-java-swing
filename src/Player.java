import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class Player {
    int x, y;

    double dx;

    double dy = 0;                              // Current vertical velocity (0 -> vertically still)
    double gravity = .5;                        // How fast the player accelerates downward every frame
    double jumpStrength = -12.0;                // The initial upward burst when jumping (negative moves UP)
    boolean isGrounded = false;                 // Tracks if the player is standing on a surface
    int floorY = 400;                           // Temporary screen coordinate for ground level

    Map<PlayerState, BufferedImage> spriteMap;  
    boolean movingLeft, movingRight;

    int speed = 8;

    BufferedImage currentSprite;

    int animationTick = 0;

    PlayerState currentState = PlayerState.IDLE_RIGHT;

    public Player() {
        spriteMap = new HashMap<>();
        this.y = floorY;
        try {
            var idleImg = createBufferedImage("idle-right.png");
            var walkImg = createBufferedImage("walking-right.png");
            spriteMap.put(PlayerState.IDLE_RIGHT, idleImg);
            spriteMap.put(PlayerState.IDLE_LEFT, idleImg);
            spriteMap.put(PlayerState.WALK_RIGHT, walkImg);
            spriteMap.put(PlayerState.WALK_LEFT, walkImg);
            
            currentSprite = spriteMap.get(PlayerState.IDLE_RIGHT);

        } catch (IOException e) {
            e.printStackTrace();
            IO.println("Error loading sprite images!");
        }
    }

    public void update() {
        
        if (!isGrounded)
            dy += gravity;

        this.y += dy;

        if (this.y >= floorY) {
            this.y = floorY;
            dy = 0;
            isGrounded = true;
        }
        
        if (movingRight && movingLeft)
            this.x +=0;
        else if (movingRight) {
            this.x += this.speed;
            this.currentState = PlayerState.WALK_RIGHT;
            this.currentSprite = this.spriteMap.get(currentState);
        } else if (movingLeft) {
            this.x -= this.speed;
            this.currentState = PlayerState.WALK_LEFT;
            this.currentSprite = this.spriteMap.get(currentState);
        } else {
            if (this.currentState == PlayerState.WALK_RIGHT)
                this.currentState = PlayerState.IDLE_RIGHT;
            else if (this.currentState == PlayerState.WALK_LEFT)
                this.currentState = PlayerState.IDLE_LEFT;
        }

        switch(this.currentState) {
            case IDLE_RIGHT:
                this.currentSprite = this.spriteMap.get(currentState);
                break;
            case IDLE_LEFT:
                this.currentSprite = this.spriteMap.get(currentState);
                break;
            case WALK_RIGHT:
                if (this.animationTick == 10) {
                    this.currentSprite = this.spriteMap.get(PlayerState.IDLE_RIGHT);
                    this.animationTick = 0;
                } else 
                    this.animationTick++;
                break;
            case WALK_LEFT:
                if (this.animationTick == 10) {
                    this.currentSprite = this.spriteMap.get(PlayerState.IDLE_LEFT);
                    this.animationTick = 0;
                } else 
                    this.animationTick++;
                
                break;
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
                x, y, x+width, y+height, 
                0, 0, srcWidth, srcHeight, 
                null
            );
        } else if (this.currentState.toString().contains("LEFT")) {
            g.drawImage(
                currentSprite,
                x+width, y, x, y+height, 
                0, 0, srcWidth, srcHeight,
                null
            );
        }
    }

    public void jump() {
        if (isGrounded) {
            dy = jumpStrength;
            isGrounded = false;
        }
    }

    private static BufferedImage createBufferedImage(String filePath) throws IOException{
        return ImageIO.read(new File(filePath));
    }
}
