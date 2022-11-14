package model.interfaces;

public interface InterfaceHasDifficulty {

    enum Difficulty {
        EASY, NORMAL, HARD
    };

    public Difficulty getDifficulty();

}
