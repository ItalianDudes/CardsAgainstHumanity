package it.italiandudes.cards_against_humanity.server.connection;

import it.italiandudes.cards_against_humanity.protocol.MessageExchanger;
import it.italiandudes.cards_against_humanity.protocol.client.MessageClientAuthenticate;
import it.italiandudes.cards_against_humanity.protocol.server.MessageServerUsernameTaken;
import it.italiandudes.cards_against_humanity.protocol.server.MessageServerWrongPassword;
import it.italiandudes.cards_against_humanity.server.data.UserConnection;
import it.italiandudes.cards_against_humanity.server.utils.ServerSettings;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.Socket;
import java.util.Objects;

public final class Authenticator extends Thread {

    // Attributes
    @NotNull private final Socket connection;

    // Constructors
    public Authenticator(@NotNull final Socket connection) {
        setDaemon(true);
        this.connection = connection;
    }

    // Runnable
    @Override
    public void run() {
        try {
            JSONObject object = MessageExchanger.receiveServerMessage(connection);
            MessageClientAuthenticate message = new MessageClientAuthenticate(object);
            UserConnection establishedConnection = new UserConnection(message.getUsername(), connection);
            if (!Objects.equals(message.getPassword(), ServerSettings.getSettings().getString(ServerSettings.SettingsKeys.PASSWORD))) {
                MessageExchanger.sendServerMessage(establishedConnection, new MessageServerWrongPassword());
                ConnectionManager.getInstance().closeAndRemoveOpenConnection(connection);
                return;
            }
            if (!ConnectionManager.getInstance().addEstablishedConnection(establishedConnection)) {
                MessageExchanger.sendServerMessage(establishedConnection, new MessageServerUsernameTaken());
                ConnectionManager.getInstance().closeAndRemoveOpenConnection(connection);
            }
        } catch (Exception e) {
            ConnectionManager.getInstance().closeAndRemoveOpenConnection(connection);
        }
    }
}
