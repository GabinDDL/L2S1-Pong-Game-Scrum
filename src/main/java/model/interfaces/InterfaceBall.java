package model.interfaces;

import model.Vector2;
import model.Objects.Racket;

public interface InterfaceBall {
    void computeRacketBounce(Vector2 nextPosition, double deltaT, Racket racket, boolean playerA);
}
