package model.interfaces;

import model.Court;

public interface InterfaceAlea {
    enum TypeAlea {
        newBall, // Adds a new ball in the game (The newly added ball is removed when it goes out of bounds)
        racketAcceleration, // Increases the acceleration and the initial speed of a player's racket
        skatingRink, // The racket of the player slips more
        doublePoint, // The player wins 2 points when scoring in the round instead of 1
        nothing // explicitly nothing
    };

    public TypeAlea getTypeAlea();

    public boolean isResetAble();

    public void reset(Court court);

    public void newAlea(Court court);
}
