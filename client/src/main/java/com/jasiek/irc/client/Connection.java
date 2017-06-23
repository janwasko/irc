package com.jasiek.irc.client;

import com.jasiek.irc.common.network.Decoder;
import com.jasiek.irc.common.network.Encoder;
import com.jasiek.irc.common.network.IDecoder;
import com.jasiek.irc.common.network.IEncoder;
import com.jasiek.irc.common.network.message.MessageDecoder;
import com.jasiek.irc.common.network.message.MessageEncoder;
import com.jasiek.irc.common.network.message.messages.Message;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Connection {

    private Socket socket;

    private IEncoder encoder;

    private IDecoder decoder;

    public Connection(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
        this.encoder = new Encoder(socket);
        this.decoder = new Decoder(socket);
    }

    public void send(Message message) throws IOException {
        MessageEncoder messageEncoder = new MessageEncoder();
        byte[] data = messageEncoder.encode(message);
        encoder.send(data);
    }

    public Message receive() throws SocketException {
        byte[] data;
        try {
            data = decoder.receive();
            MessageDecoder messageDecoder = new MessageDecoder();
            Message message = messageDecoder.decode(data);
            if (message != null) {
                return message;
            }
        } catch (SocketException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
