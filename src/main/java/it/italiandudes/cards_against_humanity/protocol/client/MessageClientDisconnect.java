package it.italiandudes.cards_against_humanity.protocol.client;

import org.json.JSONObject;

public class MessageClientDisconnect extends ClientMessage {
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("protocol", ClientMessageProtocol.DISCONNECT);
        return object.toString();
    }
}
