import javax.swing.*;

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
            "     wdww www  www wwdw     ",
            "wwwwwwdww w      w wwdwwwwww",
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

    public static final int SQUARE_SIZE = 20;
    public static final int SCOREBAR_HEIGHT = 3*SQUARE_SIZE;
    public static final int LIVES_BAR_HEIGHT = 2*SQUARE_SIZE;
    public static final int WINDOW_HEIGHT = LEVEL_DATA.length*SQUARE_SIZE + SCOREBAR_HEIGHT+LIVES_BAR_HEIGHT;
    public static final int WINDOW_WIDTH = LEVEL_DATA[0].length()*SQUARE_SIZE;
    public static final int PACMAN_LIVES = 3;

    public static void main(String[] args) {

        inspectLevelDataError();
        JFrame gamefield = new JFrame("Pac, You're It");

        gamefield.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gamefield.add(new Gameboard(WINDOW_WIDTH, WINDOW_HEIGHT));
        gamefield.setSize(576, 758);
        gamefield.setVisible(true);
        System.out.println(gamefield.getSize());
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
