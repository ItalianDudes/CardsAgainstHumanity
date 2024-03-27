package it.italiandudes.cards_against_humanity.server.data;

import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public final class UserConnection {

    // Attributes
    @NotNull private final String username;
    @NotNull private final Socket connection;

    // Constructors
    public UserConnection(@NotNull final String username, @NotNull final Socket connection) {
        this.username = username;
        this.connection = connection;
    }
    public UserConnection(@NotNull final UserConnection userConnection) {
        this.username = userConnection.username;
        this.connection = userConnection.connection;
    }

    // Methods
    @NotNull
    public String getUsername() {
        return username;
    }
    @NotNull
    public Socket getConnection() {
        return connection;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserConnection)) return false;

        UserConnection that = (UserConnection) o;

        return getUsername().equals(that.getUsername());
    }
    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }
    @Override @NotNull
    public String toString() {
        return username;
    }
}
