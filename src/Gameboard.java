import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameboard extends JPanel implements KeyListener {

    int pacmanSpeed=4;

    Character pacman = new Character(PacmanGame.PACMAN_LIVES,pacmanSpeed, new ImageIcon("src/images/cossack1.png"),new Point(PacmanGame.WINDOW_WIDTH/2,PacmanGame.SCOREBAR_HEIGHT+22*PacmanGame.SQUARE_SIZE));

    public Gameboard(int width, int height){
this.setSize(width, height);
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

    }







    public void printMaze(Graphics2D g2d){
        g2d.fillRect(120,0,5,720);
        g2d.fillRect(0,120,560,5);

        for(int i = 0; i< PacmanGame.levelData.length; i++){
            for(int j = 0; j< PacmanGame.levelData[i].length; j++){
                char current = PacmanGame.levelData[i][j];
                switch(current){
                    case 'd' -> {
                        int dotDistance = (int)(PacmanGame.SQUARE_SIZE*0.75/2d);
                        int dotSize = PacmanGame.SQUARE_SIZE-2*dotDistance;
                        //g2d.setColor(dotColor);
                        g2d.fillRect(j* PacmanGame.SQUARE_SIZE + dotDistance, PacmanGame.SCOREBAR_HEIGHT+i* PacmanGame.SQUARE_SIZE + dotDistance, dotSize, dotSize);
                        // SQUARE_SIZE/4
                    }
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

                    g2d.drawRect(x_pos, y_pos, width, height);
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
        if(e.getKeyCode()==KeyEvent.VK_UP){

        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN){

        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){

        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
