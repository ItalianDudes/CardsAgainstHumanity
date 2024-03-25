package it.italiandudes.cards_against_humanity.protocol.client;

import it.italiandudes.cards_against_humanity.exceptions.ProtocolException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageClientAuthenticate extends ClientMessage {

    // Attributes
    @NotNull private final String username;
    @Nullable private final String password;

    // Constructors
    public MessageClientAuthenticate(@NotNull final JSONObject json) throws ProtocolException {
        try {
            if (!json.getString("protocol").equals(ClientMessageProtocol.AUTHENTICATE.name())) {
                throw new ProtocolException("Expected protocol: " + ClientMessageProtocol.AUTHENTICATE.name() + ", received " + json.getString("protocol"));
            }
            String username = json.getString("username");
            if (username == null) {
                throw new ProtocolException("Unexpected null value \"username\"");
            }
            this.username = username;
            if (json.has("password") && !json.isNull("password")) {
                this.password = json.getString("password");
            } else {
                this.password = null;
            }
        } catch (JSONException e) {
            throw new ProtocolException("Error in JSON", e);
        }
    }
    public MessageClientAuthenticate(@NotNull final String username, @Nullable final String password) {
        this.username = username;
        this.password = password;
    }

    // Methods
    @NotNull
    public String getUsername() {
        return username;
    }
    @Nullable
    public String getPassword() {
        return password;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageClientAuthenticate)) return false;

        MessageClientAuthenticate that = (MessageClientAuthenticate) o;

        if (!getUsername().equals(that.getUsername())) return false;
        return getPassword() != null ? getPassword().equals(that.getPassword()) : that.getPassword() == null;
    }
    @Override
    public int hashCode() {
        int result = getUsername().hashCode();
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }
    @Override @NotNull
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("protocol", ClientMessageProtocol.AUTHENTICATE);
        object.put("username", username);
        object.put("password", password);
        return object.toString();
    }
}
