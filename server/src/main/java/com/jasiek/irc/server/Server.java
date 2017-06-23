package com.jasiek.irc.server;

import com.jasiek.irc.common.network.message.messages.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Server implements Runnable {

    private int port;

    private Set<User> users = new HashSet<>();

    private Map<String, Set<User>> channels = new HashMap<>();

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        Listener socket;
        try {
            socket = new Listener(this, port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                User user = socket.accept();
                handleNewUser(user);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return;
            }
        }
    }

    private void handleNewUser(User user) {
        users.add(user);
        Thread thread = new Thread(user);
        thread.start();
    }

    public synchronized void onMessageReceived(User user, Message message) {
        if (message instanceof SetNick) {
            SetNick setNick = (SetNick) message;
            if (isNickAvailable(setNick.getNick())) {
                user.setNick(setNick.getNick());
                ConfirmSetNick confirmSetNick = new ConfirmSetNick(setNick.getNick());
                sendToUser(user, confirmSetNick);
                System.out.println("New user: " + setNick.getNick());
            } else {
                ErrorMessage errorMessage = new ErrorMessage("Nick is not available!");
                sendToUser(user, errorMessage);
            }
            return;
        }
        if (user.getNick() == null || user.getNick().equals("")) {
            ErrorMessage errorMessage = new ErrorMessage("Set nick first!");
            sendToUser(user, errorMessage);
            return;
        }
        if (message instanceof JoinChannel) {
            JoinChannel joinChannel = (JoinChannel) message;
            if (!channels.containsKey(joinChannel.getChannelName())) {
                channels.put(joinChannel.getChannelName(), new HashSet<>());
            }

            Set<User> members = channels.get(joinChannel.getChannelName());
            if (members.contains(user)) {
                ErrorMessage errorMessage = new ErrorMessage("You are already in this channel!");
                sendToUser(user, errorMessage);
            } else {
                members.add(user);
                sendToAllInChannel(joinChannel.getChannelName(), member -> {
                    return new ConfirmJoinChannel(joinChannel.getChannelName(), user.getNick());
                });
            }

            System.out.println("Number of people in channel " + joinChannel.getChannelName() + ": " + channels.get(joinChannel.getChannelName()).size());
        }
        if (message instanceof PartChannel) {
            PartChannel partChannel = (PartChannel) message;
            Set<User> members = channels.get(partChannel.getChannelName());
            if (members != null) {
                if (members.contains(user)) {
                    sendToAllInChannel(partChannel.getChannelName(), member -> {
                        return new ConfirmPartChannel(partChannel.getChannelName(), user.getNick());
                    });
                    members.remove(user);
                    System.out.println("Number of people in channel " + partChannel.getChannelName() + ": " + members.size());
                    if (members.size() == 0) {
                        channels.remove(partChannel.getChannelName());
                    }
                } else {
                    ErrorMessage errorMessage = new ErrorMessage("You are not in this channel!");
                    sendToUser(user, errorMessage);
                }
            } else {
                ErrorMessage errorMessage = new ErrorMessage("You are not in this channel!");
                sendToUser(user, errorMessage);
            }
        }
        if (message instanceof ChannelMessage) {
            ChannelMessage channelMessage = (ChannelMessage) message;
            sendToAllInChannel(channelMessage.getChannelName(), member -> {
                return new ConfirmChannelMessage(channelMessage.getChannelName(), channelMessage.getText(), user.getNick());
            });
        }

    }

    private boolean isNickAvailable(String nick) {
        for (User user : users) {
            if (nick.equals(user.getNick())) {
                return false;
            }
        }
        return true;
    }

    public synchronized void onUserDisconnected(User user) {
        users.remove(user);

        for (Map.Entry<String, Set<User>> entry : channels.entrySet()) {
            entry.getValue().remove(user);
        }

        System.out.println("User disconnected: " + user.getNick());
    }

    private void sendToAllInChannel(String channelName, Function<User, Message> createMessage) {
        Set<User> members = channels.get(channelName);
        if (members != null) {
            for (User member : members) {
                Message message = createMessage.apply(member);
                try {
                    member.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendToUser(User user, Message message) {
        try {
            user.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}