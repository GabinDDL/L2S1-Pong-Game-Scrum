package model.interfaces;

import gui.interfaces.InterfaceRacketGui;

import model.interfaces.InterfaceRacketController.State;

public interface InterfaceRacket extends InterfaceRacketGui {

    void update(double deltaT, double height, State state);

    void reset(double height);

}
