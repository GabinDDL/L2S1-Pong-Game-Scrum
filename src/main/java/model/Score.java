package model;

import javafx.scene.control.Label;
import model.interfaces.InterfaceScore;

public class Score implements InterfaceScore {

    private int points;
    private final Label label;

    public Score(Label label) {
        this(0, label);
    }

    public Score(int points, Label label) {
        this.points = points;
        this.label = label;
    }

    public int getPoints() {
        return points;
    }

    /**
     * Sets the Score's associated integer value to points
     * 
     * @param points
     */
    public void setPoints(int points) {
        this.points = points;
    }

    public void incrementScore() {
        points++;
        label.setText(String.valueOf(points));
    }

    public Label getLabel() {
        return label;
    }

}
