package it.italiandudes.cards_against_humanity.server.exceptions;

import org.jetbrains.annotations.NotNull;

public class UserDisconnectedException extends Exception {
    // Constructors
    public UserDisconnectedException(@NotNull final String message) {
        super(message);
    }
    public UserDisconnectedException(@NotNull final String message, @NotNull final Throwable cause) {
        super(message, cause);
    }
}
