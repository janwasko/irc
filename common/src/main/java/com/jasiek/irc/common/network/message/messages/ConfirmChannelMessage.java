package com.jasiek.irc.common.network.message.messages;

public class ConfirmChannelMessage extends Message {

    private String channelName;

    private String text;

    private String nick;

    public ConfirmChannelMessage(String channelName, String text, String nick) {
        this.channelName = channelName;
        this.text = text;
        this.nick = nick;
    }

    @Override
    public String getName() {
        return "confirmChannelMessage";
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
