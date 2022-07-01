import javax.swing.*;
import java.awt.*;

public class Character{
    int livesLeft;
    int speed;
    int width, height;


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

    public Character(int livesLeft, int speed, ImageIcon currentSprite, Point spawnPoint, int width, int height) {
        this.livesLeft = livesLeft;
        this.speed = speed;
        this.currentSprite = currentSprite;
        this.spawnPoint = new Point((int) spawnPoint.getX(), (int) spawnPoint.getY());
        this.width = width;
        this.height = height;
    }

    public Character(int livesLeft, int speed, ImageIcon currentSprite, Point spawnPoint) {
        this.livesLeft = livesLeft;
        this.speed = speed;
        this.currentSprite = currentSprite;
        this.spawnPoint = spawnPoint;
    }

  public int death(){
        livesLeft--;
        return livesLeft;
  }
    public enum Movement{UP,DOWN,LEFT,RIGHT}
    public enum Mode {SCATTER, CHASE, EATEN, FRIGHTENED, MANUAL}

    public void setSpawnPoint(Point newSpawnPoint) {
        spawnPoint.x = (int) newSpawnPoint.getX();
        spawnPoint.y = (int) newSpawnPoint.getY();
    }

    public Movement getCurrentMovement() {
        return currentMovement;
    }

    Movement currentMovement = Movement.DOWN;

    public void setCurrentMovement(Movement m){
        currentMovement = m;
    }
}
