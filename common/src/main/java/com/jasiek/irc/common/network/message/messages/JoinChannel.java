package com.jasiek.irc.common.network.message.messages;

public class JoinChannel extends Message {

    private String channelName;

    public JoinChannel(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String getName() {
        return "joinChannel";
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
