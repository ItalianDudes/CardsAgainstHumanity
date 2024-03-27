package it.italiandudes.cards_against_humanity.server.exceptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class GameAlreadyRunningException extends IOException {
    // Constructors
    public GameAlreadyRunningException(@NotNull final String message) {
        super(message);
    }
    public GameAlreadyRunningException(@NotNull final String message, @NotNull final Throwable cause) {
        super(message, cause);
    }
}
