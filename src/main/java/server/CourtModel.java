package server;

import model.Court;
import model.Vector2;
import model.game_elements.BallModel;
import model.interfaces.InterfaceBallModel.LastAction;
import model.game_elements.PlayerModel;
import model.interfaces.InterfaceRacketController.State;

public class CourtModel {
    private final double width;
    private final double height;

    private final int pointsLimit;
    private boolean needLead = false; // flag to check if we need a 2 points lead

    // Instance state
    private final PlayerModel[] players = new PlayerModel[2]; // Players[0] is the player controlled by the client
    private BallModel ball;

    private LastAction lastAction = LastAction.NONE;

    private boolean wasBallOutsideCourt = false; // True if the ball was outside the court at the previous update
    private boolean wasGameWon = false; // True if the game was won at the previous update

    // Constructors

    public CourtModel(PlayerModel playerA, PlayerModel playerB, double width, double height, int pointsLimit) {

        this.width = width;
        this.height = height;

        this.pointsLimit = pointsLimit;

        this.players[0] = playerA;
        this.players[1] = playerB;

        ball = new BallModel(new Vector2(0, 0), Court.INITIAL_BALL_SPEED, Court.INITIAL_MAJOR_SPEED,
                Court.INITIAL_BALL_RADIUS);

        for (PlayerModel p : players)
            p.reset(height);

        serve();
    }

    public CourtModel(double width, double height, int pointsLimit) {
        this(new PlayerModel(true, width), new PlayerModel(false, width), width, height, pointsLimit);
    }

    // Getter

    public PlayerModel[] getPlayersModel() {
        return players;
    }

    // Methods

    /**
     * Updates the positions of the objects
     * If a player wins, it resets the objects
     * 
     * @param deltaT time passed
     * @param states List of states of the players
     */

    public void update(double deltaT, State[] states) {
        // Update player positions

        wasBallOutsideCourt = false;
        wasGameWon = false;

        players[0].update(deltaT, height, states[0]);
        players[1].update(deltaT, height, states[1]);

        lastAction = ball.update(deltaT, height, getPlayersModel());

        if (isBallOutside()) {
            if (gameWon()) {
                needLead = false; // Lower Flag
                wasGameWon = true;
                reset();
            } else {
                serve();
            }
            wasBallOutsideCourt = true;
        }

    }

    /**
     * Returns {@code true} if a player lost the point and updates the points
     * accordingly.
     * 
     * @return true if a player lost the point
     */
    private boolean isBallOutside() {

        // Check if someone wins (if the ball exits the Court)

        // if the ball leaves from the left side
        if (ball.getCoordX() < -70) {
            // player 0 loses and player 1 wins a point
            players[1].incrementPoints();
            return true;
        }
        // if the ball leaves from the right side
        else if (ball.getCoordX() > width + 70) {
            // player 1 loses and player 0 wins a point
            players[0].incrementPoints();
            return true;
        }
        return false;
    }

    /**
     * Resets the ball position
     */
    public void serve() {
        ball.reset(width, height);
    }

    /**
     * Resets the Court and its elements
     */
    public void reset() {

        for (PlayerModel p : players) {
            p.reset(height);
            p.resetPoints();
        }

        ball.reset(width, height);
    }

    /**
     * Checks if the Court has a winner
     * 
     * @return true if there's a winner; otherwise false
     */
    public boolean gameWon() {

        PlayerModel playerA = players[0];
        PlayerModel playerB = players[1];

        // Infinite game
        if (pointsLimit <= 0)
            return false;

        // Please mind the division's rounding down behavior in Java
        int upperLimit = (int) Math.ceil((double) 10 / (double) 7 * pointsLimit);

        if (!needLead) {

            // If there's a draw before the Match Point
            if (playerA.isDraw(playerB) &&
                    playerA.pointsEqualTo(pointsLimit - 1)) {
                needLead = true; // Raise Flag
                return false;
            }

            // If a Player gets to the first limit of amount of points
            if (playerA.pointsEqualTo(pointsLimit) ||
                    playerB.pointsEqualTo(pointsLimit))
                return true;

        } else {

            // If a Player gets to the last limit of amount of points
            if (playerA.pointsEqualTo(upperLimit) ||
                    playerB.pointsEqualTo(upperLimit)) {
                return true;
            }

            // If a Player gets a 2 points lead
            if (Math.abs(playerA.getPoints() - playerB.getPoints()) >= 2)
                return true;

        }
        return false;
    }

    /**
     * It converts the ball's information into a string. The information has this
     * form:
     * 
     * <blockquote>
     * <b>"ball";coordX;coordY;radius;LastAction</b>
     * </blockquote>
     * 
     * @return A string that contains the information about the ball.
     */
    private String encodeBall() {
        StringBuilder sb = new StringBuilder();

        sb.append("ball");
        sb.append(";");
        sb.append(ball.getCoordX());
        sb.append(";");
        sb.append(ball.getCoordY());
        sb.append(";");
        sb.append(ball.getSize());
        sb.append(";");
        sb.append(Conversion.lastActionToInt(lastAction));
        sb.append(":");

        return sb.toString();
    }

    /**
     * It converts the players's information into a string. The information has this
     * form:
     * 
     * <blockquote>
     * <b>"player";isPlayerLeft;coordX;coordY;width;height;points</b>
     * </blockquote>
     * 
     * @return A string that contains the information about the player.
     */
    private String encodePlayer() {
        StringBuilder sb = new StringBuilder();

        for (PlayerModel player : players) {

            sb.append("player");
            sb.append(";");
            sb.append(player.isPlayerLeft() ? 0 : 1);
            sb.append(";");
            sb.append(player.getCoordX());
            sb.append(";");
            sb.append(player.getCoordY());
            sb.append(";");
            sb.append(player.getRacketWidth());
            sb.append(";");
            sb.append(player.getRacketHeight());
            sb.append(";");
            sb.append(player.getPoints());
            sb.append(":");

        }

        return sb.toString();
    }

    /**
     * It returns a string that contains whether the ball is outside the screen, and
     * whether the game is won. This information is used for the client to know when
     * to stop the game. The information has this form:
     * 
     * <blockquote>
     * <b>"points";wasBallOutsideCourt;wasGameWon</b>
     * </blockquote>
     * 
     * @return A string that contains whether the ball is outside, and
     *         whether the game is won.
     */
    private String encodePoints() {
        StringBuilder sb = new StringBuilder();

        sb.append("points");
        sb.append(";");
        sb.append(wasBallOutsideCourt ? 1 : 0);
        sb.append(";");
        sb.append(wasGameWon ? 1 : 0);
        sb.append(":");

        return sb.toString();
    }

    /**
     * It returns a string that contains the information about the Court. The
     * information has this form:
     * 
     * <blockquote>
     * <b> ball:playerLeft;playerRight;points</b>
     * </blockquote>
     * 
     * @return A string that contains the information about the Court.
     */
    public String encode() {
        StringBuilder sb = new StringBuilder();

        sb.append(encodeBall());

        sb.append(encodePlayer());

        sb.append(encodePoints());

        return sb.toString();
    }

}