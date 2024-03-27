package it.italiandudes.cards_against_humanity.server.exceptions;

import org.jetbrains.annotations.NotNull;

public final class GameNotRunningException extends Exception {
    // Constructors
    public GameNotRunningException(@NotNull final String message) {
        super(message);
    }
    public GameNotRunningException(@NotNull final String message, @NotNull final Throwable cause) {
        super(message, cause);
    }
}
