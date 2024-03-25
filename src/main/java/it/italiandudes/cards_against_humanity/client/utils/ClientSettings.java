package it.italiandudes.cards_against_humanity.client.utils;

import it.italiandudes.cards_against_humanity.utils.Defs;
import it.italiandudes.cards_against_humanity.utils.JSONManager;
import it.italiandudes.idl.common.JarHandler;
import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public final class ClientSettings {

    // Settings
    private static JSONObject SETTINGS = null;

    // Settings Loader
    public static void loadSettingsFile() {
        File settingsFile = new File(Defs.Resources.JSON.JSON_CLIENT_SETTINGS);
        if (!settingsFile.exists() || !settingsFile.isFile()) {
            try {
                JarHandler.copyFileFromJar(new File(Defs.JAR_POSITION), Defs.Resources.JSON.DEFAULT_JSON_CLIENT_SETTINGS, settingsFile, true);
            } catch (IOException e) {
                Logger.log(e);
                return;
            }
        }
        try {
            SETTINGS = JSONManager.readJSON(settingsFile);
            fixJSONSettings();
        } catch (IOException | JSONException e) {
            Logger.log(e);
        }
    }

    // Settings Checker
    private static void fixJSONSettings() throws JSONException, IOException {
        try {
            SETTINGS.getBoolean(SettingsKeys.ENABLE_DARK_MODE);
        } catch (JSONException e) {
            SETTINGS.remove(SettingsKeys.ENABLE_DARK_MODE);
            SETTINGS.put(SettingsKeys.ENABLE_DARK_MODE, true);
        }
        try {
            SETTINGS.getBoolean(SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE);
        } catch (JSONException e) {
            SETTINGS.remove(SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE);
            SETTINGS.put(SettingsKeys.ENABLE_DISCORD_RICH_PRESENCE, true);
        }
        writeJSONSettings();
    }

    // Settings Writer
    public static void writeJSONSettings() throws IOException {
        JSONManager.writeJSON(SETTINGS, new File(Defs.Resources.JSON.JSON_CLIENT_SETTINGS));
    }

    // Settings Getter
    @NotNull
    public static JSONObject getSettings() {
        return SETTINGS;
    }

    // JSON Settings
    public static final class SettingsKeys {
        public static final String ENABLE_DARK_MODE = "enableDarkMode";
        public static final String ENABLE_DISCORD_RICH_PRESENCE = "enableDiscordRichPresence";
    }
}
