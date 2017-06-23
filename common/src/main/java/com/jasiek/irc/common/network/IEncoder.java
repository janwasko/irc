package com.jasiek.irc.common.network;

import java.io.IOException;

public interface IEncoder {
    void send(byte[] data) throws IOException;
}
