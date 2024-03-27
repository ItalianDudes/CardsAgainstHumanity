package it.italiandudes.cards_against_humanity.server.connection;

import it.italiandudes.cards_against_humanity.protocol.MessageExchanger;
import it.italiandudes.cards_against_humanity.protocol.client.ClientMessageProtocol;
import it.italiandudes.cards_against_humanity.server.data.UserConnection;
import it.italiandudes.idl.common.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class ClientListener extends Thread {

    // Attributes
    @NotNull private final UserConnection connection;

    // Constructors
    public ClientListener(@NotNull final UserConnection connection) {
        setDaemon(true);
        this.connection = connection;
    }

    // Methods
    @NotNull
    public UserConnection getConnection() {
        return connection;
    }

    // Runnable
    @Override
    public void run() {
        try {
            while (!connection.getConnection().isClosed()) {
                JSONObject object = MessageExchanger.receiveClientMessage(connection);
                ClientMessageProtocol protocol = ClientMessageProtocol.valueOf(object.getString("protocol"));
                switch (protocol) {
                    case AUTHENTICATE: // Handled during connection
                        break;
                    case DISCONNECT:
                        ConnectionManager.getInstance().closeAndRemoveEstablishedConnection(connection);
                        break;
                    case USER_CHOICES:
                        // TODO: implement user_choices
                        break;
                }
            }
        } catch (Exception e) {
            Logger.log(e);
            ConnectionManager.getInstance().closeAndRemoveEstablishedConnection(connection);
        }
    }
}
