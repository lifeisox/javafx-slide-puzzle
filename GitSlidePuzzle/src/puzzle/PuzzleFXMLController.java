package puzzle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.animation.AnimationTimer;

public class PuzzleFXMLController implements Initializable {
    
    private final int MAX_PANEL = 16;
    private final int START_X = 1;
    private final int START_Y = 1;
    private final int PANEL_SIZE = 80;
    private final long ONE_SECOND = 1000000000;
    
    PuzzleBody[] body = new PuzzleBody[MAX_PANEL];
    private final StringProperty counter = new SimpleStringProperty("0");
    private final StringProperty second = new SimpleStringProperty("0");
    private AnimationTimer timer;
    private long lastTime;
    
    
    @FXML Label countLabel, timeLabel, gameOver;
    @FXML Pane gamePane, answerPane;
    @FXML Button resetButton, exitButton;
    
    @FXML
    private void resetButtonClick(ActionEvent event) {
        initGame();
    }
    
    @FXML
    private void exitButtonClick(ActionEvent event) {
        if (gameOver.isVisible()==false)
            timer.stop();
        Platform.exit();
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        countLabel.textProperty().bind(counter);
        timeLabel.textProperty().bind(second);
        
        for (int i = 0; i < MAX_PANEL; i++) {
            int x = START_X + PANEL_SIZE * (i % 4);
            int y = START_Y + PANEL_SIZE * (i / 4);
            body[i] = new PuzzleBody(x, y, new Rectangle(x, y, PANEL_SIZE, PANEL_SIZE),
                    new Rectangle(x, y, PANEL_SIZE, PANEL_SIZE));
            gamePane.getChildren().add(body[i].getGamePanel());
            answerPane.getChildren().add(body[i].getAnswerPanel());
            
            body[i].getGamePanel().setOnMousePressed(event -> {
                for (PuzzleBody bd : body) {
                    if (bd.getGamePanel() == event.getSource()) {
                        if (bd.getGameIndex() != MAX_PANEL - 1 && gameOver.isVisible() == false)
                            swapPanel(bd);
                    }
                }
            });
            
            body[i].getGamePanel().setOnMouseEntered(event -> {
                Rectangle temp = (Rectangle) event.getSource();
                if (temp.getFill() != Color.BLACK)
                    temp.setStroke(Color.WHEAT);
            });
            
            body[i].getGamePanel().setOnMouseExited(event -> {
                Rectangle temp = (Rectangle) event.getSource();
                if (temp.getFill() != Color.BLACK)
                    temp.setStroke(Color.BLACK);
            });
        }
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0)
                    lastTime = now;
                if(now - lastTime > ONE_SECOND){
                    second.set(String.valueOf(Integer.parseInt(second.get())+1));
                    lastTime = now;
                }
            }
        };
              
        initGame();
    }
    
    private void initGame() {
        
        sufflePanel();
        counter.set("0");
        second.set("0");
        lastTime = 0;
        timer.start();
        gameOver.setVisible(false);
    }
    
    private void sufflePanel() {
        ArrayList<Integer> room = new ArrayList<>();
        int max = MAX_PANEL - 1;
        int current = 0;
        Random rand = new Random();
        
        for (int i = 0; i < MAX_PANEL; i++)
            room.add(i);
        for (int i = 0; i < MAX_PANEL - 1; i++) {
            int num = rand.nextInt(max--);
            body[current++].setAnswerIndex(room.get(num));
            room.remove(num);
        }
        body[current].setAnswerIndex(room.get(max));
        
        for (int i = 0; i < MAX_PANEL; i++)
            body[i].setGameIndex(body[i].getAnswerIndex());
        
        current = MAX_PANEL - 1;
        boolean left, right, up, down;
        for (int i = 0; i < 100000; i++) {
            int num = rand.nextInt(4);
            switch (num) {
                case 0: // change upper panel
                    if (current >= 4) {
                        current -= 4;
                        swapPanel(body[current]);
                    }
                    break;
                case 1: // change right panel
                    if (current % 4 != 3) {
                        current += 1;
                        swapPanel(body[current]);
                    }
                    break;
                case 2:
                    if (current <= 11) {
                        current += 4;
                        swapPanel(body[current]);
                    }
                    break;
                default:
                    if (current % 4 != 0) {
                        current -= 1;
                        swapPanel(body[current]);
                    }
            }
        }
    }
    
    private void swapPanel(PuzzleBody bd) {
        int index = 0;
        boolean left, right, up, down;
        int target, temp;
        
        for (int i = 0; i < MAX_PANEL; i++)
            if (bd.getGameIndex() == body[i].getGameIndex()) {
                index = i;
                break;
            }
        left = index % 4 == 0 ? false : true;
        right = index % 4 == 3 ? false : true;
        up = index < 4 ? false : true;
        down = index > 11 ? false : true;
        
        if (left && body[index-1].getGameIndex() == MAX_PANEL - 1)
            target = index - 1;
        else if (right && body[index+1].getGameIndex() == MAX_PANEL - 1)
            target = index + 1;
        else if (up && body[index-4].getGameIndex() == MAX_PANEL - 1)
            target = index - 4;
        else if (down && body[index+4].getGameIndex() == MAX_PANEL - 1)
            target = index + 4;
        else
            return;
        temp = body[target].getGameIndex();
        body[target].setGameIndex(body[index].getGameIndex());
        body[index].setGameIndex(temp);
        
        counter.set(String.valueOf(Integer.parseInt(counter.get())+1));
        
        for (int i = 0; i < MAX_PANEL; i++) {
            if (body[i].getAnswerIndex() != body[i].getGameIndex())
                return;
        }
        gameOver.setVisible(true);
        timer.stop();
    }
}

