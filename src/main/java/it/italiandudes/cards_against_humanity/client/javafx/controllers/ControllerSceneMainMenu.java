package it.italiandudes.cards_against_humanity.client.javafx.controllers;

import it.italiandudes.cards_against_humanity.client.javafx.Client;
import it.italiandudes.cards_against_humanity.client.javafx.scenes.SceneSettingsEditor;
import it.italiandudes.cards_against_humanity.client.utils.DiscordRichPresenceManager;
import javafx.fxml.FXML;

public final class ControllerSceneMainMenu {

    // Initialize
    @FXML
    private void initialize() {
        DiscordRichPresenceManager.updateRichPresenceState(DiscordRichPresenceManager.States.MENU);
    }

    // EDT
    @FXML
    private void openSettingsEditor() {
        Client.setScene(SceneSettingsEditor.getScene().getParent());
    }
}