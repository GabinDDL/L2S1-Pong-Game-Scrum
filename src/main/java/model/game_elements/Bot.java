package model.game_elements;

import java.util.ArrayList;

import gui.game_elements.PlayerGui;
import gui.game_elements.Score;

import model.Vector2;
import model.interfaces.InterfaceHasDifficulty;

public class Bot extends Player implements InterfaceHasDifficulty {

    private Difficulty difficulty;

    // Constructors

    public Bot(Racket racket, Score score, Difficulty difficulty) {
        playerModel = new BotModel(racket.getRacketModel(), score.getPoints());
        this.playerGui = new PlayerGui(racket.getRacketGui(), score);
        this.difficulty = difficulty;
    }

    public Bot(Vector2 coords, double speed, double racketWidth, double racketHeight, Score score,
            Difficulty difficulty) {
        this(new Racket(coords, speed, racketWidth, racketHeight), score, difficulty);
    }

    public Bot(Difficulty difficulty) {
        this.playerGui = new PlayerGui();
        this.playerModel = new BotModel();
        this.difficulty = difficulty;
    }

    // Getter

    public Difficulty getDifficulty() {
        return difficulty;
    }

    // Methods

    @Override
    public void resetRacket(double height) {
        ((BotModel) playerModel).reset(height);
    }

    /**
     * Reset hasPrivate and the state of the bot
     */
    public void resetPredict() {
        switch (difficulty) {
            case NORMAL:
            case HARD:
                ((BotModel) playerModel).resetPredict();
                break;
            default:
                return;
        }
    }

    public void update(double deltaT, double height, double width, ArrayList<Ball> ballList) {
        ((BotModel) playerModel).update(deltaT, height, width, ballList, difficulty);
        playerGui.getRacketGui().setCoordY(playerModel.getRacket().getCoordY());
    }
}
