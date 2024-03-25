package it.italiandudes.cards_against_humanity.client.javafx.scenes;

import it.italiandudes.cards_against_humanity.client.javafx.components.SceneController;
import it.italiandudes.cards_against_humanity.client.javafx.controllers.ControllerSceneMainMenu;
import it.italiandudes.cards_against_humanity.client.javafx.utils.JFXDefs;
import it.italiandudes.cards_against_humanity.client.javafx.utils.ThemeHandler;
import it.italiandudes.cards_against_humanity.utils.Defs;
import it.italiandudes.idl.common.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class SceneMainMenu {

    // Scene Generator
    @NotNull
    public static SceneController getScene() {
        try {
            FXMLLoader loader = new FXMLLoader(Defs.Resources.get(JFXDefs.Resources.FXML.FXML_MAIN_MENU));
            Parent root = loader.load();
            ThemeHandler.loadConfigTheme(root);
            ControllerSceneMainMenu controller = loader.getController();
            return new SceneController(root, controller);
        } catch (IOException e) {
            Logger.log(e);
            System.exit(-1);
            return null;
        }
    }
}
