package it.italiandudes.cards_against_humanity.exceptions;

import org.jetbrains.annotations.NotNull;

public class UnexptectedStateException extends Exception {
    // Constructors
    public UnexptectedStateException(@NotNull final String message) {
        super(message);
    }
    public UnexptectedStateException(@NotNull final String message, @NotNull final Throwable cause) {
        super(message, cause);
    }
}
