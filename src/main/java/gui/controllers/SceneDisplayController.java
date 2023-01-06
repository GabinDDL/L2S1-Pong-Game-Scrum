package gui.controllers;

/**
 * Creates a type which can hold up to 4 different states :
 * MENU, GAME, SETTINGS, PAUSE. These states define the set of all
 * possible scenes that can be displayed. To generate an instance of it you do
 * " SceneDisplay <NameOfTheState> = new SceneDisplay.<DesiredState> "
 * And to modify said state you do:
 * " <NameOfTheState> = SceneDisplayController.SceneDisplay.<newState> "
 */
public interface SceneDisplayController {

    enum SceneDisplay {
        GAME, PAUSE
    };

    /**
     * Returns true if the game is running, false if the game is paused
     */
    public boolean isInGame();

    /**
     * Returns true if the game is on pause, false if not
     */
    public boolean isOnPause();

    /**
     * Alterns between GAME and PAUSE
     */
    public void pauseUnpause();

    public void setScene(SceneDisplay sD);
}
