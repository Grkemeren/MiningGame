import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * entry point for JavaFX application.
     * param args command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * this method set ups the root,scene,and stage for the game.
     * instantiates the game region, drill, and information table and adds them to the root.
     * uses scroll pane to allow game region to scroll vertically.
     * sets the scene to the primary stage and shows the stage.
     * @param primaryStage the primary stage of this application.
     */
    @Override
    public void start(Stage primaryStage) {
        GraphicController.StartGame();
    }
}
