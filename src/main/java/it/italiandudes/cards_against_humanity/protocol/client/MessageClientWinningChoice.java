package it.italiandudes.cards_against_humanity.protocol.client;

import it.italiandudes.cards_against_humanity.exceptions.ProtocolException;
import it.italiandudes.cards_against_humanity.server.data.WhiteCard;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class MessageClientWinningChoice extends ClientMessage {

    // Attributes
    @NotNull private final ArrayList<@NotNull WhiteCard> WINNING_CHOICE;

    // Constructors
    public MessageClientWinningChoice(@NotNull final ArrayList<@NotNull WhiteCard> WINNING_CHOICE) {
        this.WINNING_CHOICE = WINNING_CHOICE;
    }
    public MessageClientWinningChoice(@NotNull final JSONObject json) throws ProtocolException {
        try {
            if (!json.getString("protocol").equals(ClientMessageProtocol.WINNING_CHOICE.name())) {
                throw new ProtocolException("Expected protocol: " + ClientMessageProtocol.WINNING_CHOICE.name() + ", received " + json.getString("protocol"));
            }
            this.WINNING_CHOICE = new ArrayList<>();
            JSONArray winningChoice = json.getJSONArray("winning_choice");
            for (int i=0; i<winningChoice.length(); i++) {
                WINNING_CHOICE.add(new WhiteCard(winningChoice.getJSONObject(i)));
            }
        } catch (JSONException e) {
            throw new ProtocolException("Error in JSON", e);
        }
    }

    // Methods
    @NotNull
    public ArrayList<@NotNull WhiteCard> getWinningChoice() {
        return WINNING_CHOICE;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageClientWinningChoice)) return false;

        MessageClientWinningChoice that = (MessageClientWinningChoice) o;

        return WINNING_CHOICE.equals(that.WINNING_CHOICE);
    }
    @Override
    public int hashCode() {
        return WINNING_CHOICE.hashCode();
    }

    @Override @NotNull
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("protocol", ClientMessageProtocol.WINNING_CHOICE.name());
        JSONArray winningChoice = new JSONArray();
        for (WhiteCard whiteCard : WINNING_CHOICE) {
            winningChoice.put(whiteCard.toJSON());
        }
        json.put("winning_choice", winningChoice);
        return json.toString();
    }
}
