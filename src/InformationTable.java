import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class is responsible for displaying the information table.
 */
public class InformationTable {
    private static final Label informationTable = new Label();

    /**
     * sets the font, text color of informationTable and adds it to the root.
     * creates a new AnimationTimer to update the information table.
     * @param root the root of the scene which the label added.
     * @param drill the drill object to get the fuel, haul, and money.
     * @param scrollPane to get the vertical value of the scrollPane to relocate the informationTable.
     */
    public static void showInformationTable(Pane root, Drill drill, ScrollPane scrollPane) {
        informationTable.setFont(new Font("Arial", 20));
        informationTable.setTextFill(Color.ORANGERED);
        root.getChildren().add(informationTable);
        new AnimationTimer(){
            long lastTick = 0;
            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    return;
                }
                informationTable.setText("Fuel: " + (int)drill.getFuel() + "\nHaul: " + drill.getHaul() +
                        "\nMoney: " + drill.getMoney());
                informationTable.relocate(10,scrollPane.getVvalue()*700+10); // 700 since 1500-800

            }
        }.start();
    }
}
