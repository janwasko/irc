package com.jasiek.irc.server;

import com.jasiek.irc.common.network.Decoder;
import com.jasiek.irc.common.network.Encoder;
import com.jasiek.irc.common.network.IDecoder;
import com.jasiek.irc.common.network.IEncoder;
import com.jasiek.irc.common.network.message.MessageDecoder;
import com.jasiek.irc.common.network.message.MessageEncoder;
import com.jasiek.irc.common.network.message.messages.Message;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class User implements Runnable {

    private Server server;

    private Socket socket;

    private IDecoder decoder;

    private IEncoder encoder;

    private boolean isClosed;

    private String nick;

    public User(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.decoder = new Decoder(socket);
        this.encoder = new Encoder(socket);
    }

    public void run() {
        while (true) {
            byte[] data;
            try {
                data = decoder.receive();
            } catch (EOFException e) {
                server.onUserDisconnected(this);
                cleanup();
                return;
            } catch (IOException e) {
                e.printStackTrace();
                cleanup();
                return;
            }
            handleReceivedData(data);
        }
    }

    private void cleanup() {
        isClosed = true;
        try {
            socket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void handleReceivedData(byte[] data) {
        //System.out.println(new String(data));
        MessageDecoder messageDecoder = new MessageDecoder();
        Message message = messageDecoder.decode(data);
        if (message != null) {
            server.onMessageReceived(this, message);
        }
    }

    public boolean isClosed() {
        return isClosed;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void sendMessage(Message message) throws IOException {
        MessageEncoder messageEncoder = new MessageEncoder();
        byte[] data = messageEncoder.encode(message);
        encoder.send(data);
    }
}
