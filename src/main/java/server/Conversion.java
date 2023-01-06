package server;

import model.interfaces.InterfaceBallModel.LastAction;
import model.interfaces.InterfaceRacketController.State;

public class Conversion {

    private Conversion() {
    }

    /**
     * Converts a State to an int
     * <ul>
     * <li>{@code 0} if the state is {@code IDLE}</li>
     * <li>{@code 1} if the state is {@code GOING_UP}</li>
     * <li>{@code 2} if the state is {@code GOING_DOWN}</li>
     * </ul>
     * 
     * @param state {@code State} to convert
     * @return the {@code int} corresponding to the {@code State}
     */
    public static int stateToInt(State state) {
        if (state == null)
            return 0;
        switch (state) {
            case IDLE:
                return 0;
            case GOING_UP:
                return 1;

            case GOING_DOWN:
                return 2;

            default:
                return 0;
        }
    }

    /**
     * Converts a {@code State} to an {@code int}.
     * <ul>
     * <li>{@code IDLE} if {@code 0}</li>
     * <li>{@code GOING_UP} if {@code 1}</li>
     * <li>{@code GOING_DOWN} if {@code 2}</li>
     * </ul>
     * 
     * @param i {@code int} to convert
     * @return the {@code State} corresponding to the {@code int}
     */
    public static State intToState(int i) {
        switch (i) {
            case 0:
                return State.IDLE;
            case 1:
                return State.GOING_UP;

            case 2:
                return State.GOING_DOWN;

            default:
                return State.IDLE;
        }
    }

    /**
     * Converts a {@code LastAction} to an {@code int}.
     * <ul>
     * <li>{@code 0} if {@code NONE}</li>
     * <li>{@code 1} if {@code RACKET_HIT}</li>
     * <li>{@code 2} if {@code WALL_HIT}</li>
     * </ul>
     * 
     * @param lastAction {@code LastAction} to convert
     * @return the {@code int} corresponding to the {@code LastAction}
     */
    public static int lastActionToInt(LastAction lastAction) {
        if (lastAction == null)
            return 0;
        switch (lastAction) {
            case NONE:
                return 0;
            case RACKET_HIT:
                return 1;
            case WALL_HIT:
                return 2;
            default:
                return 0;
        }
    }

    /**
     * Converts an {@code int} to a {@code LastAction}.
     * <ul>
     * <li>{@code NONE} if {@code 0}</li>
     * <li>{@code RACKET_HIT} if {@code 1}</li>
     * <li>{@code WALL_HIT} if {@code 2}</li>
     * </ul>
     * 
     * @param i {@code int} to convert
     * @return the {@code LastAction} corresponding to the {@code int}
     */
    public static LastAction intToLastAction(int i) {
        switch (i) {
            case 0:
                return LastAction.NONE;
            case 1:
                return LastAction.RACKET_HIT;
            case 2:
                return LastAction.WALL_HIT;
            default:
                return LastAction.NONE;
        }
    }
}
