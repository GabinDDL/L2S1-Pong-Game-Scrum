package model.interfaces;

import model.Vector2;

public interface InterfaceRacket extends InterfaceSolidObject {
    boolean hitBall(boolean left, Vector2 ballPosition, Vector2 nextPosition, double ballRadius);
}
