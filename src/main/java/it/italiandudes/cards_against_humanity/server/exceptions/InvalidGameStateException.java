package it.italiandudes.cards_against_humanity.server.exceptions;

import org.jetbrains.annotations.NotNull;

public class InvalidGameStateException extends Exception {
    // Constructors
    public InvalidGameStateException(@NotNull final String message) {
        super(message);
    }
    public InvalidGameStateException(@NotNull final String message, @NotNull final Throwable cause) {
        super(message, cause);
    }
}
