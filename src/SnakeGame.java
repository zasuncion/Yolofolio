import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    // Private so only the SnakeGame can access
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    // Initialize variables
    int gridWidth;
    int gridHeight;
    // each tile will be 25 pixels in width and heigth
    int tileSize = 25;

    // Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // Food
    Tile food;
    Random random;

    // Game Logic

    // gameLoop for redrawing the Snake when its location changes
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    // SnakeGame Constructor
    SnakeGame(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        setPreferredSize(new Dimension(this.gridWidth, this.gridHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        // place the food in a random location
        random = new Random();
        placeFood();

        // To make the Snake "move" in a certain direction
        velocityX = 0;
        velocityY = 0;

        // Delay in milliseconds
        gameLoop = new Timer(100, this); 
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Calls superclass's paintComponent method
        draw(g); // Calls custom draw method 
    }

    // Game Panel
    public void draw(Graphics g) {
        // // Grid with a semi-transparent white
        // g.setColor(new Color(255, 255, 255, 50)); 
        // for (int i = 0; i < gridWidth/tileSize; i++) {
        //     // (x1, y1, x2, y2)
            
        //     // vertical lines
        //     g.drawLine(i * tileSize, 0, i * tileSize, gridHeight);
        //     // horizontal lines
        //     g.drawLine(0, i * tileSize, gridWidth, i * tileSize);
        // }

        // Food
        g.setColor(Color.red);
        // g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);


        // Snake Head
        g.setColor(Color.green);
        // multiply by tileSize for proper pixel scaling
        // g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);


        // Snake Body
        for(int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }
        
        // Score
        g.setFont(new Font("Arial", Font.PLAIN, 16)) ;
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16 , tileSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    // Generate a random location for the food from pos 0-24
    public void placeFood() {
        food.x = random.nextInt(gridWidth/tileSize); // 600/25 = 24
        food.y = random.nextInt(gridHeight/tileSize); // 600/25 = 24
    }

    // To check if the snake has "eaten" food or has collided with its own body
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // Eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile (food.x,food.y));
            placeFood();
        }

        // Each tile needs to catch up to the one before it
        // before the Snake Head can move (iterate backwards)

        // Snake Body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Game Over Conditions

        // Head collides with Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // colliding with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        // Head collides with grid border
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > gridWidth ||
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > gridHeight) {
            gameOver = true;
        }
    }

    // Will be used to call move and draw over and over again
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    // Parsing the arrow keys, translate to velocity movement
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            if (velocityY == 0) {
                velocityX = 0;
                velocityY = -1;
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            if (velocityY == 0) {
                velocityX = 0;
                velocityY = 1;
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            if (velocityX == 0) {
                velocityX = -1;
                velocityY = 0;
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            if (velocityX == 0) {
                velocityX = 1;
                velocityY = 0;
            }
        }
    }

    // Not needed
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
