package model.interfaces;

import javafx.scene.shape.Circle;
import model.Vector2;

public interface InterfaceBall {
    void computeRacketBounce(Vector2 nextPosition, double deltaT, RacketController racket, boolean playerA);
}
