import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.awt.geom.Point2D;
import java.util.*;

public class Gameboard extends JPanel implements KeyListener, ActionListener {

    private static final int DEFAULT_TIMER = 120;
    int score = 0;
    static int timer = DEFAULT_TIMER;
    private static final int TIMER_DELAY = (int) (timer * 1.5) / 9;
    int level = 0;
    JLabel scoreShowcase;
    JLabel timeShowcase = new JLabel();

    int countdown;
    private final Point pacmanDefaultSpawn = new Point(PacmanGame.GAME_WIDTH / 2 - 16, PacmanGame.SCOREBAR_HEIGHT + 12 * PacmanGame.SQUARE_SIZE);
    private final Point ghost1DefaultSpawn = new Point(PacmanGame.SQUARE_SIZE, PacmanGame.SCOREBAR_HEIGHT + PacmanGame.SQUARE_SIZE);
    private final Point ghost2DefaultSpawn = new Point(PacmanGame.SQUARE_SIZE, PacmanGame.SCOREBAR_HEIGHT + 14 * PacmanGame.SQUARE_SIZE);
    private final Point ghost3DefaultSpawn = new Point(PacmanGame.GAME_WIDTH - 2 * PacmanGame.SQUARE_SIZE, PacmanGame.SCOREBAR_HEIGHT + PacmanGame.SQUARE_SIZE);
    private final Point ghost4DefaultSpawn = new Point(PacmanGame.GAME_WIDTH / 2 - 16, PacmanGame.SCOREBAR_HEIGHT + 13 * PacmanGame.SQUARE_SIZE);

    int[] speeds = new int[]{4,4,5,6,7};
    Timer tm = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            countdown++;
            if (countdown == 10) {
                countdown = 0;
                timer--;
               timeShowcase.setText("TIME: " + timer/TIMER_DELAY);
            }
            if (timer == 0) {
                death = false;
                tm.stop();
                pacman.livesLeft--;
                if (pacman.livesLeft > 0) {
                    pacman.setSpawnPoint(pacmanDefaultSpawn);
                    timer = DEFAULT_TIMER;
                    tm.start();
                    death = true;
                } else {
                    System.out.println("Ну їх в баню, тих москалів. Втомився за ними бігати.");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    });
    int pacmanSpeed = 5;
    int dumplingsEaten = 0;

    Character pacman = new Character(PacmanGame.PACMAN_LIVES, pacmanSpeed, new ImageIcon("src/images/cossack1.png"), pacmanDefaultSpawn, new ImageIcon("src/images/cossack1.png").getImage().getWidth(this), new ImageIcon("src/images/cossack1.png").getImage().getHeight(this));
    //X selects String (vertical movement), Y selects char (horizontal movement)

    Rectangle wall;

    ArrayList<Character> characters;

    Character[][] levelSetup = new Character[][]{{
            pacman, new Character(1, speeds[0], new ImageIcon("src/images/chmonya1.png"), new Point((int) ghost1DefaultSpawn.getX(), (int) ghost1DefaultSpawn.getY())),
            new Character(1, speeds[0], new ImageIcon("src/images/chmonya1.png"), new Point((int) ghost2DefaultSpawn.getX(), (int) ghost2DefaultSpawn.getY())),
            new Character(1, speeds[0], new ImageIcon("src/images/chmonya1.png"), new Point((int) ghost3DefaultSpawn.getX(), (int) ghost3DefaultSpawn.getY()))
    },
            { pacman, new Character(1, speeds[1], new ImageIcon("src/images/chmonya1.png"), new Point((int) ghost1DefaultSpawn.getX(), (int) ghost1DefaultSpawn.getY())),
                    new Character(2, speeds[1], new ImageIcon("src/images/brute1.png"), new Point((int) ghost2DefaultSpawn.getX(), (int) ghost2DefaultSpawn.getY())),
                    new Character(1, speeds[1], new ImageIcon("src/images/chmonya1.png"), new Point((int) ghost3DefaultSpawn.getX(), (int) ghost3DefaultSpawn.getY()))
            },
            {
                    pacman, new Character(1, speeds[2], new ImageIcon("src/images/chmonya1.png"), new Point((int) ghost1DefaultSpawn.getX(), (int) ghost1DefaultSpawn.getY())),
                    new Character(2, speeds[2], new ImageIcon("src/images/brute1.png"), new Point((int) ghost2DefaultSpawn.getX(), (int) ghost2DefaultSpawn.getY())),
                    new Character(1, speeds[2], new ImageIcon("src/images/chmonya1.png"), new Point((int) ghost3DefaultSpawn.getX(), (int) ghost3DefaultSpawn.getY())),
                    new Character(2, speeds[2], new ImageIcon("src/images/brute1.png"),new Point((int) ghost4DefaultSpawn.getX(), (int) ghost4DefaultSpawn.getY()))

            },
            {
                    pacman, new Character(2, speeds[3], new ImageIcon("src/images/brute1.png"), new Point((int) ghost1DefaultSpawn.getX(), (int) ghost1DefaultSpawn.getY())),
                    new Character(2, speeds[3], new ImageIcon("src/images/brute1.png"), new Point((int) ghost2DefaultSpawn.getX(), (int) ghost2DefaultSpawn.getY())),
                    new Character(2, speeds[3], new ImageIcon("src/images/brute1.png"), new Point((int) ghost3DefaultSpawn.getX(), (int) ghost3DefaultSpawn.getY())),
                    new Character(2, speeds[3], new ImageIcon("src/images/brute1.png"), new Point((int) ghost4DefaultSpawn.getX(), (int) ghost4DefaultSpawn.getY()))
            }

    };


    ImageIcon dumpIcon = new ImageIcon("src/images/dumpling.png");
    Image dumpImage = dumpIcon.getImage();
    int dotDistance = (int) (PacmanGame.SQUARE_SIZE * 0.75 / 2d);

    int dotSize = PacmanGame.SQUARE_SIZE - 2 * dotDistance;
    Image dumpling = dumpImage.getScaledInstance((int) (dotSize * 1.2), (int) (dotSize * 1.2), Image.SCALE_SMOOTH);

    public Gameboard(int width, int height) {
        this.setSize(width, height);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        tm.addActionListener(this);
        this.add(timeShowcase);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    boolean levelWon = true;
    boolean death;

    private void doDrawing(Graphics g) {

        if (levelWon) {
            setUpLevel();
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bg.getImage(), 0, 0, this);

        printMaze(g2d);
        drawHunger(g2d);
        drawLives(g2d);
        for (Character c : characters) {
            g2d.drawImage(c.getCurrentSprite().getImage(), (int) c.getSpawnPoint().getX(), (int) c.getSpawnPoint().getY(), this);
        }
    }

    private void drawHunger(Graphics2D g2d) {

        int diff = 135;
        for (int i = 0; i < timer / TIMER_DELAY && i < 9; i++) {
            g2d.drawImage(dumpImage, diff + (10 + dumpImage.getWidth(this)) * i, ((PacmanGame.SCOREBAR_HEIGHT - dumpImage.getHeight(this)) / 2), this);
        }

    }

    ImageIcon bg = new ImageIcon("src/images/gamemap.png");
    ImageIcon heartIcon = new ImageIcon("src/images/cossackHeartFull.png");
    ImageIcon brokenHeartIcon = new ImageIcon("src/images/cossackHeartEmpty.png");

    private void drawLives(Graphics2D g2d) {
        int diff = 125;
        for (int i = 0; i < pacman.livesLeft; i++) {
            g2d.drawImage(heartIcon.getImage(), diff + (10 + heartIcon.getImage().getWidth(this)) * i, (int) ((PacmanGame.LEVEL_DATA.length + 0.25) * PacmanGame.SQUARE_SIZE + PacmanGame.SCOREBAR_HEIGHT), this);
        }

        if (pacman.livesLeft < 3) {
            for (int i = pacman.livesLeft; i < 3; i++) {
                g2d.drawImage(brokenHeartIcon.getImage(), diff + (10 + heartIcon.getImage().getWidth(this)) * i, (int) ((PacmanGame.LEVEL_DATA.length + 0.25) * PacmanGame.SQUARE_SIZE + PacmanGame.SCOREBAR_HEIGHT), this);
            }
        }
    }

    private void setUpLevel() {
        if(level<levelSetup.length) {
            PacmanGame.updateDots();
            pacman.setSpawnPoint(pacmanDefaultSpawn);
            level++;
            timer = DEFAULT_TIMER;
            levelWon = false;
            characters = new ArrayList<>();
            Collections.addAll(characters, levelSetup[level - 1]);
        } else {
            gameWon();
        }
    }

    private void gameWon() {
        tm.stop();
    }

    public void printMaze(Graphics2D g2d) {
//        g2d.fillRect(120,0,5,720);
//        g2d.fillRect(0,120,560,5);
        for (int i = 0; i < PacmanGame.levelData.length; i++) {
            for (int j = 0; j < PacmanGame.levelData[i].length; j++) {
                char current = PacmanGame.levelData[i][j];
//                if(current == PacmanGame.levelData[0][0])
//                    System.out.println(current);
                switch (current) {
                    case 'd' -> //g2d.setColor(dotColor);
                        //g2d.fillRect(j* PacmanGame.SQUARE_SIZE + dotDistance, PacmanGame.SCOREBAR_HEIGHT+i* PacmanGame.SQUARE_SIZE + dotDistance, dotSize, dotSize);
                        // SQUARE_SIZE/4
                            g2d.drawImage(dumpling, j * PacmanGame.SQUARE_SIZE + dotDistance, PacmanGame.SCOREBAR_HEIGHT + i * PacmanGame.SQUARE_SIZE + dotDistance, this);

                    /*case 'w' -> {
                        int height = PacmanGame.SQUARE_SIZE;
                        int width = PacmanGame.SQUARE_SIZE;
                        int x_pos = j * PacmanGame.SQUARE_SIZE;
                        int y_pos = PacmanGame.SCOREBAR_HEIGHT + i * PacmanGame.SQUARE_SIZE;

                        //lower border
                        if (i + 1 < PacmanGame.levelData.length)
                            if (PacmanGame.levelData[i + 1][j] != 'w') {
                                height -= 5;
                            }
                        //top border
                        if (i - 1 > 0)
                            if (PacmanGame.levelData[i - 1][j] != 'w') {
                                y_pos += 5;
                                height -= 5;
                            }
                        //left border
                        if (j - 1 > 0)
                            if (PacmanGame.levelData[i][j - 1] != 'w') {
                                x_pos += 5;
                                width -= 5;
                            }
                        //right border
                        if (j + 1 < PacmanGame.levelData[i].length)
                            if (PacmanGame.levelData[i][j + 1] != 'w') {
                                width -= 5;
                            }
//                    g2d.drawRect(x_pos, y_pos, width, height);
                        wall = new Rectangle(x_pos, y_pos, width, height);
                        g2d.draw(wall);
                    }*/
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (tm.isRunning())
                tm.stop();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (pacman.getCurrentMovement() != Character.Movement.UP) {
                if (pacman.getCurrentMovement() == Character.Movement.LEFT || pacman.getCurrentMovement() == Character.Movement.RIGHT) {
                    pacman.getSpawnPoint().x -= (pacman.getSpawnPoint().x + pacman.getCurrentSprite().getImage().getWidth(this) / 2) % PacmanGame.SQUARE_SIZE - pacman.getCurrentSprite().getImage().getWidth(this) / 2;
                }
                pacman.setCurrentMovement(Character.Movement.UP);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (pacman.getCurrentMovement() != Character.Movement.DOWN) {
                if (pacman.getCurrentMovement() == Character.Movement.LEFT || pacman.getCurrentMovement() == Character.Movement.RIGHT) {
                    pacman.getSpawnPoint().x -= (pacman.getSpawnPoint().x + pacman.getCurrentSprite().getImage().getWidth(this) / 2) % PacmanGame.SQUARE_SIZE - pacman.getCurrentSprite().getImage().getWidth(this) / 2;
                }
                pacman.setCurrentMovement(Character.Movement.DOWN);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (pacman.getCurrentMovement() != Character.Movement.LEFT) {
                if (pacman.getCurrentMovement() == Character.Movement.UP || pacman.getCurrentMovement() == Character.Movement.DOWN) {
                    pacman.getSpawnPoint().y -= (pacman.getSpawnPoint().y + pacman.getCurrentSprite().getImage().getHeight(this) / 2) % PacmanGame.SQUARE_SIZE - pacman.getCurrentSprite().getImage().getHeight(this) / 2;
                }
                pacman.setCurrentMovement(Character.Movement.LEFT);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (pacman.getCurrentMovement() != Character.Movement.RIGHT) {
                if (pacman.getCurrentMovement() == Character.Movement.UP || pacman.getCurrentMovement() == Character.Movement.DOWN) {
                    pacman.getSpawnPoint().y -= (pacman.getSpawnPoint().y + pacman.getCurrentSprite().getImage().getHeight(this) / 2) % PacmanGame.SQUARE_SIZE - pacman.getCurrentSprite().getImage().getHeight(this) / 2;
                }
                pacman.setCurrentMovement(Character.Movement.RIGHT);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            tm.start();
        }
    }


    public void actionPerformed(ActionEvent e) {
        moveCharacters();
        checkDumplingsCollisions();
        checkEnemiesCollisions();
        determineEnemiesPoint();
        repaint();
    }

   private void checkEnemiesCollisions() {
      for(Character c:characters){
    if(c!=pacman){
        if (pacman.getSpawnPoint().x >= 0 && pacman.getSpawnPoint().x + pacman.getCurrentSprite().getImage().getWidth(this) <= PacmanGame.WINDOW_WIDTH) {
            int pacX = (pacman.getSpawnPoint().x + pacman.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE;
            int pacY = (pacman.getSpawnPoint().y + pacman.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE;
            int charX = (c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE;
            int charY = (c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE;

            if(pacX==charX && pacY==charY){
                enemyEaten(c);
            }
        }
}
       }



   }

    private void enemyEaten(Character toDelete) {
        if(toDelete.death() == 0) {
            ArrayList<Character> newCharacters = new ArrayList<Character>();

            for (Character c : characters) {
                if (c != toDelete)
                    newCharacters.add(c);

            }
            characters = newCharacters;
            if (characters.size() == 1)
                levelWon = true;
        }
    }

    private void checkDumplingsCollisions() {
        //Eating dumplings; eating 5 gives a 20 second boost
        if (pacman.getSpawnPoint().x >= 0 && pacman.getSpawnPoint().x + pacman.getCurrentSprite().getImage().getWidth(this) <= PacmanGame.WINDOW_WIDTH) {
            if (PacmanGame.levelData[(pacman.getSpawnPoint().y + pacman.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                    [(pacman.getSpawnPoint().x + pacman.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE] == 'd') {
                PacmanGame.levelData[(pacman.getSpawnPoint().y + pacman.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                        [(pacman.getSpawnPoint().x + pacman.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE] = ' ';
                dumplingsEaten++;
                if (dumplingsEaten == 10) {
//                    score+=100;
//                    scoreShowcase.setText(score+" pts");
                    timer += 10;
                    dumplingsEaten = 0;
                }
            }
        }

//        }
    }

    private void moveCharacters() {
        for (Character c : characters) {
c.setSpawnPoint(determinePoint(c));

        }

    }

    private Point determinePoint(Character c) {
        return determinePoint(c,c.getCurrentMovement());
    }

    private Point determinePoint(Character c, Character.Movement currentMovement) {
        int diff = 2;
        Point spawnPoint = new Point((int) c.getSpawnPoint().getX(), (int) c.getSpawnPoint().getY());
        if (currentMovement != null) {
            try {
                switch (currentMovement) {
                    case RIGHT -> {
                        spawnPoint.x += c.speed;
                        if (PacmanGame.levelData[(spawnPoint.y + c.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                                [(spawnPoint.x + c.getCurrentSprite().getImage().getWidth(this) - 5) / PacmanGame.SQUARE_SIZE] == 'w') {
                            spawnPoint.x -= ((spawnPoint.x + c.getCurrentSprite().getImage().getWidth(this)) % PacmanGame.SQUARE_SIZE - diff);
                        }
                    }
                    case LEFT -> {
                        spawnPoint.x -= c.speed;
                        if (PacmanGame.levelData[(spawnPoint.y + c.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                                [(spawnPoint.x + 5) / PacmanGame.SQUARE_SIZE] == 'w') {
                            spawnPoint.x += (PacmanGame.SQUARE_SIZE - (spawnPoint.x % PacmanGame.SQUARE_SIZE) - diff);
                        }
                    }
                    case UP -> {
                        spawnPoint.y -= c.speed;
                        if (PacmanGame.levelData[(spawnPoint.y - PacmanGame.SCOREBAR_HEIGHT + 5) / PacmanGame.SQUARE_SIZE]
                                [(spawnPoint.x + c.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE] == 'w') {
                            spawnPoint.y += (PacmanGame.SQUARE_SIZE - (spawnPoint.y % PacmanGame.SQUARE_SIZE) - diff);
                        }
                    }
                    case DOWN -> {
                        spawnPoint.y += c.speed;

                        if (PacmanGame.levelData[(spawnPoint.y + c.getCurrentSprite().getImage().getHeight(this) - PacmanGame.SCOREBAR_HEIGHT - 5) / PacmanGame.SQUARE_SIZE]
                                [(spawnPoint.x + c.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE] == 'w') {
                            spawnPoint.y -= ((spawnPoint.y + c.getCurrentSprite().getImage().getHeight(this)) % PacmanGame.SQUARE_SIZE - diff);


                        }
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                if (spawnPoint.getX() <= -c.getCurrentSprite().getImage().getWidth(this))
                    spawnPoint.x = PacmanGame.WINDOW_WIDTH - c.speed;
                else if (spawnPoint.getX() >= PacmanGame.WINDOW_WIDTH)
                    spawnPoint.x = -c.getCurrentSprite().getImage().getWidth(this) + c.speed;
                spawnPoint.y = PacmanGame.SQUARE_SIZE*14+PacmanGame.SCOREBAR_HEIGHT+diff;
            }


        }
        return spawnPoint;
    }


    private void determineEnemiesPoint() {

        for (Character c : characters) {
            if (c != pacman) {
                try {
                    //up, left, right, down
                    double[] distance = new double[]{-1, -1, -1, -1};

                    switch (c.getCurrentMovement()) {
                        case UP -> {
                            if (leftIsClear(c))
                                distance[1] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.LEFT).getX(), determinePoint(c, Character.Movement.LEFT).getY());
                            if (rightIsClear(c))
                                distance[2] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.RIGHT).getX(), determinePoint(c, Character.Movement.RIGHT).getY());
                            if (upIsClear(c))
                                distance[0] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.DOWN).getX(), determinePoint(c, Character.Movement.DOWN).getY());
                        }
                        case RIGHT -> {
                            if (upIsClear(c))
                                distance[0] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.UP).getX(), determinePoint(c, Character.Movement.UP).getY());
                            if (rightIsClear(c))
                                distance[2] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.LEFT).getX(), determinePoint(c, Character.Movement.LEFT).getY());
                            if (downIsClear(c))
                                distance[3] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.DOWN).getX(), determinePoint(c, Character.Movement.DOWN).getY());
                        }
                        case LEFT -> {
                            if (upIsClear(c))
                                distance[0] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.UP).getX(), determinePoint(c, Character.Movement.UP).getY());
                            if (leftIsClear(c))
                                distance[1] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.RIGHT).getX(), determinePoint(c, Character.Movement.RIGHT).getY());
                            if (downIsClear(c))
                                distance[3] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.DOWN).getX(), determinePoint(c, Character.Movement.DOWN).getY());
                        }
                        case DOWN -> {
                            if (downIsClear(c))
                                distance[3] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.UP).getX(), determinePoint(c, Character.Movement.UP).getY());
                            if (leftIsClear(c))
                                distance[1] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.LEFT).getX(), determinePoint(c, Character.Movement.LEFT).getY());
                            if (rightIsClear(c))
                                distance[2] = Point2D.distance(pacman.getSpawnPoint().getX(), pacman.getSpawnPoint().getY(), determinePoint(c, Character.Movement.RIGHT).getX(), determinePoint(c, Character.Movement.RIGHT).getY());

                        }
                    }
    OptionalDouble max = Arrays.stream(distance).max();
                    if (max.getAsDouble() == distance[0]) {
                        c.setCurrentMovement(Character.Movement.UP);
                    } else if (max.getAsDouble() == distance[1]) {
                        c.setCurrentMovement(Character.Movement.LEFT);
                    } else if (max.getAsDouble() == distance[2]) {
                        c.setCurrentMovement(Character.Movement.RIGHT);
                    } else if (max.getAsDouble() == distance[3]) {
                        c.setCurrentMovement(Character.Movement.DOWN);
                }

            }catch (IndexOutOfBoundsException e){
                    c.getSpawnPoint().y = PacmanGame.SQUARE_SIZE*14+PacmanGame.SCOREBAR_HEIGHT+2;
            }
        }
    }
    }

    private boolean upIsClear(Character c) {
        Point toCompare = determinePoint(c, Character.Movement.UP);
        if(toCompare.getY()==c.getSpawnPoint().getY()) {
            return false;
        } else return true;
    }

    private boolean downIsClear(Character c) {
        Point toCompare = determinePoint(c, Character.Movement.DOWN);
        if(toCompare.getY()==c.getSpawnPoint().getY()) {
            return false;
        } else return true;
    }

    private boolean leftIsClear(Character c) {
        Point toCompare = determinePoint(c, Character.Movement.LEFT);
        if(toCompare.getX()==c.getSpawnPoint().getX() ) {
            return false;
        } else return true;
    }

    private boolean rightIsClear(Character c) {
        Point toCompare = determinePoint(c, Character.Movement.RIGHT);
        if(toCompare.getX()==c.getSpawnPoint().getX()) {
            return false;
        } else return true;
    }
}


    /*
    @Override
    public void actionPerformed(ActionEvent e) {
        
        
        
        
        if(pacman.currentMovement == Character.Movement.RIGHT) {
            if (pacmanSpeed == 0) {
                pacmanSpeed = 4;
            }
            if(pacman.getSpawnPoint().getX() == PacmanGame.LEVEL_DATA.length-PacmanGame.SQUARE_SIZE || PacmanGame.levelData[(int) map_y][(int) map_x +1] == 'w') {
                pacmanSpeed = 0;
                if (!isColliding) {
                    pacman.getSpawnPoint().x += 12;
                    isColliding = true;
                }
            }
//            collide = pacman.getCollisions();
//            if (collide.intersects(wall)) {
//                pacmanSpeed=0;
//                isColliding=true;
//            }
            else {
                isColliding = false;
                //Change cossack's position relative to the map (Y is horizontal movement; addition = moving right
                map_x += 1 / 7d;
                //Change cossack's position relative to the app (X is horizontal)
                pacman.getSpawnPoint().x = pacman.getSpawnPoint().x + pacmanSpeed;
            }
        }


        else if(pacman.currentMovement == Character.Movement.LEFT) {
            if(pacmanSpeed==0) {
                pacmanSpeed = 4;
            }
            if(pacman.getSpawnPoint().getX() == PacmanGame.SQUARE_SIZE || PacmanGame.levelData[(int) map_y][(int) map_x -1] == 'w') {
                pacmanSpeed = 0;
                if(!isColliding) {
                    pacman.getSpawnPoint().x -= 12;
                    isColliding=true;
                }
            }
            else {
                isColliding=false;
                //Change cossack's position relative to the map (Y is horizontal movement); subtraction = moving left
                map_x -= 1 / 7d;
                //Change cossack's position relative to the app (X is horizontal)
                pacman.getSpawnPoint().x = pacman.getSpawnPoint().x - pacmanSpeed;
            }
        }
        else if(pacman.currentMovement == Character.Movement.UP) {
            if(pacmanSpeed==0) {
                pacmanSpeed = 4;
            }
            if(pacman.getSpawnPoint().getY() == PacmanGame.SCOREBAR_HEIGHT+PacmanGame.SQUARE_SIZE || PacmanGame.levelData[(int) map_y -1][(int) map_x] == 'w') {
                pacmanSpeed = 0;
                if(!isColliding) {
                    isColliding=true;
                    pacman.getSpawnPoint().y -= 12;
                }
            }
            else {
                isColliding=false;
                map_y -= 1 / 7d;
                pacman.getSpawnPoint().y = pacman.getSpawnPoint().y - pacmanSpeed;
            }
        }
        else if(pacman.currentMovement == Character.Movement.DOWN) {
            if(pacmanSpeed==0) {
                pacmanSpeed = 4;
            }
            if(pacman.getSpawnPoint().getY() == PacmanGame.WINDOW_HEIGHT-PacmanGame.SCOREBAR_HEIGHT-PacmanGame.SQUARE_SIZE || PacmanGame.levelData[(int) map_y +1][(int) map_x] == 'w') {
                pacmanSpeed = 0;
                if(!isColliding) {
                    pacman.getSpawnPoint().y += 12;
                    isColliding=true;
                }
            }
            else {
                isColliding=false;
                map_y += 1 / 7d;
                pacman.getSpawnPoint().y = pacman.getSpawnPoint().y + pacmanSpeed;
            }
        }
        //X selects String (vertical movement), Y selects char (horizontal movement)
        //Offset X by 3 squares to adjust spawn point
        map_y = (pacman.getSpawnPoint().y-3*PacmanGame.SQUARE_SIZE)/28d;
        map_x = pacman.getSpawnPoint().x/28d;
//        pacman.getSpawnPoint().x = Math.round(pacman.getSpawnPoint().x);
        map_x = Math.round(map_x);
//        pacman.getSpawnPoint().y = Math.round(pacman.getSpawnPoint().y);
        map_y = Math.round(map_y);
        //Eating dumplings; eating 5 gives a 20 second boost
        if(PacmanGame.levelData[(int) map_y][(int) map_x] == 'd') {
            PacmanGame.levelData[(int) map_y][(int) map_x] = ' ';
            dumplingsEaten++;
            if(dumplingsEaten==10) {
                timer += 10;
                dumplingsEaten = 0;
            }
        }

//        if(pacman.getCollisions().intersects(chmonya.getCollisions())) {
//            try {
//                tm.wait(2);
//            } catch (InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
//            timer += 100;
//            chmonya.livesLeft--;
//            if(chmonya.livesLeft == 0) {
//                //chmonya dies
//
//            }
//        }
        repaint();
    }
}
*/


//class Wall {
//    private int x, y, width, height;
//
//    public Wall(int x, int y, int width, int height) {
//        this.x=x;
//        this.y=y;
//        this.width=width;
//        this.height=height;
//    }
//
//    public void draw(Graphics2D g2d) {
//        g2d.draw(new Rectangle(this.x, this.y, this.width, this.height));
//    }
//}


