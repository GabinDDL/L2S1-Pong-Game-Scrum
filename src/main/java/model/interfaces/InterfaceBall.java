package model.interfaces;

import gui.interfaces.InterfaceBallGui;

import model.game_elements.PlayerModel;
import model.interfaces.InterfaceBallModel.LastAction;

public interface InterfaceBall extends InterfaceBallGui {

    LastAction update(double deltaT, double height, PlayerModel[] players);

    void reset(double width, double height);

}