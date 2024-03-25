package it.italiandudes.cards_against_humanity.server.connection;

import it.italiandudes.cards_against_humanity.server.data.UserConnection;
import org.jetbrains.annotations.NotNull;

public class ClientListener extends Thread {

    // Attributes
    @NotNull private final UserConnection connection;

    // Constructors
    public ClientListener(@NotNull final UserConnection connection) {
        setDaemon(true);
        this.connection = connection;
    }

    // Methods
    @NotNull
    public UserConnection getConnection() {
        return connection;
    }

    // Runnable
    @Override
    public void run() {
        // TODO: Implement me
    }
}
