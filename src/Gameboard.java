import javax.swing.*;
import java.awt.*;

public class Gameboard extends JPanel {


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
    }







    public void printMaze(Graphics2D g2d){



        for(int i = 0; i< PacmanGame.levelData.length; i++){
            for(int j = 0; j< PacmanGame.levelData[i].length(); j++){
                char current = PacmanGame.levelData[i].charAt(j);
                switch(current){
                    case 'd' -> {
                        int dotDistance = (int)(PacmanGame.SQUARE_SIZE*0.75/2d);
                        int dotSize = PacmanGame.SQUARE_SIZE-2*dotDistance;
                        //g2d.setColor(dotColor);
                        g2d.fillRect(j* PacmanGame.SQUARE_SIZE + dotDistance, PacmanGame.SCOREBAR_HEIGHT+i* PacmanGame.SQUARE_SIZE + dotDistance, dotSize, dotSize);
                        // SQUARE_SIZE/4
                    }
                    case 'w' -> {
                    g2d.drawRect(j* PacmanGame.SQUARE_SIZE, PacmanGame.SCOREBAR_HEIGHT+i*PacmanGame.SQUARE_SIZE, PacmanGame.SQUARE_SIZE, PacmanGame.SQUARE_SIZE);
                    }
                }
            }
        }
    }

   /* private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(mazeColor);
                g2d.setStroke(new BasicStroke(2));

                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(dotColor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }

                i++;
            }
        }
    }*/
}
