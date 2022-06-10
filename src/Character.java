import java.awt.*;

public class Character {
    Image sprite_up;
    Image sprite_down;
    Image sprite_left;
    Image sprite_right;
    Point spawnPoint;

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
