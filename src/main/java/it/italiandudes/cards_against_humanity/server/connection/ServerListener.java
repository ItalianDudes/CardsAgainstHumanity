package it.italiandudes.cards_against_humanity.server.connection;

import it.italiandudes.idl.common.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread {

    // Attributes
    private final ServerSocket serverSocket;

    // Constructors
    public ServerListener(final int port) throws IOException, IllegalArgumentException {
        setDaemon(true);
        serverSocket = new ServerSocket(port);
    }

    // Methods
    public void closeServerSocket() {
        try {
            serverSocket.close();
        } catch (Exception ignored) {}
    }

    // Runnable
    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            try {
                Socket incomingConnection = serverSocket.accept();
                ConnectionManager.getInstance().addOpenConnection(incomingConnection);
                new Authenticator(incomingConnection).start();
            } catch (IOException e) {
                if (serverSocket.isClosed()) return;
                Logger.log(e);
            }
        }
        Logger.log("Server Socket Closed");
    }
}
