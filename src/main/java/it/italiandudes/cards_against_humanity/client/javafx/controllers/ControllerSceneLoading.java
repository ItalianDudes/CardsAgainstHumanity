package it.italiandudes.cards_against_humanity.client.javafx.controllers;

import it.italiandudes.cards_against_humanity.client.javafx.Client;
import javafx.fxml.FXML;

public final class ControllerSceneLoading {

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(true);
    }
}