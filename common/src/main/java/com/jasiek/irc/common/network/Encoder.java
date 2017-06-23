package com.jasiek.irc.common.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Encoder implements IEncoder {

    private DataOutputStream dataOutputStream;

    public Encoder(Socket socket) throws IOException {
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void send(byte[] data) throws IOException {
        dataOutputStream.writeInt(data.length);
        dataOutputStream.write(data);
    }
}
