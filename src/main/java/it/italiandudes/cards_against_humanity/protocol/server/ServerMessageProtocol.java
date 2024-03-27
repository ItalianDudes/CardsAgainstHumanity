package it.italiandudes.cards_against_humanity.protocol.server;

public enum ServerMessageProtocol {
    USERNAME_TAKEN,
    WRONG_PASSWORD,
    CONNECTION_ERROR,
    RUNNING_GAME,
    DISCONNECT,
    CARDS_UPDATE,
    SEND_USER_CHOICES_TO_MASTER
}
