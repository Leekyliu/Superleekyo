package game.model;

import game.view.Observer;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameEngineImplementation implements GameEngine {

    /**
     * The height of the game engine
     */
    private double height;

    /**
     * The current level
     */
    private Level currentLevel;

    /**
     * Map of all the levels
     */
    private Map<Integer, Level> levels;

    /**
     * Used to create distinct level id's for each level
     */
    private int levelId;

    /**
     * Level id of the current level
     */
    private int currentLevelId;

    /**
     * Json path to the level configuration file
     */
    private String jsonPath;
    private String jsonPathOne;
    private String jsonPathTwo;

    /**
     * Used to keep track of how long it takes the user to complete the game
     */
    private Instant start;



    /**
     * Used to keep track of how long it takes the user to complete the game
     */
    private Duration interval;

    private long newTime;
    private long oldTIme;

    /**
     * The number of lives the hero has
     */
    private int lives;

    private double score;
    private double levelScore;


    private int size;
    private int lastScore;
    private int oldScore;

    private boolean scoreUpdate = false;

    private boolean finish = false;

    private Instant begin;
    private Instant end;

    private boolean keyQ = false;
    private boolean keyW = false;

    private ArrayList<Observer> observers;

    /**
     * Creates the game engine using the specified json configuration file and height
     * @param jsonPath The path to the json configuration file containing the level information
     * @param height The height of the game engine's window
     */
    public GameEngineImplementation(String jsonPath,String jsonPathOne,String jsonPathTwo, double height) {
        this.jsonPath = jsonPath;
        this.jsonPathOne = jsonPathOne;
        this.jsonPathTwo = jsonPathTwo;
        this.height = height;
        this.levels = new HashMap<>();
        this.levelId = 1;
        this.currentLevelId = 1;
        this.lives = 3;

        this.score = 0.0;
        this.levelScore = 0.0;

        oldTIme = newTime = 0;
        oldScore = lastScore = 0;

        observers = new ArrayList<>();

        LevelBuilder levelBuilder = new LevelBuilder(this.jsonPath);
        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        double tmp = levelDirector.getLevel().getTime();

        createLevels(1,0,tmp,this.levels );
        startLevel();
        begin = Instant.now();
        end = begin;
    }

    /**
     * Creates the levels associated with the json file
     */
    public void createLevels(int levelId, double score, double levelScore, Map<Integer, Level> levels) {

        this.levelId = levelId;
        LevelBuilder levelBuilder = new LevelBuilder(this.jsonPath);
        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        levels.put(levelId, levelDirector.getLevel());
        levelId += 1;

        this.score = score;

        this.levelScore = levelScore;



        LevelBuilder levelBuilderOne = new LevelBuilder(this.jsonPathOne);
        LevelDirector levelDirectorOne = new LevelDirector(levelBuilderOne);
        levelDirectorOne.buildLevel();
        levels.put(levelId, levelDirectorOne.getLevel());
        levelId += 1;



        LevelBuilder levelBuilderTwo = new LevelBuilder(this.jsonPathTwo);
        LevelDirector levelDirectorTwo = new LevelDirector(levelBuilderTwo);
        levelDirectorTwo.buildLevel();
        levels.put(levelId, levelDirectorTwo.getLevel());

        this.levelId = levelId;




    }



    @Override
    public void startLevel() {
        this.currentLevel = levels.get(currentLevelId);
        size = this.currentLevel.getEnemyLeft();
        start = Instant.now();


    }

    @Override
    public Level getCurrentLevel() {
        return this.currentLevel;
    }

    @Override
    public boolean jump() {
        return this.currentLevel.jump();
    }

    @Override
    public boolean moveLeft() {
        return this.currentLevel.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return this.currentLevel.moveRight();
    }

    @Override
    public boolean stopMoving() {
        return this.currentLevel.stopMoving();
    }

    @Override
    public void tick() {
        this.currentLevel.tick();




        interval = Duration.between(start, Instant.now());

        newTime = this.getDuration().toSeconds();
        if(newTime - oldTIme >= 1){
            levelScore--;
            if(levelScore < 0){
                levelScore = 0;
            }
            oldTIme = newTime;
        }


        lastScore =  (size - this.currentLevel.getEnemyLeft()) * 100;
        size = this.currentLevel.getEnemyLeft();
        if (oldScore != lastScore){
            scoreUpdate = true;
            setScore();
            oldScore = lastScore;
        }

    }

    @Override
    public void resetCurrentLevel() {
        this.lives--;
        if (this.lives == 0) {
            return;
        }

        LevelBuilder levelBuilder = null;
        if(currentLevelId ==1){
            levelBuilder = new LevelBuilder(this.jsonPath);
        }
        if(currentLevelId ==2){

            levelBuilder = new LevelBuilder(this.jsonPathOne);
        }
        if(currentLevelId ==3){
            levelBuilder = new LevelBuilder(this.jsonPathTwo);
        }

        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        this.levels.put(this.currentLevelId, levelDirector.getLevel());
        this.currentLevel = levels.get(currentLevelId);
        size = this.currentLevel.getEnemyLeft();
    }


    @Override
    public void changeLevel() {

        currentLevelId += 1;
        this.currentLevel = levels.get(currentLevelId);
        levelScore = currentLevel.getTime();
        size = this.currentLevel.getEnemyLeft();




    }


    @Override
    public boolean isFinished()
    {
            if(currentLevel.isFinished()){
                score += levelScore;

                if(score < 0){
                    score = 0;
                }
            }


            return currentLevel.isFinished();
    }





    public void setScore()
    {

        if (scoreUpdate){
            levelScore += lastScore;
            scoreUpdate = false;
        }





    }
    @Override
    public Duration getDuration() {
        return interval;
    }

    @Override
    public Duration getFinalDuration() {
        return interval.minus(Duration.between(start,end));
    }

    @Override
    public boolean gameOver() {
        return this.lives == 0;
    }

    @Override
    public int getLives() {
        return this.lives;
    }

    @Override
    public double getScore() {
        return this.score;
    }

    @Override
    public double getLevelScore() {
        return this.levelScore;
    }

    @Override
    public int getCurrentLevelId() {
        return this.currentLevelId;
    }




    @Override
    public void notifyObserver() {
        int id = this.getCurrentLevelId();
        Map<Integer, Level> oldLevel = new HashMap<>();




        createLevels(1,this.score,this.levelScore,oldLevel);

        Level level = new LevelImplementation();



        List<Entity> lst =  new ArrayList<>();
        List<Entity> oldlist = currentLevel.getEntities();
        List<MovableEntity> movableEntities = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();

        int H = currentLevel.getHeroHealth();

        for (Entity e: oldlist){
            Entity new_e = e.entityCopy();
            lst.add(new_e);
            if(new_e instanceof Hero){
                ((Hero) new_e).setHealth(H);
                level.setHero((Controllable) new_e);
            }
            if(new_e instanceof MovableEntity){
                if(!(new_e instanceof Hero) && !(new_e instanceof Enemy))
                {
                    movableEntities.add((MovableEntity) new_e);
                }

            }
            if(new_e instanceof Enemy){
                enemies.add((Enemy) new_e);
            }


        }
        level.setEntities(lst);
        level.setMovableEntities(movableEntities);
        level.setEnemies(enemies);
        level.setFloorHeight(currentLevel.getFloorHeight());
        level.setLevelWidth(currentLevel.getWidth());
        level.setLevelHeight(currentLevel.getHeight());
        level.setSlideEffect(currentLevel.getSlideEffect());
        level.setSlideVel(currentLevel.getSlideVel());


        level.setFinished(false);
        level.setTime(currentLevel.getTime());






            oldLevel.put(id,level);








        for(Observer observer: observers){
            observer.save(oldLevel,this.getCurrentLevelId(),this.score,this.levelScore, this.lives);
        }
            begin = Instant.now();
    }

    @Override
    public void register(Observer newObserver) {
        observers.add(newObserver);

    }



    @Override

    public  void changeState(){

        for(Observer observer: observers){
            observer.update();
        }

    }

    @Override

    public void setState( Map<Integer, Level> oldLevel,int id,double score,double levelScore,int lives){


        Map<Integer, Level> load = new HashMap<>();

        createLevels(1,score,levelScore,load);




        Level level = new LevelImplementation();



        List<Entity> lst =  new ArrayList<>();
        List<Entity> oldlist =  oldLevel.get(id).getEntities();
        List<MovableEntity> movableEntities = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        int H = oldLevel.get(id).getHeroHealth();


        for (Entity e: oldlist){
            Entity new_e = e.entityCopy();
            lst.add(new_e);
            if(new_e instanceof Hero){
                ((Hero) new_e).setHealth(H);
                this.lives = lives;

                level.setHero((Controllable) new_e);


            }
            if(new_e instanceof MovableEntity){
                if(!(new_e instanceof Hero) && !(new_e instanceof Enemy))
                {
                    movableEntities.add((MovableEntity) new_e);
                }
            }
            if(new_e instanceof Enemy){
                enemies.add((Enemy) new_e);
            }


        }
        level.setEntities(lst);

        level.setMovableEntities(movableEntities);
        level.setEnemies(enemies);
        level.setFloorHeight(oldLevel.get(id).getFloorHeight());
        level.setLevelWidth(oldLevel.get(id).getWidth());
        level.setLevelHeight(oldLevel.get(id).getHeight());

       level.setSlideEffect(oldLevel.get(id).getSlideEffect());

        level.setSlideVel(oldLevel.get(id).getSlideVel());

        level.setSlideVel(oldLevel.get(id).getSlideVel());
        level.setFinished(false);
        level.setTime(oldLevel.get(id).getTime());






        load.put(id,level);

        levels.putAll(load);
        this.currentLevelId = id;

        this.stopMoving();

        this.currentLevel = levels.get(id);
        size = currentLevel.getEnemyLeft();

        end = Instant.now();




    }

    @Override
    public void saveState() {

        notifyObserver();
    }

    @Override
    public boolean isOver() {

        if(levels.get(levelId).isFinished()){
            finish = true;
        }
        return finish;
    }

    @Override
    public boolean isKeyQ(){
        return this.keyQ;
    }

    @Override
    public boolean isKeyW(){
        return this.keyW;
    }

    @Override
    public void setKeyQ(boolean b){
         this.keyQ = b;
    }

    @Override
    public void setKeyW(boolean b){
        this.keyW = b;
    }
}

