package model.game_elements;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Court;
import model.Vector2;
import model.interfaces.InterfaceAlea;

// An alea is an event that occurs during a game (in Court a new alea happens at the start of a round)
public class Alea implements InterfaceAlea {

    private Random rd = new Random();
    private boolean resetAble = false;
    private TypeAlea typeAlea = TypeAlea.nothing;

    @Override
    public TypeAlea getTypeAlea() {
        return typeAlea;
    }

    public TypeAlea intToType(int n) {
        switch (n) {
            case 0:
                return TypeAlea.newBall;
            case 1:
                return TypeAlea.racketAcceleration;
            case 2:
                return TypeAlea.skatingRink;
            case 3:
                return TypeAlea.doublePoint;
            default:
                return TypeAlea.nothing;
        }
    }

    /**
     * @return true if the alea can be reset or not
     */
    @Override
    public boolean isResetAble() {
        return resetAble;
    }

    /**
     * reset the alea with TypeAlea
     * 
     * @param court
     */
    public void reset(Court court) {
        switch (typeAlea) {
            case racketAcceleration:
                for (PlayerModel pm : court.getPlayersModel()) {
                    pm.setAcceleration(court.getInitialRacketAcceleration());
                    pm.setMajorSpeed(court.getInitialRacketMajorSpeed());
                }
                break;
            case skatingRink:
                for (PlayerModel pm : court.getPlayersModel()) {
                    pm.setDecceleration(court.getInitialRacketDeceleration());
                }
                break;
            default:
                break;
        }
        resetAble = false;
    }

    /**
     * add the parameter ball for the reset
     * 
     * @param c
     * @param b
     */
    public void reset(Court c, Ball b) {
        if (typeAlea == TypeAlea.newBall) {
            c.removeBall(b);
            resetAble = false;
        } else {
            reset(c);
        }
    }

    /**
     * add the parameter player for the reset
     * 
     * @param c
     * @param p
     */
    public void reset(Court c, Player p) {
        if (typeAlea == TypeAlea.doublePoint && resetAble) {
            p.incrementScore();
            resetAble = false;
        } else {
            reset(c);
        }
    }

    /**
     * Change the alea's type randomly and reset the reset
     */
    @Override
    public void newAlea(Court court) {
        resetAble = true;
        typeAlea = intToType(rd.nextInt(7));
        switch (typeAlea) {
            case newBall: // New ball
                Ball newBall = new Ball(new Vector2(0, 0), court.getInitialBallSpeed(), court.getInitialMajorSpeed(),
                        court.getInitialBallRadius());
                newBall.initDisplay(court.getScale(), court.getxMargin());
                court.addBall(newBall);
                break;
            case racketAcceleration: // Acceleration of racket
                for (PlayerModel pm : court.getPlayersModel()) {
                    pm.setAcceleration(court.getInitialRacketAcceleration() + 400);
                    pm.setMajorSpeed(court.getInitialRacketMajorSpeed() + 300);
                }
                break;
            case skatingRink: // Skating rink
                for (PlayerModel pm : court.getPlayersModel()) {
                    pm.setDecceleration(0.97);
                }
                break;
            case doublePoint:
                // already resetAble
                break;
            default:
                resetAble = false;
                break;
        }
        show(court);
    }

    /**
     * print the alea in the game
     * 
     * @param court
     */
    public void show(Court court) {
        String s = "";

        switch (typeAlea) {
            case newBall:
                s = "New ball";
                break;
            case racketAcceleration:
                s = "Players Acceleration";
                break;
            case skatingRink:
                s = "Skating ring";
                break;
            case doublePoint:
                s = "Point x2";
                break;
            case nothing:
                break;
        }

        Text text = new Text(s);
        text.setFill(Color.GOLDENROD);
        text.setX(court.getWidth() * court.getScale() / 2 + court.getxMargin());
        text.setY(court.getHeight() / 2);

        // Adds the text to the pane
        court.addText(text);

        // Creates a new ScaleTransition that will increase the scale size of the
        // timer's text

        ScaleTransition st = new ScaleTransition(Duration.seconds(1.5), text);

        st.setByX(10f);
        st.setByY(10f);

        text.setFont(Font.font(text.getFont().getFamily(), 5));

        // At the end of the animation, if the timer > 1, the timer is launched again
        // with duration minus one
        // Else the game is animated
        st.setOnFinished(e -> {

            court.removeText(text);
        });

        RotateTransition rt = new RotateTransition(Duration.seconds(0.7), text);

        rt.setByAngle(360);
        rt.setInterpolator(Interpolator.EASE_IN);

        // Launches the animation
        st.play();
        rt.play();
    }
}
