import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


import java.util.*;

/**
 * This class creates stores the blocks with their Images, generates the playGround with randomized blocks,
 * and draws the game region to graphics context.
 */
public class GameRegion {
    private GraphicsContext gc; // the graphics context to draw the game region.
    private final HashMap<String, Block> allBlocks = new HashMap<>(); // blockName -> Block

    private final String[][] playGround = new String[28][16]; // stores the playGround as String of block names.
    private final List<String> allValuables = new ArrayList<>(Arrays.asList("amazonite", "bronzium", "diamond"
            , "einsteinium", "emerald", "goldium", "ironium", "platinium", "ruby", "silverium"));
    /**
     * Constructor
     * loads the assets, generates the playGround, and draws the game region.
     */
    public GameRegion() {
        loadAssets();
        generatePlayGround();
    }

    /**
     * creates the blocks with their images and adds them to the allBlocks HashMap.
     */
    private void loadAssets() {
        allBlocks.put("lava", new Block("lava", 0, 0,
                new ArrayList<Image>(Arrays.asList(
                new Image("assets/underground/lava_01.png"),
                new Image("assets/underground/lava_02.png"),
                new Image("assets/underground/lava_03.png")
        ))));
        allBlocks.put("soil", new Block("soil", 0, 0,
                new ArrayList<Image>(Arrays.asList(
                new Image("assets/underground/soil_01.png"),
                new Image("assets/underground/soil_02.png"),
                new Image("assets/underground/soil_03.png")
        ))));
        allBlocks.put("top", new Block("top", 0, 0,
                new ArrayList<Image>(Arrays.asList(
                new Image("assets/underground/top_01.png"),
                new Image("assets/underground/top_02.png")
        ))));
        allBlocks.put("obstacle", new Block("obstacle", 0, 0,
                new ArrayList<Image>(Arrays.asList(
                new Image("assets/underground/obstacle_01.png"),
                new Image("assets/underground/obstacle_02.png"),
                new Image("assets/underground/obstacle_03.png")
        ))));
        allBlocks.put("amazonite", new Block("amazonite", 500000, 120,
                new ArrayList<Image>(Collections.singletonList(
                        new Image("assets/underground/valuable_amazonite.png")
                ))));
        allBlocks.put("bronzium", new Block("bronzium", 60, 10,
                new ArrayList<Image>(Collections.singletonList(
                new Image("assets/underground/valuable_bronzium.png")
        ))));
        allBlocks.put("diamond", new Block("diamond", 100000, 100,
                new ArrayList<Image>(Collections.singletonList(
                new Image("assets/underground/valuable_diamond.png")
        ))));
        allBlocks.put("einsteinium", new Block("einsteinium", 2000, 40,
                new ArrayList<Image>(Collections.singletonList(
                new Image("assets/underground/valuable_einsteinium.png")
        ))));
        allBlocks.put("emerald", new Block("emerald", 500000, 120,
                new ArrayList<Image>(Collections.singletonList(
                new Image("assets/underground/valuable_emerald.png")
        ))));
        allBlocks.put("goldium", new Block("goldium", 250, 20,
                new ArrayList<Image>(Collections.singletonList(
                new Image("assets/underground/valuable_goldium.png")
        ))));
        allBlocks.put("ironium", new Block("ironium", 30, 10,
                new ArrayList<Image>(Collections.singletonList(
                new Image("assets/underground/valuable_ironium.png")
        ))));
        allBlocks.put("platinium", new Block("platinium", 750, 30,
                new ArrayList<Image>(Collections.singletonList(
                new Image("assets/underground/valuable_platinum.png")
        ))));
        allBlocks.put("ruby", new Block("ruby", 20000, 80,
                new ArrayList<Image>(Collections.singletonList(
                new Image("assets/underground/valuable_ruby.png")
        ))));
        allBlocks.put("silverium", new Block("silverium", 100, 10,
                new ArrayList<Image>(Collections.singletonList(
                new Image("assets/underground/valuable_silverium.png")
        ))));
    }

    /**
     * fills the playGround array with suitable blocks.
     * each block count is randomized between two integers, and the settlement of the blocks is randomized.
      */
    public void generatePlayGround() {
        int numberOfRows = playGround.length; // 28
        int numberOfCol = playGround[0].length; // 16
        for (int i = 0; i < numberOfCol; i++) {
            playGround[0][i] = "top";
        }
        for (int i = 1; i < numberOfRows; i++) {
            playGround[i][0] = "obstacle";
            playGround[i][playGround[0].length - 1] = "obstacle";
        }
        for (int i = 1; i < numberOfCol - 1; i++) {
            playGround[playGround.length - 1][i] = "obstacle";
        }

        Random random = new Random();  // bounds number of blocks in the playGround.
        int valuableCount = random.nextInt(5) + 30;
        int lavaCount = random.nextInt(5) + 20;
        int obstacleCount = random.nextInt(5) + 30;
        int soilCount = (numberOfRows - 2) * (numberOfCol - 2) - valuableCount - lavaCount - obstacleCount;
        List<String> shuffleList = new ArrayList<>();
        for (int i = 0; i < valuableCount; i++) {
            shuffleList.add(allValuables.get(random.nextInt(allValuables.size())));
        }
        for (int i = 0; i < lavaCount; i++) {
            shuffleList.add("lava");
        }
        for (int i = 0; i < obstacleCount; i++) {
            shuffleList.add("obstacle");
        }
        for (int i = 0; i < soilCount; i++) {
            shuffleList.add("soil");
        }
        Collections.shuffle(shuffleList);
        int rowindex = 1;
        int colindex = 1;
        for (String blockName : shuffleList) {  // filling the playGround with the shuffled list.
            playGround[rowindex][colindex] = blockName;
            if (colindex == numberOfCol - 2) {
                colindex = 1;
                rowindex++;
            } else {
                colindex++;
            }
        }
    }

    /**
     * draws the game region to the graphics context.
     * the images are selected randomly from the list of images of the block.
     * the first two rows is colored with light sky blue and the rest is drawn with the images of the blocks.
     */
    public void drawGameRegion() {
        Random random = new Random();
        gc.setFill(Color.LIGHTSKYBLUE);
        gc.fillRect(0, 0, 800, 900);
        for (int i = 0; i < playGround[0].length; i++) {
            for (int j = 0; j < playGround.length; j++) {
                Block block = allBlocks.get(playGround[j][i]);
                Image image = block.getImages().get(random.nextInt(block.getImages().size()));
                // selects a random image from the list of images
                gc.drawImage(image, i * 50, j * 50 + 100, 50, 50);
            }
        }
    }

    /**
     * Breaks the block in the given row and column.
     * Sets the gameRegion array at that position to "void".
     * Fills the rectangle with dark orange on the graphics context.
     * @param row the row of the block to break.
     * @param col the column of the block to break.
     */
    public void breakBlock(int row, int col) {
        playGround[row][col] = "void";
        gc.setFill(Color.DARKORANGE);
        gc.fillRect(col * 50, row * 50 + 100, 50, 50);
    }

    public List<String> getAllValuables() {
        return allValuables;
    }
    public HashMap<String, Block> getAllBlocks() {
        return allBlocks;
    }
    public String[][] getPlayGround() {
        return playGround;
    }
    public void SetGc(GraphicsContext gc) {
        this.gc = gc;
    }
}


