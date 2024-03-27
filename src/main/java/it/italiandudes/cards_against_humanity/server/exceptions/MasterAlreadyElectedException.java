package it.italiandudes.cards_against_humanity.server.exceptions;

import org.jetbrains.annotations.NotNull;

public final class MasterAlreadyElectedException extends Exception {
    // Constructors
    public MasterAlreadyElectedException(@NotNull final String message) {
        super(message);
    }
    public MasterAlreadyElectedException(@NotNull final String message, @NotNull final Throwable cause) {
        super(message, cause);
    }
}
