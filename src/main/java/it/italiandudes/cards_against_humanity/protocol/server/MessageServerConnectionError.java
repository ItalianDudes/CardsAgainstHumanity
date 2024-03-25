package it.italiandudes.cards_against_humanity.protocol.server;

import org.json.JSONObject;

public class MessageServerConnectionError extends ServerMessage {
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("protocol", ServerMessageProtocol.CONNECTION_ERROR);
        return object.toString();
    }
}
