package gui.server_gui;

import java.util.ArrayList;
import java.util.List;

import gui.App;
import gui.Sound;
import gui.game_elements.BallGui;
import gui.game_elements.PlayerGui;
import gui.interfaces.UpdatableGui;
import model.Vector2;
import model.interfaces.InterfaceRacketController.State;
import server.Conversion;

// The problem is hat the racketGui is on the wrong side, the switch is correct
// for the points and the ball, but racketGui doesn't realize that it is on the
// other side...

public class CourtGui {
    private BallGui ball;
    private PlayerGui[] players;

    private Sound soundLost;
    private Sound soundBallRacket;
    private Sound soundBallWall;

    private int clientID;

    private boolean isSoundsActive = true;

    // Constructors
    public CourtGui(PlayerGui[] players, double initialBallRadius) {
        this.players = players;

        ball = new BallGui(new Vector2(-50, -50), initialBallRadius);

        soundLost = new Sound("Sound Perdu.wav"); // à chaque point marqué
        soundBallRacket = new Sound("Bruitage ball racket.wav"); // impact avec une raquette
        soundBallWall = new Sound("Bruitage ball mur.wav"); // impact avec un mur
    }

    // Getters

    /**
     * Returns a list of all the game elements that need to be updated.
     * 
     * @return A list of all the game elements in the court
     */
    public List<UpdatableGui> getListObjects() {
        List<UpdatableGui> list = new ArrayList<>();

        list.add(ball);
        for (PlayerGui player : players)
            list.add(player);

        return list;
    }

    public PlayerGui getPlayerGui(int playerID) {
        return players[playerID];
    }

    public State getState() {
        return players[clientID].getState();
    }

    public int getClientID() {
        return clientID;
    }

    // Setter

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    // Methods
    public void stopSounds() {
        isSoundsActive = false;
    }

    public boolean canSoundsBePlayed() {
        return isSoundsActive && App.soundsButton;
    }

    /**
     * Update te position and the radius of the wall. It also plays a sound if the
     * ball hit the wall or a racket.
     * 
     * @param encodedBall Server information of the ball
     */
    private void updateBall(String[] encodedBall) {

        Vector2 newCoordinates = new Vector2(Double.parseDouble(encodedBall[1]), Double.parseDouble(encodedBall[2]));

        ball.setCoord(newCoordinates);

        ball.setRadius(Double.parseDouble(encodedBall[3]));

        switch (Conversion.intToLastAction(Integer.parseInt(encodedBall[4]))) {
            case NONE:
                break;
            case RACKET_HIT:
                if (canSoundsBePlayed()) {
                    soundBallRacket.play();
                }
                break;
            case WALL_HIT:
                if (canSoundsBePlayed()) {
                    soundBallWall.play();
                }
                break;
        }
    }

    /**
     * Update the position, the width and the height of the racket and the points of
     * the player.
     * 
     * @param encodedPlayer Server information of the player
     */
    private void updatePlayer(String[] encodedPlayer) {

        PlayerGui player;

        if (clientID == 0)
            player = players[Integer.parseInt(encodedPlayer[1])];
        else
            player = players[1 - Integer.parseInt(encodedPlayer[1])];

        player.setCoord(new Vector2(Double.parseDouble(encodedPlayer[2]), Double.parseDouble(encodedPlayer[3])));

        player.setRacketWidth(Double.parseDouble(encodedPlayer[4]));

        player.setRacketHeight(Double.parseDouble(encodedPlayer[5]));

        player.setPoints(Integer.parseInt(encodedPlayer[6]));
    }

    /**
     * Updates the score counting system (max points, lead, etc).
     * 
     * @param encodedPoints Server information of the points
     */
    private void updatePoints(String[] encodedPoints) {
        if (encodedPoints[1].equals("1") && canSoundsBePlayed()) {
            soundLost.play();
        }
    }

    /**
     * It takes a sub-string representing an element of the game and updates it
     * accordingly.
     * 
     * @param info The raw information sent from the server.
     */
    public void parseInfo(String[] info) {
        switch (info[0]) {
            case "ball":
                updateBall(info);
                break;
            case "player":
                updatePlayer(info);
                break;
            case "points":
                updatePoints(info);
                break;
            default:
                break;
        }
    }

}