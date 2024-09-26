import javafx.scene.image.Image;

import java.util.List;

public class Block {
    private final String blockName; //it isn't used in the game but has been added for future improvements.
    private final int worth;
    private final int weight;
    private final List<Image> images;

    /**
     * Constructor of the Block class.
     * @param blockName the name of the block. it isn't used in the game but has been added for future improvements.
     * @param worth the worth of the block.
     * @param weight the weight of the block.
     * @param images the list of possible images of the block.
     */
    public Block(String blockName, int worth, int weight, List<Image> images) {
        this.blockName = blockName;
        this.worth = worth;
        this.weight = weight;
        this.images = images;
    }
    public int getWorth() {
        return worth;
    }
    public int getWeight() {
        return weight;
    }

    /**
     * @return the list of images of the block.
     */
    public List<Image> getImages() {
        return images;
    }
}
