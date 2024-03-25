package it.italiandudes.cards_against_humanity.client.javafx.controllers;

import it.italiandudes.cards_against_humanity.client.javafx.Client;
import it.italiandudes.cards_against_humanity.client.javafx.alerts.ErrorAlert;
import it.italiandudes.cards_against_humanity.client.javafx.alerts.InformationAlert;
import it.italiandudes.cards_against_humanity.client.javafx.scenes.SceneMainMenu;
import it.italiandudes.cards_against_humanity.client.javafx.utils.ThemeHandler;
import it.italiandudes.cards_against_humanity.client.utils.ClientSettings;
import it.italiandudes.cards_against_humanity.utils.Defs;
import it.italiandudes.cards_against_humanity.utils.DiscordRichPresenceManager;
import it.italiandudes.idl.common.Logger;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONException;

import java.io.IOException;

public final class ControllerSceneSettingsEditor {

    // Attributes
    private static final Image DARK_MODE = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_DARK_MODE));
    private static final Image LIGHT_MODE = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_LIGHT_MODE));
    private static final Image WUMPUS = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_WUMPUS));
    private static final Image NO_WUMPUS = new Image(Defs.Resources.getAsStream(Defs.Resources.Image.IMAGE_NO_WUMPUS));

    // Graphic Elements
    @FXML private ImageView imageViewEnableDarkMode;
    @FXML private ToggleButton toggleButtonEnableDarkMode;
    @FXML private ImageView imageViewEnableDiscordRichPresence;
    @FXML private ToggleButton toggleButtonEnableDiscordRichPresence;

    // Initialize
    @FXML
    private void initialize() {
        toggleButtonEnableDarkMode.setSelected(ClientSettings.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_DARK_MODE));
        toggleButtonEnableDiscordRichPresence.setSelected(ClientSettings.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE));
        if (toggleButtonEnableDarkMode.isSelected()) imageViewEnableDarkMode.setImage(DARK_MODE);
        else imageViewEnableDarkMode.setImage(LIGHT_MODE);
        if (toggleButtonEnableDiscordRichPresence.isSelected()) imageViewEnableDiscordRichPresence.setImage(WUMPUS);
        else imageViewEnableDiscordRichPresence.setImage(NO_WUMPUS);
    }

    // EDT
    @FXML
    private void toggleEnableDarkMode() {
        if (toggleButtonEnableDarkMode.isSelected()) {
            imageViewEnableDarkMode.setImage(DARK_MODE);
            ThemeHandler.loadDarkTheme(Client.getScene());
        } else {
            imageViewEnableDarkMode.setImage(LIGHT_MODE);
            ThemeHandler.loadLightTheme(Client.getScene());
        }
    }
    @FXML
    private void toggleEnableDiscordRichPresence() {
        if (toggleButtonEnableDiscordRichPresence.isSelected()) imageViewEnableDiscordRichPresence.setImage(WUMPUS);
        else imageViewEnableDiscordRichPresence.setImage(NO_WUMPUS);
    }
    @FXML
    private void backToMenu() {
        Client.setScene(SceneMainMenu.getScene().getParent());
    }
    @FXML
    private void save() {
        new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        try {
                            ClientSettings.getSettings().put(Defs.SettingsKeys.ENABLE_DARK_MODE, toggleButtonEnableDarkMode.isSelected());
                            ClientSettings.getSettings().put(Defs.SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE, toggleButtonEnableDiscordRichPresence.isSelected());
                        } catch (JSONException e) {
                            Logger.log(e);
                        }
                        ThemeHandler.setConfigTheme();
                        if (!toggleButtonEnableDiscordRichPresence.isSelected()) {
                            DiscordRichPresenceManager.shutdownRichPresence();
                        }
                        try {
                            ClientSettings.writeJSONSettings();
                            Platform.runLater(() -> new InformationAlert("SUCCESSO", "Salvataggio Impostazioni", "Impostazioni salvate e applicate con successo!"));
                        } catch (IOException e) {
                            Logger.log(e);
                            Platform.runLater(() -> new ErrorAlert("ERRORE", "Errore di I/O", "Si e' verificato un errore durante il salvataggio delle impostazioni."));
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
}