package com.jasiek.irc.client;

import com.jasiek.irc.common.network.message.messages.*;

import java.io.IOException;
import java.net.SocketException;
import java.security.InvalidParameterException;
import java.util.Arrays;

public class Client {

    private final String host;

    private final int port;

    private Connection connection;

    private final GUI gui;

    public Client(GUI gui, String host, int port) {
        this.gui = gui;
        this.host = host;
        this.port = port;
    }

    public boolean run() {
        try {
            connection = new Connection(host, port);
        } catch (IOException e) {
            return false;
        }

        Thread thread = new Thread(() -> receiveMessages(connection));
        thread.start();
        return true;
    }

    public void handleInput(String text) {
        try {
            Message message = textToMessage(text);
            if (message != null) {
                try {
                    connection.send(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (InvalidParameterException e) {
            gui.handleErrorMessage(new ErrorMessage(e.getMessage()));
        }
    }

    private Message textToMessage(String text) throws InvalidParameterException {
        String[] parts = text.split("\\s+");
        switch (parts[0].toUpperCase()) {
            case "NICK":
                return new SetNick(parts[1]);
            case "JOIN":
                return new JoinChannel(parts[1]);
            case "PART":
                return new PartChannel(parts[1]);
            case "SEND":
                return new ChannelMessage(parts[1], String.join(" ", Arrays.copyOfRange(parts, 2, parts.length)));
            default:
                throw new InvalidParameterException("This command does not exist!");
        }
    }

    private void receiveMessages(Connection connection) {
        while (true) {
            Message msg = null;
            try {
                msg = connection.receive();
            } catch (SocketException e) {
                break;
            }
            if (msg instanceof ConfirmSetNick) {
                ConfirmSetNick confirmSetNick = (ConfirmSetNick) msg;
                if (gui != null) {
                    gui.handleConfirmSetNick(confirmSetNick);
                }
                System.out.println("*** Your nick is " + confirmSetNick.getNick());
            }
            if (msg instanceof ConfirmJoinChannel) {
                ConfirmJoinChannel confirmJoinChannel = (ConfirmJoinChannel) msg;
                if (gui != null) {
                    gui.handleConfirmJoinChannel(confirmJoinChannel);
                }
                //System.out.println("*** Now talking in " + confirmJoinChannel.getChannelName());
                System.out.println("*** " + confirmJoinChannel.getNick() + " has joined the channel " + confirmJoinChannel.getChannelName());
            }
            if (msg instanceof ConfirmPartChannel) {
                ConfirmPartChannel confirmPartChannel = (ConfirmPartChannel) msg;
                if (gui != null) {
                    gui.handleConfirmPartChannel(confirmPartChannel);
                }
                //System.out.println("*** Now leaving " + confirmPartChannel.getChannelName());
                System.out.println("*** " + confirmPartChannel.getNick() + " has left the channel " + confirmPartChannel.getChannelName());
            }
            if (msg instanceof ConfirmChannelMessage) {
                ConfirmChannelMessage confirmChannelMessage = (ConfirmChannelMessage) msg;
                if (gui != null) {
                    gui.handleConfirmChannelMessage(confirmChannelMessage);
                }
                System.out.println("[" + confirmChannelMessage.getChannelName() + "] " + confirmChannelMessage.getNick() + ": " + confirmChannelMessage.getText());
            }
            if (msg instanceof ErrorMessage) {
                ErrorMessage errorMessage = (ErrorMessage) msg;
                if (gui != null) {
                    gui.handleErrorMessage(errorMessage);
                }
                System.out.println("*** ERROR: " + errorMessage.getText());
            }
        }
    }

    public void disconnect() {
        connection.close();
    }
}
