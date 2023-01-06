package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 * Class which allow to play the main game
 */
public class HostPongController implements Initializable {

    // Elements created and described in classicPongMenu.fxml
    @FXML
    BorderPane hostPongMenuContainer;
    @FXML
    Text title;
    @FXML
    Label serverIp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //
    }

    /**
     * Load the animation translating to the Servers's Pane (up)
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    public void loadServerPage(MouseEvent event) throws IOException {
        ServerPongController.gameServerThread.interrupt();
        ServerPongController.gameServer.closeServer();
        TransitionClass.transition(false, false, "server.fxml", Interpolator.EASE_IN,
                hostPongMenuContainer, null);
    }

    /**
     * Displays the ip.
     */
    @FXML
    public void displayIp() {
        serverIp.setText(ServerPongController.gameServer.getIp());
    }

}
