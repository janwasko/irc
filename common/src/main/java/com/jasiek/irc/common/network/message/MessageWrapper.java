package com.jasiek.irc.common.network.message;

public class MessageWrapper {

    private String name;
    private String parameters;

    public MessageWrapper() {
    }

    public MessageWrapper(String name, String parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
