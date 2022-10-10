package model.interfaces;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public interface InterfaceScore {

    /**
     * Initializes the Scores' position and color
     * @param color
     * @param width
     */
    public void initDisplay(Color color, double width);

    /**
     * Updates and increments the Score integer value by 1
     */
    public void win();

    /**
     * Updates the Score's shown text
     */
    public void updateDisplay();
    
    /**
     * Returns the Text of the Score
     * @return Text
     */
    public Text getTextScore();
    
}
