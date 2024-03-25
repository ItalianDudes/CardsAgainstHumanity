package it.italiandudes.cards_against_humanity.exceptions;

import org.jetbrains.annotations.NotNull;

public final class ProtocolException extends Exception {

    // Constructors
    public ProtocolException(@NotNull final String message) {
        super(message);
    }
    public ProtocolException(@NotNull final String message, @NotNull final Throwable cause) {
        super(message, cause);
    }
}
