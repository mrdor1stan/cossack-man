import javax.swing.*;
import java.awt.*;

public class Character{
   private int livesLeft;
   private final int speed;

    public int getLivesLeft() {
        return livesLeft;
    }

    public int getSpeed() {
        return speed;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public ImageIcon getCurrentSprite() {
        return currentSprite;
    }

    private final ImageIcon currentSprite;
    private Point spawnPoint;

    private boolean invincible;

    public Point getSpawnPoint() {
        return spawnPoint;
    }

    public Character(int livesLeft, int speed, ImageIcon currentSprite, Point spawnPoint) {
        this.livesLeft = livesLeft;
        this.speed = speed;
        this.currentSprite = currentSprite;
        this.spawnPoint = new Point((int) spawnPoint.getX(), (int) spawnPoint.getY());
    }

  public int death(){
        livesLeft--;
        return livesLeft;
  }

    public void setLivesLeft(int livesLeft) {
        this.livesLeft = livesLeft;
    }

    public enum Movement{UP,DOWN,LEFT,RIGHT}
    public void setSpawnPoint(Point newSpawnPoint) {
        spawnPoint.x = (int) newSpawnPoint.getX();
        spawnPoint.y = (int) newSpawnPoint.getY();
    }

    public Movement getCurrentMovement() {
        return currentMovement;
    }

    private Movement currentMovement = Movement.DOWN;

    public void setCurrentMovement(Movement m){
        currentMovement = m;
    }
}
