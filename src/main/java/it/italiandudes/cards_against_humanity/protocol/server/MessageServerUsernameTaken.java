package it.italiandudes.cards_against_humanity.protocol.server;

import org.json.JSONObject;

public class MessageServerUsernameTaken extends ServerMessage {
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("protocol", ServerMessageProtocol.USERNAME_TAKEN);
        return object.toString();
    }
}
