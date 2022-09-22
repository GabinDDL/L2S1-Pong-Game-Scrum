package model;

public interface RacketController {
    /*
     * Creates a type wich can hold up to 3 different states :
     * GOING_UP, IDLE, GOING_DOWN. These states define the set of all
     * possible states of the Racket. To generate an instace of it you do
     * " State <NameOfTheState> = new State.<DesiredState> "
     * And to modify said state you do:
     * " <NameOfTheState> = RacketController.State.<newState> "
     */
    enum State { GOING_UP, IDLE, GOING_DOWN }


    // State getter (Overided on ../gui/App.java)
    State getState();
}
