import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This class is controlling the stage, scene and elements of root.
 * initializes the game region, drill, and information table and adds them to the root.
 */
public class GraphicController {
    /**
     * sets up the root, scene, and stage for the game.
     * instantiates the game region, drill, and information table and adds them to the root.
     * uses scroll pane to allow game region to scroll vertically.
     * sets the scene to the stage and shows the stage.
     */
    public static void StartGame() {
        Pane root = new Pane();
        ScrollPane scrollPane = new ScrollPane();
        GameRegion gameRegion = new GameRegion();
        Canvas character = new Canvas(70,71);;
        Canvas backGround = new Canvas(gameRegion.getPlayGround()[0].length*50,
                gameRegion.getPlayGround().length*50+100);
        GraphicsContext gcCharacter = character.getGraphicsContext2D();
        GraphicsContext gcBackGround = backGround.getGraphicsContext2D();
        Drill drill = new Drill(32,36,15000,character,gcCharacter,gameRegion,scrollPane);
        Scene scene = new Scene(scrollPane,gameRegion.getPlayGround()[0].length*50, 800);
        Stage stage = new Stage();

        gameRegion.SetGc(gcBackGround); // set graphics context for game region
        gameRegion.drawGameRegion();
        scrollPane.setContent(root);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        drill.setController(scene); // movement controller wits arrow keys.
        GameOverScreen.setStage(stage); // sets stage for GameOver scene.
        root.getChildren().addAll(backGround,character);
        InformationTable.showInformationTable(root,drill,scrollPane); // sets information table.
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Drill Game");
        stage.show();
    }
}
