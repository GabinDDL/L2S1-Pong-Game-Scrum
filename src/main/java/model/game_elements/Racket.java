package model.game_elements;

import gui.game_elements.RacketGui;
import gui.interfaces.InterfaceRacketGui;

import model.Vector2;

import javafx.scene.shape.Shape;

public class Racket extends RacketModel implements InterfaceRacketGui {
    private RacketGui racketGui;

    // Constructor
    public Racket(Vector2 coord, double speed, double racketWidth, double size) {
        super(coord, speed, size, racketWidth);
        racketGui = new RacketGui(coord, racketWidth, size);
    }

    // Getters
    @Override
    public Shape getShape() {
        return racketGui.getShape();
    }

    public RacketGui getRacketGui() {
        return racketGui;
    }
}