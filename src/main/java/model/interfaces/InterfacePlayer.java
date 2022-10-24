package model.interfaces;

import model.Score;
import model.Objects.Racket;

public interface InterfacePlayer {

    /**
     * @return Score
     */
    public Score getScore();

    /**
     * @param racket
     */
    public void setRacket(Racket racket);

    /**
     * @return Racket
     */
    public Racket getRacket();
}
