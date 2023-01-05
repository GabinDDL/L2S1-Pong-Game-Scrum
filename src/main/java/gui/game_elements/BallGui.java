package gui.game_elements;

import gui.interfaces.InterfaceBallGui;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import model.Vector2;

/**
 * This class represents a ball in the GUI.
 * It has no knowledge of the game logic.
 * It is only used to display the ball.
 * 
 */
public class BallGui implements InterfaceBallGui {

    private Circle ball;

    // Constructors
    public BallGui(Vector2 coord, double radius) {
        ball = new Circle();
        ball.setRadius(radius);
        ball.setCenterX(coord.getXdir());
        ball.setCenterY(coord.getYdir());
    }

    // Getters
    public double getCoordX() {
        return ball.getCenterX();
    }

    public double getCoordY() {
        return ball.getCenterY();
    }

    @Override
    public Shape getShape() {
        return ball;
    }

    // Setters
    public void setRadius(double radius) {
        ball.setRadius(radius);
    }

    public void setCoords(Vector2 coords) {
        ball.setCenterX(coords.getXdir());
        ball.setCenterY(coords.getYdir());
    }

}
