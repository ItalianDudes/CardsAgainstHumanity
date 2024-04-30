package it.italiandudes.cards_against_humanity.server.exceptions;

import org.jetbrains.annotations.NotNull;

public class WinnerNotFoundException extends Exception {
    // Constructors
    public WinnerNotFoundException(@NotNull final String message) {
        super(message);
    }
    public WinnerNotFoundException(@NotNull final String message, @NotNull final Throwable cause) {
        super(message, cause);
    }
}
