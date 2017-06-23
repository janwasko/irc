package com.jasiek.irc.common.network.message.messages;

public class ConfirmJoinChannel extends Message {

    private String channelName;

    private String nick;

    public ConfirmJoinChannel(String channelName, String nick) {
        this.channelName = channelName;
        this.nick = nick;
    }

    @Override
    public String getName() {
        return "confirmJoinChannel";
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
