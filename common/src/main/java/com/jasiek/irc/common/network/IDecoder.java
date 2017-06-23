package com.jasiek.irc.common.network;

import java.io.IOException;

public interface IDecoder {
    byte[] receive() throws IOException;
}
