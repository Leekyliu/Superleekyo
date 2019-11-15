package game.model;

/**
 * Represents the ground in the level
 */
public class Ground extends EntityAbstraction {

    public Ground(String imagePath, double xPos, double yPos, double width, double height) {
        super(imagePath, xPos, yPos, width, height, Layer.FOREGROUND);
    }

    @Override
    public Entity entityCopy(){
        Ground ground = new Ground(this.imagePath, this.xPos, this.yPos, this.width, this.height);
        return ground;
    }
}
