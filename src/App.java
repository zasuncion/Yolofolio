import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        // Border lengths (in pixels)
        int gridWidth = 600;
        int gridHeight = gridWidth;

        // Setting the game frame
        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(gridWidth, gridHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Instance of SnakeGame
        SnakeGame snakeGame = new SnakeGame(gridWidth, gridHeight);
        frame.add(snakeGame);
        // To place the Jpanel inside the 600x600 game frame
        frame.pack();
        // SnakeGame "listens" to key presses
        snakeGame.requestFocus();
    }
}
