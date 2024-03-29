package game.model;

import game.view.GameWindow;

/**
 * Represents a cloud entity in the level
 */


public class Cloud extends MovableEntityAbstraction {

    private double tmp;
    /**
     * Creates a new Cloud with the given image path,
     * x position, y position, width, height and velocity
     */
    public Cloud(String imagePath, double xPos, double yPos, double width, double height, double xVel) {
        super(imagePath, xPos, yPos, width, height, xVel / GameWindow.ticksPerSecond(), Layer.BACKGROUND);
        tmp = xVel;
    }

    @Override
    public Entity entityCopy(){
        Cloud cloud = new Cloud(this.imagePath, this.xPos, this.yPos, this.width, this.height, tmp);
        return cloud;
    }
}
