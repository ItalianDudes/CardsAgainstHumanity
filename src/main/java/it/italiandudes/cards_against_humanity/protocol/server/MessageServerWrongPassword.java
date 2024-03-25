package it.italiandudes.cards_against_humanity.protocol.server;

import org.json.JSONObject;

public final class MessageServerWrongPassword extends ServerMessage {
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("protocol", ServerMessageProtocol.WRONG_PASSWORD);
        return object.toString();
    }
}
