package com.jasiek.irc.common.network.message.messages;

public class ErrorMessage extends Message {

    private String text;

    public ErrorMessage(String text) {
        this.text = text;
    }

    @Override
    public String getName() {
        return "errorMessage";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
