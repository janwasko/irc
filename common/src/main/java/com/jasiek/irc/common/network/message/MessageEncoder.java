package com.jasiek.irc.common.network.message;

import com.google.gson.Gson;
import com.jasiek.irc.common.network.message.messages.Message;

public class MessageEncoder {

    public byte[] encode(Message message) {
        Gson gson = new Gson();
        MessageWrapper messageWrapper = new MessageWrapper(message.getName(), gson.toJson(message));
        String json = gson.toJson(messageWrapper);
        //System.out.println(json);
        return json.getBytes();
    }
}
