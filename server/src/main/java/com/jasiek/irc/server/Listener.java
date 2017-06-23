package com.jasiek.irc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Listener {

    private final Server server;

    private final ServerSocket serverSocket;

    public Listener(Server server, int port) throws IOException {
        this.server = server;
        this.serverSocket = new ServerSocket(port);
    }

    public User accept() throws IOException {
        Socket socket = serverSocket.accept();
        return new User(server, socket);
    }

    public void close() throws IOException {
        serverSocket.close();
    }
}
