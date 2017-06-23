package com.jasiek.irc.common.network.message.messages;

public class ChannelMessage extends Message {

    private String channelName;

    private String text;

    public ChannelMessage(String channelName, String text) {
        this.channelName = channelName;
        this.text = text;
    }

    @Override
    public String getName() {
        return "channelMessage";
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
