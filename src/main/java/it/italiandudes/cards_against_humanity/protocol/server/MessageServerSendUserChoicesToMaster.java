package it.italiandudes.cards_against_humanity.protocol.server;

import it.italiandudes.cards_against_humanity.exceptions.ProtocolException;
import it.italiandudes.cards_against_humanity.server.data.WhiteCard;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageServerSendUserChoicesToMaster extends ServerMessage {

    // Attributes
    @NotNull private final ArrayList<@NotNull ArrayList<@NotNull WhiteCard>> usersChoices;

    // Constructors
    public MessageServerSendUserChoicesToMaster(@NotNull final HashMap<@NotNull String, @NotNull ArrayList<@NotNull WhiteCard>> usersChoices) {
        this.usersChoices = new ArrayList<>();
        this.usersChoices.addAll(usersChoices.values());
    }
    public MessageServerSendUserChoicesToMaster(@NotNull final JSONObject json) throws ProtocolException {
        try {
            if (!json.getString("protocol").equals(ServerMessageProtocol.SEND_USER_CHOICES_TO_MASTER.name())) {
                throw new ProtocolException("Expected protocol: " + ServerMessageProtocol.SEND_USER_CHOICES_TO_MASTER.name() + ", received " + json.getString("protocol"));
            }
            this.usersChoices = new ArrayList<>();
            JSONArray usersChoices = json.getJSONArray("users_choices");
            for (int i=0; i<usersChoices.length(); i++) {
                ArrayList<WhiteCard> choices = new ArrayList<>();
                JSONArray userChoices = usersChoices.getJSONArray(i);
                for (int j=0; j<userChoices.length(); j++) {
                    choices.add(new WhiteCard(userChoices.getJSONObject(j)));
                }
                this.usersChoices.add(choices);
            }
        } catch (JSONException e) {
            throw new ProtocolException("Error in JSON", e);
        }
    }

    // Methods
    @NotNull
    public ArrayList<@NotNull ArrayList<@NotNull WhiteCard>> getUsersChoices() {
        return usersChoices;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageServerSendUserChoicesToMaster)) return false;

        MessageServerSendUserChoicesToMaster that = (MessageServerSendUserChoicesToMaster) o;

        return getUsersChoices().equals(that.getUsersChoices());
    }
    @Override
    public int hashCode() {
        return getUsersChoices().hashCode();
    }

    @Override @NotNull
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("protocol", ServerMessageProtocol.SEND_USER_CHOICES_TO_MASTER);
        JSONArray usersChoices = new JSONArray();
        for (@NotNull ArrayList<WhiteCard> userChoicesList : this.usersChoices) {
            JSONArray userChoicesArray = new JSONArray();
            for (WhiteCard whiteCard : userChoicesList) {
                userChoicesArray.put(whiteCard.toJSON());
            }
            usersChoices.put(userChoicesArray);
        }
        object.put("users_choices", usersChoices);
        return object.toString();
    }
}
