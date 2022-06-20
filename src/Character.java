import javax.swing.*;
import java.awt.*;

public class Character{
    int livesLeft;
    int speed;

    public ImageIcon getCurrentSprite() {
        return currentSprite;
    }

    ImageIcon currentSprite;
    ImageIcon SPRITE_UP;
    ImageIcon SPRITE_DOWN;
    ImageIcon SPRITE_LEFT;
    ImageIcon SPRITE_RIGHT;
    Point spawnPoint;


    public Point getSpawnPoint() {
        return spawnPoint;
    }

    public Character(int livesLeft, int speed, ImageIcon currentSprite, Point spawnPoint) {
        this.livesLeft = livesLeft;
        this.speed = speed;
        this.currentSprite = currentSprite;
        this.spawnPoint = spawnPoint;
        
    }

    public enum Movement{UP,DOWN,LEFT,RIGHT};
    public enum Mode {SCATTER, CHASE, EATEN, FRIGHTENED, MANUAL}

    public Movement getCurrentMovement() {
        return currentMovement;
    }

    Movement currentMovement;

    public void setCurrentMovement(Movement m){
        currentMovement = m;
    }
}