package it.italiandudes.cards_against_humanity.server.connection;

import it.italiandudes.cards_against_humanity.server.data.UserConnection;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;
import java.util.ArrayList;

public final class ConnectionManager {

    // Attributes
    @NotNull private final ArrayList<@NotNull Socket> OPEN_CONNECTION = new ArrayList<>();
    @NotNull private final ArrayList<@NotNull UserConnection> ESTABLISHED_CONNECTIONS = new ArrayList<>();

    public void closeConnections() {
        for (Socket socket : OPEN_CONNECTION) {
            try {
                socket.close();
                OPEN_CONNECTION.remove(socket);
            } catch (Exception ignored) {}
        }
        for (UserConnection connection : ESTABLISHED_CONNECTIONS) {
            try {
                connection.getConnection().close();
                ESTABLISHED_CONNECTIONS.remove(connection);
            } catch (Exception ignored) {}
        }
    }
    public void addOpenConnection(@NotNull final Socket socket) {
        OPEN_CONNECTION.add(socket);
    }
    public void closeAndRemoveOpenConnection(@NotNull final Socket socket) {
        OPEN_CONNECTION.remove(socket);
        try {
            socket.close();
        } catch (Exception ignored) {}
    }
    public boolean addEstablishedConnection(@NotNull final UserConnection connection) {
        if (isConnected(connection)) return false;
        OPEN_CONNECTION.remove(connection.getConnection());
        new ClientListener(connection).start();
        return ESTABLISHED_CONNECTIONS.add(connection);
    }
    @NotNull
    public ArrayList<@NotNull UserConnection> getEstablishedConnections() {
        return ESTABLISHED_CONNECTIONS;
    }
    public void closeAndRemoveEstablishedConnection(@NotNull final UserConnection connection) {
        ESTABLISHED_CONNECTIONS.remove(connection);
        try {
            connection.getConnection().close();
        } catch (Exception ignored) {}
    }
    public boolean isConnected(@NotNull UserConnection connection) {
        return ESTABLISHED_CONNECTIONS.contains(connection);
    }

    // Instance
    private static ConnectionManager INSTANCE = null;
    @NotNull
    public static ConnectionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionManager();
        }
        return INSTANCE;
    }
}
