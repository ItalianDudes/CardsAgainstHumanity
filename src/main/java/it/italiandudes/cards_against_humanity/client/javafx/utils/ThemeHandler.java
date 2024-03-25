package it.italiandudes.cards_against_humanity.client.javafx.utils;

import it.italiandudes.cards_against_humanity.client.utils.ClientSettings;
import it.italiandudes.cards_against_humanity.utils.Defs;
import javafx.scene.Parent;
import org.jetbrains.annotations.NotNull;

public final class ThemeHandler {

    // Config Theme
    private static String configTheme = null;

    // Methods
    public static void setConfigTheme() {
        if (ClientSettings.getSettings().getBoolean(Defs.SettingsKeys.ENABLE_DARK_MODE)) {
            configTheme = Defs.Resources.get(JFXDefs.Resources.CSS.CSS_DARK_THEME).toExternalForm();
        } else {
            configTheme = Defs.Resources.get(JFXDefs.Resources.CSS.CSS_LIGHT_THEME).toExternalForm();
        }
    }

    // Config Theme
    public static void loadConfigTheme(@NotNull final Parent scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(configTheme);
    }

    // Light Theme
    public static void loadLightTheme(@NotNull final Parent scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Defs.Resources.get(JFXDefs.Resources.CSS.CSS_LIGHT_THEME).toExternalForm());
    }

    // Dark Theme
    public static void loadDarkTheme(@NotNull final Parent scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Defs.Resources.get(JFXDefs.Resources.CSS.CSS_DARK_THEME).toExternalForm());
    }
}