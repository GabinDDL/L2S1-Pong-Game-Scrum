package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import gui.game_elements.PlayerGui;
import gui.game_elements.Score;
import gui.server_gui.ClientGameView;
import gui.server_gui.CourtGui;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.MediaHandler;
import model.interfaces.InterfaceRacketController.State;
import server.ClientSideConnection;
import server.GameServer;

/**
 * Class which allow to play the main game
 */
public class ServerPongController implements Initializable {

    // Elements created and described in classicPongMenu.fxml
    @FXML
    BorderPane serverPongMenuContainer;
    @FXML
    ImageView leftArrow;
    @FXML
    Text title;
    @FXML
    AnchorPane informations;
    @FXML
    TextField serverIpTextField;

    private Pane gameplay;

    private ClientGameView clientGameView;

    private BorderPane pauseMenu;
    private Button quitButton;
    private boolean inReset = false;

    private ClientSideConnection clientSideConnection;
    private Thread clientSideConnectionThread;

    public static GameServer gameServer;
    public static Thread gameServerThread;

    /**
     * Class controlling what's, and more importantly when, being displayed on the
     * screen
     */
    private static class SceneDisplayModifier implements SceneDisplayController {

        // Scene that beings displaying
        SceneDisplay actualView = SceneDisplay.GAME;

        @Override
        public boolean isInGame() {
            return actualView == SceneDisplay.GAME;
        }

        @Override
        public void setScene(SceneDisplay sD) {
            actualView = sD;
        }

        /**
         * Alter between Game and Pause in a game
         */
        @Override
        public void pauseUnpause() {
            switch (actualView) {
                case GAME:
                    setScene(SceneDisplay.PAUSE);
                    break;
                case PAUSE:
                    setScene(SceneDisplay.GAME);
                    break;
                default:
                    break;
            }
        }

        @Override
        public boolean isOnPause() {
            return actualView == SceneDisplay.PAUSE;
        }
    }

    private static SceneDisplayController sceneDisplayModifier = new SceneDisplayModifier();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        quitButton = new Button("MENU");

        quitButton.setPrefSize(150, 50);
        quitButton.setFont(new Font("Brandish", 20.0));

        pauseMenu = new BorderPane(quitButton, null, null, null, null);
        pauseMenu.setPadding(new Insets(300, 300, 0, 300));
        pauseMenu.setMinWidth(1100.0);

        quitButton.addEventHandler(ActionEvent.ACTION, (ActionEvent e) -> {
            try {
                loadMenu(null);
            } catch (IOException error) {
                error.printStackTrace();
            }
        });
    }

    /**
     * Load the animation translating to the Servers's Pane (left)
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    public void loadClassicPage(MouseEvent event) throws IOException {
        TransitionClass.transition(true, true, "classicPongMenu.fxml", Interpolator.EASE_IN,
                serverPongMenuContainer, null);
    }

    @FXML
    public void startServer(MouseEvent event) throws IOException {
        gameServer = new GameServer();
        gameServerThread = new Thread(gameServer);
        gameServerThread.start();

        TransitionClass.transition(false, true, "host.fxml", Interpolator.EASE_IN,
                serverPongMenuContainer, null);

    }

    /**
     * Launch the game. Starts with an animation.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    public void playGame(MouseEvent event) throws IOException {

        // Refers to gameScene initialized in App.java
        Scene gameScene = serverPongMenuContainer.getScene();

        // Getting .fxml file's URL to load the Score display style.
        URL fxmlUrl = MediaHandler.getFXMLURL("init.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        gameplay = loader.load();

        // Adding StyleClass, useful in `background.css`
        gameplay.getStyleClass().add("root");

        gameplay.getChildren().addAll(pauseMenu);
        pauseMenu.setDisable(true);
        pauseMenu.setVisible(false);

        // Associate Labels to Players
        // Init player
        ControllerFXML labels = loader.getController();

        Score scoreA = new Score(labels.getLabelA());
        PlayerGui playerA = new PlayerGui();
        playerA.setScore(scoreA);

        Score scoreB = new Score(labels.getLabelB());
        PlayerGui playerB = new PlayerGui();
        playerB.setScore(scoreB);

        bindKeys(gameScene, playerA);

        try {
            initServerConnection();
        } catch (Exception e) {
            return;
        }

        while (!clientSideConnection.getIsGameOn()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Initializing game...");

        CourtGui court;
        if (clientSideConnection.getPlayerID() == 0) {
            court = new CourtGui(new PlayerGui[] { playerA, playerB }, 10.);
        } else {
            court = new CourtGui(new PlayerGui[] { playerB, playerA }, 10.);
        }

        clientGameView = new ClientGameView(court, gameplay, 1.0, sceneDisplayModifier, clientSideConnection);

        // Starts the transition to the game, starts a decount of 3 at the end of the
        // animation.
        TransitionClass.classicGameTransition(false, true, Interpolator.EASE_IN, serverPongMenuContainer, gameplay,
                new EndFunction() {
                    @Override
                    public void end() {
                        launchTimer(gameplay, court, clientGameView, 3, clientSideConnection, new EndFunction() {
                            @Override
                            public void end() {
                                clientGameView.animate();
                            }
                        });
                    }
                });
    }

    private void bindKeys(Scene gameScene, PlayerGui playerA) {
        // We bind the pressing of the keys to the mouvement of the rackets and the
        // desire to put the game in pause
        gameScene.setOnKeyPressed(ev -> {
            switch (ev.getCode()) {
                case SHIFT:
                    playerA.setState(State.GOING_UP);
                    break;
                case CONTROL:
                    playerA.setState(State.GOING_DOWN);
                    break;
                case ESCAPE:
                    if (!inReset) {
                        sceneDisplayModifier.pauseUnpause();

                        displayPauseButtons(!sceneDisplayModifier.isInGame());
                    }
                    break;
                case Q:
                    if (!sceneDisplayModifier.isInGame()) {
                        try {
                            loadMenu(ev);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        });

        // We bind the release of the keys to the IDLE state
        gameScene.setOnKeyReleased(ev -> {
            switch (ev.getCode()) {
                case SHIFT:
                    if (playerA.getState() == State.GOING_UP)
                        playerA.setState(State.IDLE);
                    break;
                case CONTROL:
                    if (playerA.getState() == State.GOING_DOWN)
                        playerA.setState(State.IDLE);
                    break;
                default:
                    break;
            }
        });
    }

    public void displayPauseButtons(boolean display) {
        pauseMenu.setVisible(display);
        pauseMenu.setDisable(!display);
    }

    private void initServerConnection() {
        String serverIp = serverIpTextField.getText();

        String regexIP = "^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(.(?!$)|$)){4}$";

        if (!serverIp.matches(regexIP) && !serverIp.equals("localhost") && !serverIp.equals("")) {
            System.out.println("Invalid IP");
            throw new IllegalArgumentException("Invalid IP");
        }

        System.out.println("Creating ClientSideConnection...");
        try {
            if (serverIp.equals("") || serverIp.equals("localhost")) {
                clientSideConnection = new ClientSideConnection("");
            } else {
                clientSideConnection = new ClientSideConnection(serverIp);
            }
        } catch (IOException e) {
            System.out.println("Server not found");
            throw new IllegalArgumentException("Server not found");
        }

        System.out.println("ClientSideConnection created");

        System.out.println("Starting ClientSideConnectionThread...");
        clientSideConnectionThread = new Thread(clientSideConnection);
        clientSideConnectionThread.start();

        System.out.println("ClientSideConnectionThread started");
    }

    public void loadMenu(KeyEvent event) throws IOException {

        clientGameView.stopMusicAndSounds();

        TransitionClass.transition(false, false, "server.fxml", Interpolator.EASE_IN,
                gameplay, new EndFunction() {
                    @Override
                    public void end() {
                        sceneDisplayModifier.pauseUnpause();
                        inReset = true;
                        clientSideConnection.closeConnection();
                        clientSideConnectionThread.interrupt();
                    }
                });
    }

    /**
     * Add a counter of 'duration' duration in the center of court.
     * When finished, if the counter has reached 0, the game is launch, else the
     * function is once again called with duration - 1
     */
    private void launchTimer(Pane game, CourtGui court, ClientGameView gameView, int duration,
            ClientSideConnection clientSideConnection, EndFunction endfunc) {

        inReset = true;

        // Creates a new timer, sets it visible, sets its color
        Text timer = new Text("" + duration);
        timer.setFill(Color.GOLDENROD);

        // Defines a new StackPane containing the timer just created, sets its height,
        // width and alignment
        StackPane compteur = new StackPane(timer);

        compteur.setLayoutX((clientSideConnection.getWidth() * gameView.getScale()) / 2 + gameView.getMarginX());
        compteur.setLayoutY(clientSideConnection.getHeight() / 2);

        // Adds the counter to the pane
        gameplay.getChildren().addAll(compteur);

        // Creates a new ScaleTransition that will increase the scale size of the
        // timer's text
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), timer);

        st.setByX(10f);
        st.setByY(10f);
        timer.setFont(Font.font(timer.getFont().getFamily(), 15));

        // At the end of the animation, if the timer > 1, the timer is launched again
        // with duration minus one
        // Else the gameplay is animated
        st.setOnFinished(e -> {
            gameplay.getChildren().remove(compteur);
            inReset = false;

            if (duration > 1) {
                launchTimer(gameplay, court, gameView, duration - 1, clientSideConnection, endfunc);
            } else {
                endfunc.end();
            }
        });

        // Launch the animation
        st.play();

        if (duration <= 1) {
            clientSideConnection.sendReady();
            System.out.println("Ready sent");
        }
    }
}
