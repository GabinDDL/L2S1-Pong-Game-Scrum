package gui.server_gui;

import gui.Sound;
import gui.controllers.SceneDisplayController;
import gui.game_elements.BallGui;
import gui.game_elements.PlayerGui;
import gui.interfaces.UpdatableGui;
import server.ClientSideConnection;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

public class ClientGameView {

    // class parameters
    private final CourtGui courtGui;
    private final Pane gameRoot; // main node of the game
    private final double scale;
    private SceneDisplayController sceneDisplayModifier;

    private final ClientSideConnection clientSideConnection;

    private double xMargin = 50.0;

    private Sound backgroundMusic; // Background music

    private int clientID;

    // Constructeur

    /**
     * Creates a new {@code ClientGameView} object. It creates a new CourtGui object
     * and
     * initializes the gameRoot node.
     * 
     * @param courtGui             the {@code CourtGui} object
     * @param game                 the {@code Pane} object
     * @param scale                the scale of the game
     * @param sceneDisplayModifier the {@code SceneDisplayController} object
     * @param clientSideConnection the {@code ClientSideConnection} object
     */
    public ClientGameView(CourtGui courtGui, Pane game, double scale, SceneDisplayController sceneDisplayModifier,
            ClientSideConnection clientSideConnection) {

        this.courtGui = courtGui;
        this.gameRoot = game;
        this.scale = scale;
        this.sceneDisplayModifier = sceneDisplayModifier;
        this.clientSideConnection = clientSideConnection;
        this.clientID = clientSideConnection.getPlayerID();

        game.setMinWidth(clientSideConnection.getWidth() * scale + 2 * xMargin);
        game.setMinHeight(clientSideConnection.getHeight() * scale);

        courtGui.setClientID(clientSideConnection.getPlayerID());

        // this.changeImageBackground("terrain.jpg"); // edit wallpaper

        backgroundMusic = new Sound("GameMusicLoop.wav");
        backgroundMusic.loop(); // play sound background

        for (UpdatableGui object : courtGui.getListObjects()) {
            if (object instanceof BallGui)
                ((BallGui) object).initDisplay(scale, xMargin);
            else if (object instanceof PlayerGui)
                ((PlayerGui) object).initDisplayRacket(scale, xMargin, 10);
        }

        for (Object object : courtGui.getListObjects()) {
            if (object instanceof BallGui)
                gameRoot.getChildren().add(((BallGui) object).getCircle());
            else if (object instanceof PlayerGui)
                gameRoot.getChildren().add(((PlayerGui) object).getShape());
        }
    }

    // Getters
    public double getMarginX() {
        return xMargin;
    }

    public double getScale() {
        return scale;
    }

    public double getHeight() {
        return clientSideConnection.getHeight();
    }

    public double getWidth() {
        return clientSideConnection.getWidth();
    }

    // Setters
    public void setMarginX(double margin) {
        xMargin = margin;
    }

    // Methods

    public void stopMusicAndSounds() {
        backgroundMusic.stop(); // stop sound background
        courtGui.stopSounds();
    }

    public void animate() {
        new AnimationTimer() {
            boolean isGameOn = true;
            long last = 0;

            @Override
            public void handle(long now) {

                if (last == 0) { // ignore the first tick, just compute the first deltaT
                    last = now;
                    return;
                }

                if (sceneDisplayModifier.isInGame())
                    update();

                last = now;
            }

            /**
             * Updates all elements of the game GUI
             */
            private void update() {
                updateServer();
                updateCourt();
                updateDisplays();
            }

            /**
             * Updates clientSideConnection with the new information from the server
             */
            private void updateServer() {
                clientSideConnection.setRacketStatus(courtGui.getState());
                clientSideConnection.sendRacketStatus();
            }

            private void updateCourt() {
                String encodedInfo = clientSideConnection.getEncodedInfo();
                if (encodedInfo == null)
                    return;

                String[] info = encodedInfo.split(":");

                for (String string : info) {
                    String[] splittedString = string.split(";");

                    // If the game status (paused or unpaused) has changed
                    if (splittedString[0].equals("game"))
                        isGameOn = splittedString[1].equals("true");
                    else
                        courtGui.parseInfo(splittedString);
                }
            }

            /**
             * Updates graphical part of the elements on court.
             */
            private void updateDisplays() {
                for (UpdatableGui object : courtGui.getListObjects()) {
                    if (object instanceof BallGui)
                        ((BallGui) object).updateDisplay(scale, xMargin);
                    else if (object instanceof PlayerGui)
                        ((PlayerGui) object).updateDisplay(scale, new double[] { xMargin });
                }
            }
        }.start();
    }
}