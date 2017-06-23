package com.jasiek.irc.common.network.message.messages;

public class PartChannel extends Message {

    private String channelName;

    public PartChannel(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String getName() {
        return "partChannel";
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
