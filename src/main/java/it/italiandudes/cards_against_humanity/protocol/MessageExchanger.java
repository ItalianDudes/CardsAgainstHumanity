package it.italiandudes.cards_against_humanity.protocol;

import it.italiandudes.cards_against_humanity.exceptions.ProtocolException;
import it.italiandudes.cards_against_humanity.protocol.client.ClientMessage;
import it.italiandudes.cards_against_humanity.protocol.server.ServerMessage;
import it.italiandudes.cards_against_humanity.server.data.UserConnection;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("DuplicatedCode")
public final class MessageExchanger {
    public static void sendServerMessage(@NotNull final UserConnection connection, @NotNull final ServerMessage message) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(connection.getConnection().getOutputStream());
        String json = message.toString();
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        outputStream.writeInt(bytes.length);
        if (bytes.length == 0) return;
        outputStream.write(bytes, 0, bytes.length);
        outputStream.flush();
    }
    public static void sendClientMessage(@NotNull final Socket connection, @NotNull final ClientMessage message) throws IOException {
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        String json = message.toString();
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        outputStream.writeInt(bytes.length);
        if (bytes.length == 0) return;
        outputStream.write(bytes, 0, bytes.length);
        outputStream.flush();
    }
    @NotNull
    public static JSONObject receiveServerMessage(@NotNull final Socket connection) throws IOException, ProtocolException {
        DataInputStream inputStream = new DataInputStream(connection.getInputStream());
        int length = inputStream.readInt();
        if (length == 0) return new JSONObject();
        byte[] bytes = new byte[length];
        int bytesRead = inputStream.read(bytes, 0, length);
        if (bytesRead != length) throw new IOException("Expected bytes: " + length + ", received " + bytesRead);
        String json = new String(bytes, StandardCharsets.UTF_8);
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            throw new ProtocolException("Invalid JSON", e);
        }
    }
    @NotNull
    public static JSONObject receiveClientMessage(@NotNull final UserConnection connection) throws IOException, ProtocolException {
        DataInputStream inputStream = new DataInputStream(connection.getConnection().getInputStream());
        int length = inputStream.readInt();
        if (length == 0) return new JSONObject();
        byte[] bytes = new byte[length];
        int bytesRead = inputStream.read(bytes, 0, length);
        if (bytesRead != length) throw new IOException("Expected bytes: " + length + ", received " + bytesRead);
        String json = new String(bytes, StandardCharsets.UTF_8);
        try {
            JSONObject object = new JSONObject(json);
            if (!object.has("protocol") || object.getString("protocol") == null) {
                throw new ProtocolException("Missing protocol descriptor");
            }
            return object;
        } catch (JSONException e) {
            throw new ProtocolException("Invalid JSON", e);
        }
    }
}
