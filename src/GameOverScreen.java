import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class is responsible for displaying the game over screen.
 */
public class GameOverScreen {
    private static Stage stage; // the stage which the game over screen will be shown.
    private static Boolean gameOver = false; // if true all the timers will be stopped.

    /**
     * Creates a new scene with a rectangle filled with green color if win is true, red otherwise.
     * adds a text to the scene with the game over message and the money collected.
     * sets the primaryStage's scene to the created scene.
     * @param drill the drill object to get the money.
     * @param win if true the rectangle will be filled with green color, red otherwise.
     */
    public static void showGameOverScene(Drill drill, boolean win) {
        gameOver = true;
        Group root = new Group();
        Scene scene = new Scene(root, 800, 900);
        Rectangle rectangle = new Rectangle(800, 900);
        if (win) {
            rectangle.setFill(Color.GREEN);
        } else {
            rectangle.setFill(Color.RED);
        }
        root.getChildren().add(rectangle);
        Text text = new Text("          Game Over" + "\nCollected Money: " + drill.getMoney());
        text.setFont(new Font("Arial", 50));
        text.setFill(Color.BLACK);
        text.relocate(100, 300);
        root.getChildren().add(text);
        stage.setScene(scene);
    }

    /**
     * sets the primaryStage to the given stage.
     * @param stage the stage which the game over screen will be shown.
     */
    public static void setStage(Stage stage) {
        GameOverScreen.stage = stage;
    }

    public static Boolean isGameOver() {
        return gameOver;
    }
}
