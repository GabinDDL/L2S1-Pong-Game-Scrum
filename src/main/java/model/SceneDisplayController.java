package model;

    /**
     * Creates a type which can hold up to 4 different states :
     * MENU, GAME, SETTINGS, PAUSE. These states define the set of all
     * possible scenes that can be displayed. To generate an instance of it you do
     * " SceneDisplay <NameOfTheState> = new SceneDisplay.<DesiredState> "
     * And to modify said state you do:
     * " <NameOfTheState> = SceneDisplayController.SceneDisplay.<newState> "
     */
public interface SceneDisplayController {

    enum SceneDisplay { MENU, GAME, SETTINGS, PAUSE };

    public boolean isInGame();
    public void pauseUnpause();
    public void setScene(SceneDisplay sD);
}
