package com.jasiek.irc.common.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Decoder implements IDecoder {

    private DataInputStream dataInputStream;

    public Decoder(Socket socket) throws IOException {
        dataInputStream = new DataInputStream(socket.getInputStream());
    }

    public byte[] receive() throws IOException {
        int size = dataInputStream.readInt();
        byte[] data = new byte[size];
        dataInputStream.readFully(data);
        return data;
    }
}
