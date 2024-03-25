package it.italiandudes.cards_against_humanity.server;

import it.italiandudes.cards_against_humanity.server.connection.ConnectionManager;
import it.italiandudes.cards_against_humanity.server.connection.ServerListener;
import it.italiandudes.cards_against_humanity.server.utils.DBManager;
import it.italiandudes.cards_against_humanity.server.utils.ServerSettings;
import it.italiandudes.idl.common.Logger;

import java.io.File;

public final class Server {

    // Attributes
    private static ServerListener serverListener = null;

    // Entry Point
    public static void start() {
        loadSettings();
        loadDB();
        loadServerSocket();
        // TODO: keyboard reader for console commands
    }

    // Init server
    public static void shutdownServer() {
        if (serverListener != null) {
            serverListener.closeServerSocket();
            serverListener = null;
        }
        ConnectionManager.getInstance().closeConnections();
        DBManager.closeConnection();
        Logger.close();
        System.exit(0);
    }
    public static void showErrorAndShutdownServer(Throwable e) {
        Logger.log(e);
        shutdownServer();
    }
    private static void loadSettings() {
        try {
            ServerSettings.loadSettingsFile();
            Logger.log("Server Configuration Loaded");
        } catch (Exception e) {
            Logger.log(e);
            shutdownServer();
        }
    }
    private static void loadDB() {
        try {
            String dbPath = ServerSettings.getSettings().getString(ServerSettings.SettingsKeys.DB_PATH);
            File dbPointer = new File(dbPath);
            if (dbPointer.exists() && dbPointer.isFile()) {
                DBManager.connectToDB(dbPointer);
            } else {
                DBManager.createDB(dbPath);
            }
            Logger.log("Database Loaded");
        } catch (Exception e) {
            Logger.log(e);
            shutdownServer();
        }
    }
    private static void loadServerSocket() {
        try {
            serverListener = new ServerListener(ServerSettings.getSettings().getInt(ServerSettings.SettingsKeys.SERVER_PORT));
            serverListener.start();
        } catch (Exception e) {
            Logger.log(e);
            shutdownServer();
        }
    }
}
