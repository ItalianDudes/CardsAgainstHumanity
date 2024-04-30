package it.italiandudes.cards_against_humanity.protocol.server;

import it.italiandudes.cards_against_humanity.exceptions.ProtocolException;
import it.italiandudes.cards_against_humanity.server.data.BlackCard;
import it.italiandudes.cards_against_humanity.server.data.WhiteCard;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class MessageServerCardsUpdate extends ServerMessage {

    // Attributes
    @NotNull private final BlackCard blackCard;
    @NotNull private final ArrayList<@NotNull WhiteCard> newWhiteCards;

    // Constructors
    public MessageServerCardsUpdate(@NotNull final BlackCard blackCard, @NotNull final ArrayList<@NotNull WhiteCard> newWhiteCards) {
        this.blackCard = blackCard;
        this.newWhiteCards = newWhiteCards;
    }
    public MessageServerCardsUpdate(@NotNull final JSONObject json) throws ProtocolException {
        try {
            if (!json.getString("protocol").equals(ServerMessageProtocol.CARDS_UPDATE.name())) {
                throw new ProtocolException("Expected protocol: " + ServerMessageProtocol.CARDS_UPDATE.name() + ", received " + json.getString("protocol"));
            }
            this.blackCard = new BlackCard(json.getJSONObject("black_card"));
            JSONArray array = json.getJSONArray("new_white_cards");
            this.newWhiteCards = new ArrayList<>();
            for (int i=0; i<array.length(); i++) {
                newWhiteCards.add(new WhiteCard(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            throw new ProtocolException("Error in JSON", e);
        }
    }

    // Methods
    @NotNull
    public BlackCard getBlackCard() {
        return blackCard;
    }
    @NotNull
    public ArrayList<@NotNull WhiteCard> getNewWhiteCards() {
        return newWhiteCards;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageServerCardsUpdate)) return false;

        MessageServerCardsUpdate that = (MessageServerCardsUpdate) o;

        if (!getBlackCard().equals(that.getBlackCard())) return false;
        return getNewWhiteCards().equals(that.getNewWhiteCards());
    }
    @Override
    public int hashCode() {
        int result = getBlackCard().hashCode();
        result = 31 * result + getNewWhiteCards().hashCode();
        return result;
    }
    @Override @NotNull
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("protocol", ServerMessageProtocol.CARDS_UPDATE.name());
        object.put("black_card", blackCard.toJSON());
        JSONArray whiteCardsArray = new JSONArray();
        for (WhiteCard whiteCard : newWhiteCards) {
            whiteCardsArray.put(whiteCard.toJSON());
        }
        object.put("new_white_cards", whiteCardsArray);
        return object.toString();
    }
}
