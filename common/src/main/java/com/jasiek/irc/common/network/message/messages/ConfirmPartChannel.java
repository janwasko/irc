package com.jasiek.irc.common.network.message.messages;

public class ConfirmPartChannel extends Message {

    private String channelName;

    private String nick;

    public ConfirmPartChannel(String channelName, String nick) {
        this.channelName = channelName;
        this.nick = nick;
    }

    public ConfirmPartChannel(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String getName() {
        return "confirmPartChannel";
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