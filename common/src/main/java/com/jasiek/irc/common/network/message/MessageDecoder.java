package com.jasiek.irc.common.network.message;

import com.google.gson.Gson;
import com.jasiek.irc.common.network.message.messages.*;

public class MessageDecoder {

    public Message decode(byte[] data) {
        Gson gson = new Gson();
        MessageWrapper messageWrapper = gson.fromJson(new String(data), MessageWrapper.class);
        switch (messageWrapper.getName()) {
            case "setNick":
                return gson.fromJson(messageWrapper.getParameters(), SetNick.class);
            case "confirmSetNick":
                return gson.fromJson(messageWrapper.getParameters(), ConfirmSetNick.class);
            case "joinChannel":
                return gson.fromJson(messageWrapper.getParameters(), JoinChannel.class);
            case "confirmJoinChannel":
                return gson.fromJson(messageWrapper.getParameters(), ConfirmJoinChannel.class);
            case "partChannel":
                return gson.fromJson(messageWrapper.getParameters(), PartChannel.class);
            case "confirmPartChannel":
                return gson.fromJson(messageWrapper.getParameters(), ConfirmPartChannel.class);
            case "channelMessage":
                return gson.fromJson(messageWrapper.getParameters(), ChannelMessage.class);
            case "confirmChannelMessage":
                return gson.fromJson(messageWrapper.getParameters(), ConfirmChannelMessage.class);
            case "errorMessage":
                return gson.fromJson(messageWrapper.getParameters(), ErrorMessage.class);
        }
        return null;
    }
}
