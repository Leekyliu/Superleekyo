package game.model;

/**
 * Represents an ice platform in the game
 */
public class IcePlatform extends EntityAbstraction {

    /**
     * Creates an ice platform with the specified image path, xPos, yPos,
     * width and height
     */
    public IcePlatform(String imagePath, double xPos, double yPos, double width, double height) {
        super(imagePath, xPos, yPos, width, height, Layer.FOREGROUND);
    }

    @Override
    public boolean isIcy() {
        return true;
    }

    @Override
    public Entity entityCopy(){
        IcePlatform icePlatform = new IcePlatform(this.imagePath, this.xPos, this.yPos, this.width, this.height);
        return icePlatform;
    }

}
