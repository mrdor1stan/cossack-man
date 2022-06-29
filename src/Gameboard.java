import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Gameboard extends JPanel implements KeyListener, ActionListener {


    static int timer = 100;
    private static final int TIMER_DELAY = (int)(timer*1.5)/9;
int level = 0;
    JLabel timeShowcase;

    int countdown;
    private final Point pacmanDefaultSpawn = new Point(PacmanGame.GAME_WIDTH / 2 - 16, PacmanGame.SCOREBAR_HEIGHT + 23 * PacmanGame.SQUARE_SIZE);
    private final Point ghost2DefaultSpawn = new Point(PacmanGame.GAME_WIDTH / 2 - 16, PacmanGame.SCOREBAR_HEIGHT + 15 * PacmanGame.SQUARE_SIZE);
    private final Point ghost1DefaultSpawn = new Point(PacmanGame.GAME_WIDTH / 2 - 16 - 32 * 2, PacmanGame.SCOREBAR_HEIGHT + 15 * PacmanGame.SQUARE_SIZE);
    private final Point ghost3DefaultSpawn = new Point(PacmanGame.GAME_WIDTH / 2 - 16 + 32 * 2, PacmanGame.SCOREBAR_HEIGHT + 15 * PacmanGame.SQUARE_SIZE);
    private final Point ghost4DefaultSpawn = new Point(PacmanGame.GAME_WIDTH / 2 - 16, PacmanGame.SCOREBAR_HEIGHT + 13 * PacmanGame.SQUARE_SIZE);

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
                death=false;
                tm.stop();
                pacman.livesLeft--;
                if (pacman.livesLeft > 0) {
                    pacman.setSpawnPoint(pacmanDefaultSpawn);
                    timer = 100;
                    tm.start();
                    death=true;
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
    int pacmanSpeed = 6;
    int dumplingsEaten = 0;

    Character pacman = new Character(PacmanGame.PACMAN_LIVES, pacmanSpeed, new ImageIcon("src/images/cossack1.png"), pacmanDefaultSpawn, new ImageIcon("src/images/cossack1.png").getImage().getWidth(this), new ImageIcon("src/images/cossack1.png").getImage().getHeight(this));
    //X selects String (vertical movement), Y selects char (horizontal movement)

    Rectangle wall;

    ArrayList<Character> characters;

   Character[] level1Setup = new Character[]{
            pacman, new Character(1, 5, new ImageIcon("src/images/chmonya1.png"), ghost1DefaultSpawn),
            new Character(1, 5, new ImageIcon("src/images/chmonya1.png"), ghost2DefaultSpawn),
            new Character(1, 5, new ImageIcon("src/images/chmonya1.png"), ghost3DefaultSpawn)

    };

    Character[] level2Setup = new Character[]{
            pacman, new Character(1, 3, new ImageIcon("src/images/chmonya1.png"), ghost1DefaultSpawn),
            new Character(2, 3, new ImageIcon("src/images/brute1.png"), ghost2DefaultSpawn),
            new Character(1, 3, new ImageIcon("src/images/chmonya1.png"), ghost3DefaultSpawn)

    };
    Character[] level3Setup = new Character[]{
            pacman, new Character(1, 3, new ImageIcon("src/images/chmonya1.png"), ghost1DefaultSpawn),
            new Character(2, 3, new ImageIcon("src/images/brute1.png"), ghost2DefaultSpawn),
            new Character(1, 3, new ImageIcon("src/images/chmonya1.png"), ghost3DefaultSpawn),
            new Character(2, 3, new ImageIcon("src/images/brute1.png"), ghost4DefaultSpawn),

    };
    Character[] level4Setup = new Character[]{
            pacman, new Character(2, 3, new ImageIcon("src/images/brute1.png"), ghost1DefaultSpawn),
            new Character(2, 3, new ImageIcon("src/images/brute1.png"), ghost2DefaultSpawn),
            new Character(2, 3, new ImageIcon("src/images/brute1.png"), ghost3DefaultSpawn),
            new Character(2, 3, new ImageIcon("src/images/brute1.png"), ghost4DefaultSpawn),

    };

    ImageIcon dumpIcon = new ImageIcon("src/images/dumpling.png");
    Image dumpImage = dumpIcon.getImage();
    int dotDistance = (int) (PacmanGame.SQUARE_SIZE * 0.75 / 2d);

    int dotSize = PacmanGame.SQUARE_SIZE - 2 * dotDistance;
    Image dumpling = dumpImage.getScaledInstance((int) (dotSize * 1.2), (int) (dotSize * 1.2), Image.SCALE_SMOOTH);

    public Gameboard(int width, int height) {
        this.setSize(width, height);
        timeShowcase = new JLabel("TIME: " + timer);
        Dimension size = timeShowcase.getPreferredSize();
        timeShowcase.setBounds(100, 100, size.width, size.height);
        timeShowcase.setFont(new Font("Sans Serif", Font.BOLD, 16));
        add(timeShowcase);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        tm.addActionListener(this);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    boolean levelWon=true;
    boolean death;
    private void doDrawing(Graphics g) {

        if(levelWon){
            setUpLevel();
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(bg.getImage(),0,0,this);

        printMaze(g2d);
        drawHunger(g2d);
        drawLives(g2d);
        for (Character c : characters) {
            g2d.drawImage(c.getCurrentSprite().getImage(), (int) c.getSpawnPoint().getX(), (int) c.getSpawnPoint().getY(), this);
        }
    }

    private void drawHunger(Graphics2D g2d) {

        int diff = 135;
        for(int i=0;i<timer/TIMER_DELAY && i<9; i++) {
            g2d.drawImage(dumpImage, diff + (10 + dumpImage.getWidth(this))*i, (int) ((PacmanGame.SCOREBAR_HEIGHT-dumpImage.getHeight(this))/2), this);
        }

    }

    ImageIcon bg = new ImageIcon("src/images/background.jpg");
    ImageIcon heartIcon = new ImageIcon("src/images/cossackHeartFull.png");
    ImageIcon brokenHeartIcon = new ImageIcon("src/images/cossackHeartEmpty.png");
    private void drawLives(Graphics2D g2d) {
        int diff = 125;
        for(int i=0;i< pacman.livesLeft; i++) {
            g2d.drawImage(heartIcon.getImage(), diff + (10 + heartIcon.getImage().getWidth(this))*i, (int) ((PacmanGame.LEVEL_DATA.length+0.25)* PacmanGame.SQUARE_SIZE + PacmanGame.SCOREBAR_HEIGHT), this);
        }

        if(pacman.livesLeft<3){
            for(int i=pacman.livesLeft;i< 3; i++) {
                g2d.drawImage(brokenHeartIcon.getImage(), diff + (10 + heartIcon.getImage().getWidth(this)) * i, (int) ((PacmanGame.LEVEL_DATA.length + 0.25) * PacmanGame.SQUARE_SIZE + PacmanGame.SCOREBAR_HEIGHT), this);
            }
        }
    }

    private void setUpLevel() {
        level++;
        levelWon=false;
        switch(level){
            case 1-> {
                characters=new ArrayList<>();
                for (Character c: level1Setup) {
                    characters.add(c);
                }

            }
            case 2-> {
                characters=new ArrayList<>();
                for (Character c: level2Setup) {
                characters.add(c);
            } }
            case 3-> {
                characters = new ArrayList<>();
                for (Character c : level3Setup) {
                    characters.add(c);
                }
            }
            case 4-> {
                characters = new ArrayList<>();
                for (Character c : level4Setup) {
                    characters.add(c);
                }
            }
        }
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
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            tm.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (tm.isRunning())
                tm.stop();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (pacman.getCurrentMovement() != Character.Movement.UP) {
                if(pacman.getCurrentMovement() == Character.Movement.LEFT || pacman.getCurrentMovement() == Character.Movement.RIGHT){
                    pacman.getSpawnPoint().x -= (pacman.getSpawnPoint().x+pacman.getCurrentSprite().getImage().getWidth(this)/2)%PacmanGame.SQUARE_SIZE-pacman.getCurrentSprite().getImage().getWidth(this)/2;
                }
                pacman.setCurrentMovement(Character.Movement.UP);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (pacman.getCurrentMovement() != Character.Movement.DOWN) {
                if(pacman.getCurrentMovement() == Character.Movement.LEFT || pacman.getCurrentMovement() == Character.Movement.RIGHT){
                    pacman.getSpawnPoint().x -= (pacman.getSpawnPoint().x+pacman.getCurrentSprite().getImage().getWidth(this)/2)%PacmanGame.SQUARE_SIZE-pacman.getCurrentSprite().getImage().getWidth(this)/2;
                }
                pacman.setCurrentMovement(Character.Movement.DOWN);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (pacman.getCurrentMovement() != Character.Movement.LEFT) {
                if(pacman.getCurrentMovement() == Character.Movement.UP || pacman.getCurrentMovement() == Character.Movement.DOWN){
                    pacman.getSpawnPoint().y -= (pacman.getSpawnPoint().y+pacman.getCurrentSprite().getImage().getHeight(this)/2)%PacmanGame.SQUARE_SIZE-pacman.getCurrentSprite().getImage().getHeight(this)/2;
                }
                pacman.setCurrentMovement(Character.Movement.LEFT);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (pacman.getCurrentMovement() != Character.Movement.RIGHT) {
                if(pacman.getCurrentMovement() == Character.Movement.UP || pacman.getCurrentMovement() == Character.Movement.DOWN){
                    pacman.getSpawnPoint().y -= (pacman.getSpawnPoint().y+pacman.getCurrentSprite().getImage().getHeight(this)/2)%PacmanGame.SQUARE_SIZE-pacman.getCurrentSprite().getImage().getHeight(this)/2;
                }
                pacman.setCurrentMovement(Character.Movement.RIGHT);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


    public void actionPerformed(ActionEvent e) {
       determineEnemiesPoint();
        moveCharacters();
        checkCollisions();
        repaint();
    }

    private void checkCollisions() {
        //Eating dumplings; eating 5 gives a 20 second boost
        if(pacman.getSpawnPoint().x>=0 && pacman.getSpawnPoint().x + pacman.getCurrentSprite().getImage().getWidth(this)<=PacmanGame.WINDOW_WIDTH) {
            if (PacmanGame.levelData[(pacman.getSpawnPoint().y + pacman.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                    [(pacman.getSpawnPoint().x + pacman.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE] == 'd') {
                PacmanGame.levelData[(pacman.getSpawnPoint().y + pacman.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                        [(pacman.getSpawnPoint().x + pacman.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE] = ' ';
                dumplingsEaten++;
                if (dumplingsEaten == 10) {
                    timer += 10;
                    dumplingsEaten = 0;
                }
            }
        }

//        }
    }

    private void moveCharacters() {
        for (Character c : characters) {
            int diff = 2;
            if (c.getCurrentMovement() != null) {
                try {
                    switch (c.getCurrentMovement()) {
                        case RIGHT -> {
                            c.getSpawnPoint().x += c.speed;
                            if (PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                                    [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) - 5) / PacmanGame.SQUARE_SIZE] == 'w') {
                                c.getSpawnPoint().x -= ((c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this)) % PacmanGame.SQUARE_SIZE - diff);
                            }
                        }
                        case LEFT -> {
                            c.getSpawnPoint().x -= c.speed;
                            if (PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                                    [(c.getSpawnPoint().x + 5) / PacmanGame.SQUARE_SIZE] == 'w') {
                                c.getSpawnPoint().x += (PacmanGame.SQUARE_SIZE - (c.getSpawnPoint().x % PacmanGame.SQUARE_SIZE) - diff);
                            }
                        }
                        case UP -> {
                            c.getSpawnPoint().y -= c.speed;
                            if (PacmanGame.levelData[(c.getSpawnPoint().y - PacmanGame.SCOREBAR_HEIGHT + 5) / PacmanGame.SQUARE_SIZE]
                                    [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE] == 'w') {
                                c.getSpawnPoint().y += (PacmanGame.SQUARE_SIZE - (c.getSpawnPoint().y % PacmanGame.SQUARE_SIZE) - diff);
                            }
                        }
                        case DOWN -> {
                            c.getSpawnPoint().y += c.speed;

                            if (PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) - PacmanGame.SCOREBAR_HEIGHT - 5) / PacmanGame.SQUARE_SIZE]
                                    [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE] == 'w') {
                                c.getSpawnPoint().y -= ((c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this)) % PacmanGame.SQUARE_SIZE - diff);


                            }
                            }
                        }
                } catch (IndexOutOfBoundsException e) {
                    if (c.getSpawnPoint().getX() <= -c.getCurrentSprite().getImage().getWidth(this))
                        c.getSpawnPoint().x = PacmanGame.levelData[0].length * PacmanGame.SQUARE_SIZE - c.speed;
                    else if (c.getSpawnPoint().getX() >= PacmanGame.WINDOW_WIDTH)
                        c.getSpawnPoint().x = -c.getCurrentSprite().getImage().getWidth(this) + c.speed;
                }
            }


        }

    }



    private void determineEnemiesPoint() {
        for (Character c : characters) {
            if (c != pacman) {
                int diff = 3;
                ArrayList<Character.Movement> possibleDirections = new ArrayList<>();
                switch (c.getCurrentMovement()) {
                    case UP -> {

                        //up middle check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y - PacmanGame.SCOREBAR_HEIGHT +diff - PacmanGame.SQUARE_SIZE/2) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.UP);
                        }

                        //right check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y -
                                PacmanGame.SCOREBAR_HEIGHT + diff) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) + PacmanGame.SQUARE_SIZE/2 - diff) / PacmanGame.SQUARE_SIZE] != 'w'
                                && PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) - diff -
                                PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) +  PacmanGame.SQUARE_SIZE/2 - diff) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.RIGHT);
                        }

                        //left check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y + diff - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + diff - PacmanGame.SQUARE_SIZE/2) / PacmanGame.SQUARE_SIZE] != 'w'
                                && PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) - diff - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + diff - PacmanGame.SQUARE_SIZE/2) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.LEFT);
                        }
                    }
                    case RIGHT -> {

                        //up check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y - PacmanGame.SCOREBAR_HEIGHT + diff - PacmanGame.SQUARE_SIZE/2) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + diff) / PacmanGame.SQUARE_SIZE] != 'w'
                                &&
                                PacmanGame.levelData[(c.getSpawnPoint().y - PacmanGame.SCOREBAR_HEIGHT + diff - PacmanGame.SQUARE_SIZE/2)/ PacmanGame.SQUARE_SIZE]
                                        [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) - diff) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.UP);
                        }

                        //down check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) + PacmanGame.SQUARE_SIZE/2 - PacmanGame.SCOREBAR_HEIGHT - diff) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + diff) / PacmanGame.SQUARE_SIZE] != 'w'
                                &&
                                PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) + PacmanGame.SQUARE_SIZE/2 - PacmanGame.SCOREBAR_HEIGHT - diff) / PacmanGame.SQUARE_SIZE]
                                        [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) - diff) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.DOWN);
                        }

                        //right middle check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) + PacmanGame.SQUARE_SIZE/2 - diff) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.RIGHT);
                        }
                    }
                    case LEFT -> {

                        //up check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y - PacmanGame.SQUARE_SIZE/2 - PacmanGame.SCOREBAR_HEIGHT + diff) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + diff) / PacmanGame.SQUARE_SIZE] != 'w'
                                &&
                                PacmanGame.levelData[(c.getSpawnPoint().y - PacmanGame.SQUARE_SIZE/2 - PacmanGame.SCOREBAR_HEIGHT + diff) / PacmanGame.SQUARE_SIZE]
                                        [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) - diff) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.UP);
                        }

                        //down check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y + PacmanGame.SQUARE_SIZE/2 + c.getCurrentSprite().getImage().getHeight(this) - PacmanGame.SCOREBAR_HEIGHT - diff) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + diff) / PacmanGame.SQUARE_SIZE] != 'w'
                                &&
                                PacmanGame.levelData[(c.getSpawnPoint().y  +PacmanGame.SQUARE_SIZE/2 + c.getCurrentSprite().getImage().getHeight(this) - PacmanGame.SCOREBAR_HEIGHT - diff) / PacmanGame.SQUARE_SIZE]
                                        [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) - diff) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.DOWN);
                        }

                        //left middle check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) / 2 - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + diff - PacmanGame.SQUARE_SIZE/2) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.LEFT);
                        }
                    }

                    case DOWN -> {

                        //down middle check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) + PacmanGame.SQUARE_SIZE/2 - PacmanGame.SCOREBAR_HEIGHT - diff) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) / 2) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.DOWN);
                        }

                        //right check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y -
                                PacmanGame.SCOREBAR_HEIGHT + diff) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) - diff) / PacmanGame.SQUARE_SIZE] != 'w'
                                && PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) - diff -
                                PacmanGame.SCOREBAR_HEIGHT + PacmanGame.SQUARE_SIZE/2) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x + c.getCurrentSprite().getImage().getWidth(this) + PacmanGame.SQUARE_SIZE/2- diff) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.RIGHT);
                        }

                        //left check
                        if (PacmanGame.levelData[(c.getSpawnPoint().y - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x - PacmanGame.SQUARE_SIZE/2+ diff) / PacmanGame.SQUARE_SIZE] != 'w'
                                && PacmanGame.levelData[(c.getSpawnPoint().y + c.getCurrentSprite().getImage().getHeight(this) - PacmanGame.SCOREBAR_HEIGHT) / PacmanGame.SQUARE_SIZE]
                                [(c.getSpawnPoint().x - PacmanGame.SQUARE_SIZE/2 + diff) / PacmanGame.SQUARE_SIZE] != 'w') {
                            possibleDirections.add(Character.Movement.LEFT);
                        }
                    }
                }
                Random rgen = new Random();
                if(possibleDirections.size()>1) {
                    c.setCurrentMovement(possibleDirections.get(rgen.nextInt(possibleDirections.size())));
                }
            }
        }
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


