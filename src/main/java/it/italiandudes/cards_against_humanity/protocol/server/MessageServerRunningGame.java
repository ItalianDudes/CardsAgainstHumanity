package it.italiandudes.cards_against_humanity.protocol.server;

import org.json.JSONObject;

public class MessageServerRunningGame extends ServerMessage {
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("protocol", ServerMessageProtocol.RUNNING_GAME);
        return object.toString();
    }
}
