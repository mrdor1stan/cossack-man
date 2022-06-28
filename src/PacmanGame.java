
import javax.swing.*;
import java.awt.*;

public class PacmanGame {
    static String[] LEVEL_DATA = new String[]{
            "wwwwwwwwwwwwwwwwwwwwwwwwwwww",
            "wddddddddddddwwddddddddddddw",
            "wdwwwwdwwwwwdwwdwwwwwdwwwwdw",
            "wdwwwwdwwwwwdwwdwwwwwdwwwwdw",
            "wdwwwwdwwwwwdwwdwwwwwdwwwwdw",
            "wddddddddddddddddddddddddddw",
            "wdwwwwdwwdwwwwwwwwdwwdwwwwdw",
            "wdwwwwdwwdwwwwwwwwdwwdwwwwdw",
            "wddddddwwddddwwddddwwddddddw",
            "wwwwwwdwwwww ww wwwwwdwwwwww",
            "     wdwwwww ww wwwwwdw     ",
            "     wdww          wwdw     ",
            "     wdww          wwdw     ",
            "wwwwwwdww www  www wwdwwwwww",
            "      d   w      w   d      ",
            "wwwwwwdww w      w wwdwwwwww",
            "     wdww wwwwwwww wwdw     ",
            "     wdww          wwdw     ",
            "     wdww wwwwwwww wwdw     ",
            "wwwwwwdww wwwwwwww wwdwwwwww",
            "wddddddddddddwwddddddddddddw",
            "wdwwwwdwwwwwdwwdwwwwwdwwwwdw",
            "wdwwwwdwwwwwdwwdwwwwwdwwwwdw",
            "wdddwwddddddd  dddddddwwdddw",
            "wwwdwwdwwdwwwwwwwwdwwdwwdwww",
            "wwwdwwdwwdwwwwwwwwdwwdwwdwww",
            "wddddddwwddddwwddddwwddddddw",
            "wdwwwwwwwwwwdwwdwwwwwwwwwwdw",
            "wdwwwwwwwwwwdwwdwwwwwwwwwwdw",
            "wddddddddddddddddddddddddddw",
            "wwwwwwwwwwwwwwwwwwwwwwwwwwww"
    };

    static char[][] levelData = new char[LEVEL_DATA.length][LEVEL_DATA[0].length()];

    public static final int SQUARE_SIZE = 28;
    public static final int SCOREBAR_HEIGHT = 3*SQUARE_SIZE;
    public static final int LIVES_BAR_HEIGHT = 2*SQUARE_SIZE;
    public static final int WINDOW_HEIGHT = LEVEL_DATA.length*SQUARE_SIZE + SCOREBAR_HEIGHT+LIVES_BAR_HEIGHT;
    public static final int WINDOW_WIDTH = LEVEL_DATA[0].length()*SQUARE_SIZE;

    public static final int GAME_WIDTH = PacmanGame.WINDOW_WIDTH;
    public static final int GAME_HEIGHT = PacmanGame.WINDOW_HEIGHT-PacmanGame.SCOREBAR_HEIGHT-PacmanGame.LIVES_BAR_HEIGHT;
    public static final int PACMAN_LIVES = 3;

    public static void main(String[] args) {
        inspectLevelDataError();
        JFrame gamefield = new JFrame("Cossack-Man"); //Pa-Cossack-Man?
        gamefield.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gamefield.add(new Gameboard(WINDOW_WIDTH, WINDOW_HEIGHT));
        gamefield.setSize(800, 1034);
        gamefield.setVisible(true);
        gamefield.setResizable(false);
        gamefield.setLocationRelativeTo(null);
    }

    private static void inspectLevelDataError() {
        int length = LEVEL_DATA[0].length();
        for(int i=1;i< LEVEL_DATA.length;i++){
            if(LEVEL_DATA[i].length()!=length)throw new RuntimeException("Level is not built correctly: line "+i);
        }
        for(int i=0;i< LEVEL_DATA.length;i++){
            for(int j=0;j<length;j++){
                levelData[i][j]=LEVEL_DATA[i].charAt(j);
            }
        }
    }
}
