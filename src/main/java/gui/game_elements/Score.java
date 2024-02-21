package gui.game_elements;

import model.interfaces.InterfaceScore;

import javafx.scene.control.Label;

public class Score implements InterfaceScore {

    private int points;
    private final Label label;

    // Constructors

    public Score(Label label) {
        this(0, label);
    }

    public Score(int points, Label label) {
        this.points = points;
        this.label = label;
    }

    // Getters

    public int getPoints() {
        return points;
    }

    public Label getLabel() {
        return label;
    }

    // Setters

    /**
     * Sets the Score's associated integer value to points
     * 
     * @param points
     */
    public void setPoints(int points) {
        this.points = points;
        label.setText(String.valueOf(points));
    }

    // Methods

    /**
     * Adds one to the score and updates the label of the score
     */
    @Override
    public void incrementScore() {
        points++;
        label.setText(String.valueOf(points));
    }

    /**
     * Resets the points at 0 and updates the label
     */
    @Override
    public void resetScore() {
        points = 0;
        label.setText("0");
    }

}
