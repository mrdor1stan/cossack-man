
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
            "wwwwwwdwwwww ww wwwwwdwwwwww",
            "wwwwwwdww          wwdwwwwww",
            "wwwwwwdww www  www wwdwwwwww",
            "wwwwwwdww wwwwwwww wwdwwwwww",
            "      d   wwwwwwww   d      ",
            "wwwwwwdww wwwwwwww wwdwwwwww",
            "wwwwwwdww wwwwwwww wwdwwwwww",
            "wwwwwwdww          wwdwwwwww",
            "wwwwwwdww wwwwwwww wwdwwwwww",
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
    public static final int PACMAN_LIVES = 1;
    private static final ImageIcon windowIcon = new ImageIcon("src/images/dumpling.png");

    static JFrame gamefield;

    static JPanel menu;
    static JLabel introText = new JLabel("Press SPACE to begin");

    static JLabel levelText, scoreText;
    static Clip clip, menuMusic, gameloop;

    static AudioInputStream audioIn;
    static Gameboard board = new Gameboard(WINDOW_WIDTH, WINDOW_HEIGHT);

    public static void main(String[] args) {
        inspectLevelDataError();
        gamefield = new JFrame("Cossack-Man");
        gamefield.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gamefield.setSize(798, 1032);
        gamefield.setVisible(true);
        gamefield.setResizable(false);
        gamefield.setLocationRelativeTo(null);
        gamefield.setIconImage(windowIcon.getImage());
        introText.setFont(new Font("Monospaced", Font.BOLD, 24));
        introText.setForeground(Color.WHITE);
        introText.setHorizontalAlignment(SwingConstants.CENTER);
        introText.setVerticalAlignment(SwingConstants.CENTER);
        levelText = new JLabel();
        levelText.setForeground(Color.WHITE);
        levelText.setFont(new Font("Monospaced", Font.BOLD, 30));
        scoreText = new JLabel();
        scoreText.setForeground(Color.WHITE);
        scoreText.setFont(new Font("Monospaced", Font.BOLD, 24));

        if(!Gameboard.gameReset) {
            final BufferedImage mainMenu;
            try {
                mainMenu = ImageIO.read(new File("src/images/title.png"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            menu = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(mainMenu, 0, 0, null);
                }
            };
            menu.add(introText, BorderLayout.CENTER);
            gamefield.add(menu);
            {
                try {
                    audioIn = AudioSystem.getAudioInputStream(new File("src/audio/menutheme.wav"));
                    menuMusic = AudioSystem.getClip();
                    menuMusic.open(audioIn);
                    FloatControl audioControl = (FloatControl) menuMusic.getControl(FloatControl.Type.MASTER_GAIN);
                    audioControl.setValue(-25.0f);
                    menuMusic.start();
                    menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                }
            }
            gamefield.revalidate();
            gamefield.repaint();
            gamefield.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (!Gameboard.win && !Gameboard.lose) {
                        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                            {
                                try {
                                    menuMusic.stop();
                                    audioIn = AudioSystem.getAudioInputStream(new File("src/audio/start.wav"));
                                    clip = AudioSystem.getClip();
                                    clip.open(audioIn);
                                    FloatControl audioControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                                    audioControl.setValue(-25.0f);
                                    clip.start();
                                    audioIn = AudioSystem.getAudioInputStream(new File("src/audio/gameloop.wav"));
                                    gameloop = AudioSystem.getClip();
                                    gameloop.open(audioIn);
                                    audioControl = (FloatControl) gameloop.getControl(FloatControl.Type.MASTER_GAIN);
                                    audioControl.setValue(-25.0f);
                                    gameloop.start();
                                    gameloop.loop(Clip.LOOP_CONTINUOUSLY);
                                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            gamefield.remove(menu);
                            board.setLayout(new BorderLayout());
                            scoreText.setVerticalAlignment(SwingConstants.TOP);
                            scoreText.setHorizontalAlignment(SwingConstants.RIGHT);
                            board.add(scoreText);
                            levelText.setVerticalAlignment(SwingConstants.BOTTOM);
                            board.add(levelText, BorderLayout.LINE_END);
                            gamefield.add(board);
                            gamefield.revalidate();
                            gamefield.repaint();
                            board.requestFocusInWindow();
                        }
                    }
                }
            });
        }
    }

   public static void updateDots(){
       int length = LEVEL_DATA[0].length();
        for(int i=0;i< LEVEL_DATA.length;i++){
            for(int j=0;j<length;j++){
                levelData[i][j]=LEVEL_DATA[i].charAt(j);
            }
        }
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
