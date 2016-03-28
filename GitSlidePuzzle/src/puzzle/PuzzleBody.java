package puzzle;

import javafx.scene.shape.Rectangle;

public class PuzzleBody {
    private final int locateX;
    private final int locateY;
    private int gameIndex;
    private int answerIndex;
    private final Rectangle ansPanel;
    private final Rectangle gamePanel;
    
    public PuzzleBody() {
        this(0, 0, 0, 0, null, null);
    }
        
    public PuzzleBody(int x, int y, Rectangle gi, Rectangle ai) {
        this(x, y, 0, 0, gi, ai);
    }
    
    public PuzzleBody(int x, int y, int gi, int ai,
            Rectangle game, Rectangle ans) {
        
        locateX = x;
        locateY = y;
        gameIndex = gi;
        answerIndex = ai;
        
        gamePanel = game;
        gamePanel.setArcHeight(15.0);
        gamePanel.setArcWidth(15.0);
        gamePanel.getStyleClass().add("puzzle-style-no" + gi);
        
        ansPanel = ans;
        ansPanel.setArcHeight(15.0);
        ansPanel.setArcWidth(15.0);
        ansPanel.getStyleClass().add("puzzle-style-no" + ai);
    }
    
    public int getLocateX() { return locateX; }
    public int getLocateY() { return locateY; }
    public int getGameIndex() { return gameIndex; }
    public int getAnswerIndex() { return answerIndex; }
    public Rectangle getGamePanel() { return gamePanel; }
    public Rectangle getAnswerPanel() { return ansPanel; }
    
    public void setGameIndex(int i) {
        gameIndex = i;
        gamePanel.getStyleClass().clear();
        gamePanel.getStyleClass().add("puzzle-style-no" + i);
    }
    
    public void setAnswerIndex(int i) {
        answerIndex = i;
        ansPanel.getStyleClass().clear();
        ansPanel.getStyleClass().add("puzzle-style-no" + i);
    }

}
