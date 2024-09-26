import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Drill {
    private  int x;
    private  int y;
    private long lastMoveTime;
    private double fuel;
    private int fallingSpeed = 2; // the speed of the drill when falling.
    private int haul = 0;
    private int money = 0;
    private final Canvas canvas;  // for relocating the drill.
    private final GraphicsContext gc; // for re-drawing the drill.

    private final ScrollPane scrollPane; // scrolls the scrollPane when drill moves.
    private final GameRegion gameRegion;
    private final List<Image> LeftImages = new ArrayList<>(Arrays.asList(
            new Image("assets/drill/drill_01.png"),
            new Image("assets/drill/drill_02.png"),
            new Image("assets/drill/drill_03.png")));

    private final List<Image> RightImages =new ArrayList<>(Arrays.asList(
            new Image("assets/drill/drill_57.png"),
            new Image("assets/drill/drill_58.png"),
            new Image("assets/drill/drill_59.png")));
    private final List<Image> UpImages = new ArrayList<>(Arrays.asList(
            new Image("assets/drill/drill_23.png"),
            new Image("assets/drill/drill_25.png"),
            new Image("assets/drill/drill_27.png")));
    private final List<Image> DownImages = new ArrayList<>(Arrays.asList(
            new Image("assets/drill/drill_23.png"),
            new Image("assets/drill/drill_25.png"),
            new Image("assets/drill/drill_27.png")));
    private boolean isDrillMoving = false;

    /**
     * Constructor for Drill class.
     * draws the drill to the canvas, starts the engine, and activates the falling.
     * @param x x coordinate of the drill.
     * @param y y coordinate of the drill.
     * @param fuel initial fuel of the drill.
     * @param canvas the canvas which drill drawn.
     * @param gc the graphics context of the canvas.
     * @param gameRegion the game region which the drill interacts.
     * @param scrollPane the scrollPane which the drill moves and scrolls.
     */
    public Drill(int x, int y,double fuel, Canvas canvas, GraphicsContext gc,
                 GameRegion gameRegion,ScrollPane scrollPane) {
        this.x = x;
        this.y = y;
        this.fuel = fuel;
        this.canvas = canvas;
        this.gc = gc;
        this.gameRegion = gameRegion;
        this.scrollPane = scrollPane;
        this.drawDrill();
        this.startEngine();
        this.activateFalling();
    }

    /**
     * moves the drill to the given direction.
     * if drill's movement animation is in progress then returns.
     * sets up animation timer for moving the drill timer stops after 10 ticks.
     * in every tick the drill moves 5 pixels to the given direction, drawn with the next image and
     * the scroll pane scrolled to corresponding vertical value.
     * when the animation ends drillInteraction method called to interact with the game region and
     * drillMoving set to false.
     * @param direction Direction enum for specifying the direction of the movement.
     */
    private void move(Direction direction) {
        if (isDrillMoving ) {  // if the drill is in move animation then return;
            return;
        }
        AnimationTimer timer = new AnimationTimer() {
            private int tickCount = 0;
            private long lastTick = 0;
            private final int speed = 50;


            public void handle(long now) {
                isDrillMoving = true;
                lastMoveTime = now;
                if (lastTick == 0) {
                    lastTick = now;
                    return;
                }
                if (tickCount == 10) {
                    drillInteraction(gameRegion);
                    isDrillMoving = false;
                    this.stop();
                }
                if (now - lastTick > 1000000000 / speed) {  // 10^9 nanosecond = 1 second
                    lastTick = now;
                    tickCount++;
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    switch (direction) {
                        case UP:
                            y -= 5;
                            gc.drawImage(UpImages.get(tickCount % UpImages.size()), 0, 0, 70, 71);
                            break;
                        case DOWN:
                            y += 5;
                            gc.drawImage(DownImages.get(tickCount % DownImages.size()), 0, 0, 70, 71);
                            break;
                        case RIGHT:
                            x += 5;
                            gc.drawImage(RightImages.get(tickCount % RightImages.size()), 0, 0, 70, 71);
                            break;
                        case LEFT:
                            x -= 5;
                            gc.drawImage(LeftImages.get(tickCount % LeftImages.size()), 0, 0, 70, 71);
                            break;
                    }
                    canvas.relocate(x,y);
                    scrollPane.setVvalue((y-300)/1000.0>0 ? (y-300)/1000.0 : 0); // scrolls scroll page.
                }
            }
        };
        timer.start();
    }


    /**
     * draws the drill to the graphicContent of Drill.
     */
    private void drawDrill() {
        canvas.relocate(x, y);
        gc.drawImage(LeftImages.get(0), 0, 0, 70, 71);
    }

    /**
     * checks if the drill can move to the given direction.
     * if the drill is at the edge of the playGround or there is obstacle in his way then returns false.
     * @param direction Direction enum for specifying the direction of the movement.
     * @param gameRegion the game region which the drill interacts.
     * @return true if the drill can move to the given direction, false otherwise.
     */
    private boolean legalMoveChecker(Direction direction,GameRegion gameRegion) {
        String[][] playGround = gameRegion.getPlayGround();
        int drillRow = (y / 50)-1; //  +1 since drones canvas location is one block above the drill. -2 since first
        // two rows are empty.
        int drillCol = (x / 50)+1; // +1 since canvas location;

        switch (direction){
            case UP:
                if (drillRow == -1) return false;
                if (drillRow == 0) break;
                if (playGround[drillRow-1][drillCol].equals("obstacle") ||
                        !playGround[drillRow-1][drillCol].equals("void")){ // cant drill upwards.
                    return false;
                }
                break;
            case DOWN:
                if (drillRow == playGround.length-1 || playGround[drillRow+1][drillCol].equals("obstacle")){
                    return false;
                }
                break;
            case RIGHT:
                if (drillCol == playGround[0].length-2) return false;
                if (drillRow == -1) break;
                if (playGround[drillRow][drillCol+1].equals("obstacle")){
                    return false;
                }
                break;
            case LEFT:
                if (drillCol == 1) return false;
                if (drillRow == -1) break;
                if (playGround[drillRow][drillCol-1].equals("obstacle") ) {
                    return false;
                }
                break;
        }
        return true;
    }

    /**
     * controls the movement of the drill with respect to given KeyCode.
     * if game is over, then returns.
     * always decreases the fuel according to the movement direction.
     * calls the legalMoveChecker method to check if the drill can move to the given direction.
     * if drill cant move to the given direction then just changes the direction of the drill.
     * if the drill can move to the given direction calls move method with the direction.
     * @param key KeyCode of the key pressed.
     */
    private void movementController(KeyCode key){
        if (GameOverScreen.isGameOver()) return; // if game is over close the controller. **
        if (isDrillMoving ) {  // if the drill is in move animation then return;
            return;
        }
        switch (key) {
            case UP:
                fuel -= 200; // when moving up fuel decreases more.
                if (legalMoveChecker(Direction.UP,gameRegion)) {
                    move(Direction.UP);
                    fallingSpeed = 2;
                    drillInteraction(gameRegion);
                }
                break;
            case DOWN:
                fuel -= 100;
                if (legalMoveChecker(Direction.DOWN,gameRegion)) {
                    move(Direction.DOWN);
                    fallingSpeed = 2;
                    drillInteraction(gameRegion);
                }
                break;
            case LEFT:
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // make sures to turn the drill.
                gc.drawImage(LeftImages.get(0), 0, 0, 70, 71);
                fuel -= 100;
                if (legalMoveChecker(Direction.LEFT,gameRegion)) {
                    move(Direction.LEFT);
                    fallingSpeed = 2;
                    drillInteraction(gameRegion);
                }
                break;
            case RIGHT:
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // make sures to turn the drill.
                gc.drawImage(RightImages.get(0), 0, 0, 70, 71);
                fuel -= 100;
                if (legalMoveChecker(Direction.RIGHT,gameRegion)) {
                    move(Direction.RIGHT);
                    fallingSpeed = 2;
                    drillInteraction(gameRegion);
                }
                break;
        }
    }

    /**
     * sets the controller for the drill according to the given scene.
     * @param scene the scene which game is played.
     */
    public void setController(Scene scene){
        scene.setOnKeyPressed(e -> movementController(e.getCode()));
    }

    /**
     * interacts with the game region.
     * if the block is lava calls showGameOverScreen method with false and finishes the game.
     * if the block is valuable then adds the weight and worth of the block to the haul and money.
     * if the block is soil or top then breaks the block.
     * @param gameRegion the game region which the drill interacts.
     */
    private void drillInteraction(GameRegion gameRegion) {
        String[][] playGround = gameRegion.getPlayGround();
        int drillRow = (y / 50)-1; //  +1 since drones canvas location is one block above the drill. -2 since first
        // two rows are empty.
        int drillCol = (x / 50)+1; // +1 since canvas location;;
        if (drillRow<0) return;
        String blockName = playGround[drillRow][drillCol];
        if (blockName.equals("lava")) {
            GameOverScreen.showGameOverScene(this,false);
        }
        if (gameRegion.getAllValuables().contains(blockName)) {
            haul += gameRegion.getAllBlocks().get(blockName).getWeight();
            money += gameRegion.getAllBlocks().get(blockName).getWorth();
            gameRegion.breakBlock(drillRow,drillCol);
        }
        if (blockName.equals("soil") || blockName.equals("top")) {
            gameRegion.breakBlock(drillRow,drillCol);
        }

    }

    /**
     * starts the engine of the drill.
     * the engine consumes the fuel consistently.
     * if the fuel run out then calls showGameOverScreen method with true and finishes the game.
     */
    private void startEngine() {
        //starts the timer
         new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (GameOverScreen.isGameOver()) { // if game over stop the drill.
                    this.stop();
                }
                fuel -= 0.1;
                if (fuel <= 0) {
                    GameOverScreen.showGameOverScene(Drill.this,true);
                }
            }
        }.start();
    }

    /**
     * activates the falling of the drill.
     * if game over then stops the falling.
     * if drill isnt moving for 0.5 seconds and the block below the drill is void then moves the drill down.
     * the duration of FallingWait decreases after the first fall.
     */
    private void activateFalling() {
        new AnimationTimer() {
            long lastTick = 0;
            @Override
            public void handle(long now) {
                if (GameOverScreen.isGameOver()) { // if game over stop the drill.
                    this.stop();
                }
                if (lastTick == 0) {
                    lastTick = now;
                    return;
                }
                if (now - lastTick > 1000000000/10 && !isDrillMoving) { // 10^9 nanosecond = 1 second
                    lastTick = now;
                    if (now - lastMoveTime > 1000000000/ fallingSpeed) {
                        int drillRow = (y / 50) - 1;
                        int drillCol = (x / 50) + 1;
                        if (gameRegion.getPlayGround()[drillRow + 1][drillCol].equals("void")) {
                            move(Direction.DOWN);
                            fallingSpeed = 1000; // if drill falls once falls faster until a move command given.
                        }
                    }
                }
            }
        }.start();
    }
    public double getFuel() {
        return fuel;
    }

    public int getHaul() {
        return haul;
    }

    public int getMoney() {
        return money;
    }


}
