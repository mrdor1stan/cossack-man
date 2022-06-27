import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameboard extends JPanel implements KeyListener, ActionListener {

    static int timer = 100;

    JLabel timeShowcase;

    int countdown;
    private final Point pacmanDefaultSpawn = new Point(PacmanGame.GAME_WIDTH/2,PacmanGame.SCOREBAR_HEIGHT+23*PacmanGame.SQUARE_SIZE);

    Timer tm = new Timer(5, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            countdown++;
            if(countdown==10) {
                countdown=0;
                timer--;
                timeShowcase.setText("TIME: "+timer);
            }
            if(timer==0) {
                tm.stop();
                pacman.livesLeft--;
                if(pacman.livesLeft!=0) {
                    pacman.setSpawnPoint(pacmanDefaultSpawn);
                    timer=100;
                    tm.start();
                }
                else {
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
    int pacmanSpeed=4;

    boolean isColliding;
    int dumplingsEaten = 0;

    Character pacman = new Character(PacmanGame.PACMAN_LIVES, pacmanSpeed, new ImageIcon("src/images/cossack1.png"), pacmanDefaultSpawn);
    //X selects String (vertical movement), Y selects char (horizontal movement)
    double map_y = (pacman.getSpawnPoint().y-3*PacmanGame.SQUARE_SIZE)/28d;
    double map_x = pacman.getSpawnPoint().x/28d;

    Rectangle wall;
    Character chmonya = new Character(1, 3, new ImageIcon("src/images/chmonya1.png"), new Point(PacmanGame.WINDOW_WIDTH/2, (PacmanGame.WINDOW_HEIGHT/2)-PacmanGame.SQUARE_SIZE));

    Character brute = new Character(2, 2, new ImageIcon("src/images/brute1.png"), new Point(PacmanGame.WINDOW_WIDTH/2-2*PacmanGame.SQUARE_SIZE, (PacmanGame.WINDOW_HEIGHT/2)-PacmanGame.SQUARE_SIZE));

    ImageIcon dumpIcon = new ImageIcon("src/images/dumpling.png");
    Image dumpImage = dumpIcon.getImage();
    int dotDistance = (int)(PacmanGame.SQUARE_SIZE*0.75/2d);

    int dotSize = PacmanGame.SQUARE_SIZE-2*dotDistance;
    Image dumpling = dumpImage.getScaledInstance((int) (dotSize*1.2), (int) (dotSize*1.2), Image.SCALE_SMOOTH);

    public Gameboard(int width, int height){
        this.setSize(width, height);
        timeShowcase = new JLabel("TIME: "+timer);
        Dimension size = timeShowcase.getPreferredSize();
        timeShowcase.setBounds(100,100, size.width, size.height);
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

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        printMaze(g2d);
        g2d.drawImage(pacman.getCurrentSprite().getImage(), (int) pacman.getSpawnPoint().getX(), (int) pacman.getSpawnPoint().getY(), this);
        g2d.drawImage(chmonya.getCurrentSprite().getImage(), (int) chmonya.getSpawnPoint().getX(), (int) chmonya.getSpawnPoint().getY(), this);
        g2d.drawImage(brute.getCurrentSprite().getImage(), (int) brute.getSpawnPoint().getX(), (int) brute.getSpawnPoint().getY(), this);
    }

    public void printMaze(Graphics2D g2d){
//        g2d.fillRect(120,0,5,720);
//        g2d.fillRect(0,120,560,5);
        for(int i = 0; i< PacmanGame.levelData.length; i++){
            for(int j = 0; j< PacmanGame.levelData[i].length; j++){
                char current = PacmanGame.levelData[i][j];
//                if(current == PacmanGame.levelData[0][0])
//                    System.out.println(current);
                switch(current){
                    case 'd' -> //g2d.setColor(dotColor);
                        //g2d.fillRect(j* PacmanGame.SQUARE_SIZE + dotDistance, PacmanGame.SCOREBAR_HEIGHT+i* PacmanGame.SQUARE_SIZE + dotDistance, dotSize, dotSize);
                        // SQUARE_SIZE/4
                            g2d.drawImage(dumpling, j* PacmanGame.SQUARE_SIZE + dotDistance, PacmanGame.SCOREBAR_HEIGHT+i* PacmanGame.SQUARE_SIZE + dotDistance, this);

                    case 'w' -> {
                        int height  =  PacmanGame.SQUARE_SIZE;
                        int width = PacmanGame.SQUARE_SIZE;
                        int x_pos = j* PacmanGame.SQUARE_SIZE;
                        int y_pos = PacmanGame.SCOREBAR_HEIGHT+i*PacmanGame.SQUARE_SIZE;

                        //lower border
                       if(i+1<PacmanGame.levelData.length)
                           if(PacmanGame.levelData[i+1][j]!='w'){
                           height-=5;
                       }
                        //top border
                        if(i-1>0)
                            if(PacmanGame.levelData[i-1][j]!='w'){
                                y_pos+=5;
                                height-=5;
                            }
                        //left border
                        if(j-1>0)
                            if(PacmanGame.levelData[i][j-1]!='w'){
                                x_pos+=5;
                                width-=5;
                            }
                        //right border
                        if(j+1<PacmanGame.levelData[i].length)
                            if(PacmanGame.levelData[i][j+1]!='w'){
                                width-=5;
                            }
//                    g2d.drawRect(x_pos, y_pos, width, height);
                        wall = new Rectangle(x_pos, y_pos, width, height);
                        g2d.draw(wall);
                    }
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE) {
            tm.start();
        }
        if(e.getKeyCode()==KeyEvent.VK_P){
            if(tm.isRunning())
                tm.stop();
            else
                tm.start();
        }
        if(e.getKeyCode()==KeyEvent.VK_UP){
            pacman.setCurrentMovement(Character.Movement.UP);
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            pacman.setCurrentMovement(Character.Movement.DOWN);
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            pacman.setCurrentMovement(Character.Movement.LEFT);
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            pacman.setCurrentMovement(Character.Movement.RIGHT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

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


