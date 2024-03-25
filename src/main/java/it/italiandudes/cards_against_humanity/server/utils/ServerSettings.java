package it.italiandudes.cards_against_humanity.server.utils;

import it.italiandudes.cards_against_humanity.utils.Defs;
import it.italiandudes.cards_against_humanity.utils.JSONManager;
import it.italiandudes.idl.common.JarHandler;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public final class ServerSettings {

    // Settings
    private static JSONObject SETTINGS = null;

    // Settings Loader
    public static void loadSettingsFile() throws IOException {
        File settingsFile = new File(Defs.Resources.JSON.JSON_SERVER_SETTINGS);
        if (!settingsFile.exists() || !settingsFile.isFile()) {
            JarHandler.copyFileFromJar(new File(Defs.JAR_POSITION), Defs.Resources.JSON.DEFAULT_JSON_SERVER_SETTINGS, settingsFile, true);
        }
        try {
            SETTINGS = JSONManager.readJSON(settingsFile);
            fixJSONSettings();
        } catch (JSONException e) {
            throw new IOException("JSONException caught", e);
        }
    }

    // Settings Checker
    private static void fixJSONSettings() throws JSONException, IOException {
        try {
            SETTINGS.getString(SettingsKeys.DB_PATH);
        } catch (JSONException e) {
            SETTINGS.remove(SettingsKeys.DB_PATH);
            SETTINGS.put(SettingsKeys.DB_PATH, "database.cah");
        }
        try {
            SETTINGS.getInt(SettingsKeys.SERVER_PORT);
        } catch (JSONException e) {
            SETTINGS.remove(SettingsKeys.SERVER_PORT);
            SETTINGS.put(SettingsKeys.SERVER_PORT, 45800);
        }
        try {
            SETTINGS.getString(SettingsKeys.PASSWORD);
        } catch (JSONException e) {
            SETTINGS.remove(SettingsKeys.PASSWORD);
            SETTINGS.put(SettingsKeys.PASSWORD, JSONObject.NULL);
        }
        try {
            SETTINGS.getBoolean(SettingsKeys.ALWAYS_RANDOMIZE_MASTER);
        } catch (JSONException e) {
            SETTINGS.remove(SettingsKeys.ALWAYS_RANDOMIZE_MASTER);
            SETTINGS.put(SettingsKeys.ALWAYS_RANDOMIZE_MASTER, false);
        }
        try {
            SETTINGS.getBoolean(SettingsKeys.ALWAYS_RANDOMIZE_WHITE_CARDS);
        } catch (JSONException e) {
            SETTINGS.remove(SettingsKeys.ALWAYS_RANDOMIZE_WHITE_CARDS);
            SETTINGS.put(SettingsKeys.ALWAYS_RANDOMIZE_WHITE_CARDS, false);
        }
        try {
            SETTINGS.getBoolean(SettingsKeys.ALWAYS_RANDOMIZE_BLACK_CARDS);
        } catch (JSONException e) {
            SETTINGS.remove(SettingsKeys.ALWAYS_RANDOMIZE_BLACK_CARDS);
            SETTINGS.put(SettingsKeys.ALWAYS_RANDOMIZE_BLACK_CARDS, false);
        }
        try {
            SETTINGS.getBoolean(SettingsKeys.INFINITE_MODE);
        } catch (JSONException e) {
            SETTINGS.remove(SettingsKeys.INFINITE_MODE);
            SETTINGS.put(SettingsKeys.INFINITE_MODE, false);
        }
        try {
            SETTINGS.getInt(SettingsKeys.GAME_WIN_TARGET);
        } catch (JSONException e) {
            SETTINGS.remove(SettingsKeys.GAME_WIN_TARGET);
            SETTINGS.put(SettingsKeys.GAME_WIN_TARGET, 5);
        }
        try {
            SETTINGS.getInt(SettingsKeys.WHITE_CARDS);
        } catch (JSONException e) {
            SETTINGS.remove(SettingsKeys.WHITE_CARDS);
            SETTINGS.put(SettingsKeys.WHITE_CARDS, 7);
        }
        writeJSONSettings();
    }

    // Settings Writer
    public static void writeJSONSettings() throws IOException {
        JSONManager.writeJSON(SETTINGS, new File(Defs.Resources.JSON.JSON_SERVER_SETTINGS));
    }

    // Settings Getter
    @NotNull
    public static JSONObject getSettings() {
        return SETTINGS;
    }

    // JSON Settings
    public static final class SettingsKeys {
        public static final String DB_PATH = "db_path";
        public static final String SERVER_PORT = "server_port";
        public static final String PASSWORD = "password";
        public static final String ALWAYS_RANDOMIZE_MASTER = "always_randomize_master";
        public static final String ALWAYS_RANDOMIZE_WHITE_CARDS = "always_randomize_white_cards";
        public static final String ALWAYS_RANDOMIZE_BLACK_CARDS = "always_randomize_black_cards";
        public static final String INFINITE_MODE = "infinite_mode";
        public static final String GAME_WIN_TARGET = "game_win_target";
        public static final String WHITE_CARDS = "white_cards";
    }
}
