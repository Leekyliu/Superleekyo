package game.view;

import game.model.Block;
import game.model.Entity;
import game.model.GameEngine;
import game.model.Level;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Runner implements Observer{

    private GameEngine model;

    private int oldId;

    Map<Integer, Level> oldLevel;






    private Pane pane;
    private List<EntityView> entityViews;
    private BackgroundDrawer backgroundDrawer;
    private ImageView[] health = new ImageView[3];
    private Text lives;
    private Text score;
    private Text levelScore;
    private Text levelId;
    private Timeline timeline;
    private double xViewportOffset = 0.0;
    private double width;
    private double height;

    private Button saveBtn;
    private Button loadBtn;
    private double tmpLevelScore;
    private double tmpScore;
    private int oldLives;
    private Instant oldStart;


    Runner(GameEngine model, Pane pane, double width, double height) {
        this.model = model;
        this.pane = pane;
        this.width = width;
        this.height = height;

        this.entityViews = new ArrayList<>();
        this.health[0] = new ImageView(new Image("heart.png"));
        this.health[1] = new ImageView(new Image("heart.png"));
        this.health[2] = new ImageView(new Image("heart.png"));
        for (int i = health.length - 1; i >= 0; i--) {
            this.health[i].setFitHeight(30);
            this.health[i].setFitWidth(30);
            this.health[i].setY(10);
            this.health[i].setX(width - 40 - i * 40);
        }
        this.lives = new Text(10, 20, "lives remaining: " + model.getLives());
        this.lives.setFont(Font.font ("Chalkboard SE", FontPosture.ITALIC, 20));
        this.lives.setFill(Paint.valueOf("BLACK"));
        this.lives.setX(width -  this.lives.getLayoutBounds().getWidth() - 150);
        this.lives.setY(30);
        this.pane.getChildren().add(lives);




        this.levelId = new Text(10, 20, "Level: " + model.getCurrentLevelId());
        this.levelId.setFont(Font.font ("Chalkboard SE", FontPosture.ITALIC, 20));
        this.levelId.setFill(Paint.valueOf("BLACK"));
        this.levelId.setX(width -  this.levelId.getLayoutBounds().getWidth() - 800);
        this.levelId.setY(30);
        this.pane.getChildren().add(levelId);


        this.score = new Text(10, 20, "Total Score: " + model.getScore());
        this.score.setFont(Font.font ("Chalkboard SE", FontPosture.ITALIC, 20));
        this.score.setFill(Paint.valueOf("BLACK"));
        this.score.setX(width -  this.score.getLayoutBounds().getWidth() - 600);
        this.score.setY(30);
        this.pane.getChildren().add(score);


        this.levelScore = new Text(10, 20, "Level Score: " + model.getLevelScore());
        this.levelScore.setFont(Font.font ("Chalkboard SE", FontPosture.ITALIC, 20));
        this.levelScore.setFill(Paint.valueOf("BLACK"));
        this.levelScore.setX(width -  this.score.getLayoutBounds().getWidth() - 400);
        this.levelScore.setY(30);
        this.pane.getChildren().add(levelScore);




        HBox buttons = new HBox();
        buttons.setSpacing(100);
        buttons.setLayoutY(500);
        buttons.setLayoutX(400);



        this.saveBtn = new Button("Save");
        saveBtn.setFocusTraversable(false);
        saveBtn.setOnAction(event -> this.handleSaveClick());


        this.loadBtn = new Button("Load");

        loadBtn.setFocusTraversable(false);
        loadBtn.setDisable(true);
        loadBtn.setOnAction(event -> this.handleLoadClick());





        buttons.getChildren().add(saveBtn);

        buttons.getChildren().add(loadBtn);


        pane.getChildren().add(buttons);

        this.backgroundDrawer = new ParallaxBackground();
        this.backgroundDrawer.draw(model, pane);
        addHealth();

        this.model.register(this);
    }

    void run() {
        timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }






    private void handleSaveClick() {
        loadBtn.setDisable(false);
        model.saveState();


    }


   private void handleLoadClick(){

       this.model.changeState();
         drawH();

   }

    public void drawH() {
        pane.getChildren().remove(health[0]);
        pane.getChildren().remove(health[1]);
        pane.getChildren().remove(health[2]);
        addHealth();
        int h = model.getCurrentLevel().getHeroHealth();

        if (h == 2) {
            pane.getChildren().remove(health[2]);
        } else if (h == 1) {
            pane.getChildren().remove(health[2]);
            pane.getChildren().remove(health[1]);
        }
    }

    private void draw() {
        model.tick();
        if(model.isKeyQ()){
            loadBtn.setDisable(false);
            model.setKeyQ(false);
        }
        if(model.isKeyW()){
           drawH();
           model.setKeyW(false);
        }
        if(model.isOver()){
            saveBtn.setDisable(true);
            loadBtn.setDisable(true);
        }
        if (model.isFinished()) {

            if(!model.isOver()){
                model.changeLevel();
            }

            else{
                drawScreen("Congratulations!\nYou Won in " + model.getFinalDuration().toSeconds() + "s got "+ model.getScore() +" points"+ " \n You had "
                        + model.getLives() + " lives remaining!");
            }

            return;

        } else if(model.gameOver()) {
            drawScreen("You lose!");
            return;
        }

        drawLives();
        drawScore();
        drawId();
        drawLevelScore();

        if (model.getCurrentLevel().getHeroHealth() == 2) {
            pane.getChildren().remove(health[2]);
        } else if (model.getCurrentLevel().getHeroHealth() == 1) {
            pane.getChildren().remove(health[1]);
        } else if (model.getCurrentLevel().getHeroHealth() == 0) {
            pane.getChildren().remove(health[0]);
            addHealth();
            model.resetCurrentLevel();
        }

        List<Entity> entities = model.getCurrentLevel().getEntities();

        for (EntityView entityView: entityViews) {
            entityView.markForDelete();
        }

        double heroXPos = model.getCurrentLevel().getHeroX();
        heroXPos -= xViewportOffset;

        if (heroXPos < GameWindow.getViewportMargin()) {
            if (xViewportOffset >= 0) { // Don't go further left than the start of the level
                xViewportOffset -= GameWindow.getViewportMargin() - heroXPos;
                if (xViewportOffset < 0) {
                    xViewportOffset = 0;
                }
            }
        } else if (heroXPos > width -  GameWindow.getViewportMargin()) {
            xViewportOffset += heroXPos - (width - GameWindow.getViewportMargin());
        }

        backgroundDrawer.update(xViewportOffset);

        for (Entity entity: entities) {
            boolean notFound = true;
            for (EntityView view: entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView: entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }
        entityViews.removeIf(EntityView::isMarkedForDelete);
    }

    /**
     * Draws the finish screen with the specified message in the center
     * @param message The message to be displayed in the center
     */
    private void drawScreen(String message) {
        for (EntityView entityView: entityViews) {
            pane.getChildren().remove(entityView.getNode());
        }
        for (ImageView life: this.health) {
            pane.getChildren().remove(life);
        }
        pane.getChildren().remove(lives);
        Text t = new Text(10, 20, message);
        t.setFont(Font.font ("Chalkboard SE", FontPosture.ITALIC, 60));
        t.setFill(Paint.valueOf("BLACK"));
        t.setLayoutX((width - t.getLayoutBounds().getWidth()) / 2.0);
        t.setLayoutY((height - t.getLayoutBounds().getHeight()) / 2.0);
        t.setTextAlignment(TextAlignment.CENTER);
        pane.getChildren().add(t);
        timeline.stop();
    }

    /**
     * Adds the hero's health to the view
     */
    private void addHealth() {
        for (ImageView life: health) {
            pane.getChildren().add(life);
        }
    }

    /**
     * Adds the number of lives the hero has to the view
     */
    private void drawLives() {
        this.lives.setText("lives remaining: " + model.getLives());
    }

    private void drawLevelScore() {
        this.levelScore.setText("Level Score: " + model.getLevelScore());
    }

    private void drawScore() {
        this.score.setText("Total Score: " + model.getScore());
    }

    private void drawId() {
        this.levelId.setText("Level: " + model.getCurrentLevelId());
    }

    @Override
    public void update() {


        this.model.setState(oldLevel,oldId,tmpScore,tmpLevelScore,oldLives);
    }

    @Override
    public void save(Map<Integer, Level> oldLevel, int id, double score, double levelScore, int lives) {


        this.oldLevel = oldLevel;

        this.oldId = id;
        this.tmpScore = score;
        this.tmpLevelScore = levelScore;
        this.oldLives = lives;


    }
}
