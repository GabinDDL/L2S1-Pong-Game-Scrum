package model.game_elements;

import gui.game_elements.BallGui;
import gui.interfaces.InterfaceBallGui;

import model.Vector2;

import javafx.scene.shape.Shape;

/**
 * This class represents the ball of the game. It includes the graphical part of
 * the ball and the model of the ball.
 * 
 */
public class Ball extends BallModel implements InterfaceBallGui {
    private BallGui ballGui;

    // Constructor
    public Ball(Vector2 coord, double InitialSpeed, double size) {
        super(coord, InitialSpeed, size);
        ballGui = new BallGui(coord, size);
    }

    // Getters
    @Override
    public Shape getShape() {
        return ballGui.getShape();
    }

    // Setters
    @Override
    public void setRadius(double radius) {
        ballGui.setRadius(radius);
    }

    @Override
    public void setCoords(Vector2 coords) {
        ballGui.setCoords(coords);
    }

}
