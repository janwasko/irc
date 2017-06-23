package com.jasiek.irc.common.network.message.messages;

public class ConfirmSetNick extends Message {

    private String nick;

    public ConfirmSetNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String getName() {
        return "confirmSetNick";
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
