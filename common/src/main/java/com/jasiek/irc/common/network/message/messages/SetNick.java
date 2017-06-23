package com.jasiek.irc.common.network.message.messages;

public class SetNick extends Message {

    private String nick;

    public SetNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String getName() {
        return "setNick";
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
