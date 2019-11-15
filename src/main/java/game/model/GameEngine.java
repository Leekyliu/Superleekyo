package game.model;

import game.view.Observer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface GameEngine {

    /**
     * Returns the current level
     * @return The current level
     */
    Level getCurrentLevel();

    /**
     * Starts the current level
     */
    void startLevel();

    // Hero inputs - boolean for success (possibly for sound feedback)
    boolean jump();
    boolean moveLeft();
    boolean moveRight();
    boolean stopMoving();

    void tick();

    /**
     * Resets the current level
     */
    void resetCurrentLevel();

    /**
     * Determines whether or not the current level is finished
     * @return True if the current level is finished, else false
     */
    boolean isFinished();

    /**
     * Returns the duration of the current level
     * @return The duration of the current level
     */
    Duration getDuration();

    /**
     * Determines whether or not the game is over i.e. the hero has no more lives left
     * @return True if the game is over, else false
     */
    boolean gameOver();

    /**
     * Returns the number of lives the hero has left
     * @return The number of lives the hero has left
     */
    int getLives();

    void changeLevel();

    double getScore();

    double getLevelScore();

    int getCurrentLevelId();

    void notifyObserver();

    void register(Observer newObserver);

    void changeState();

    void saveState();

    void setState(Map<Integer, Level> oldLevel, int id, double score, double levelScore, int lives);


    boolean isOver();



    Duration getFinalDuration();

    boolean isKeyQ();
    boolean isKeyW();

    void setKeyQ (boolean b);
    void setKeyW (boolean b);


}
