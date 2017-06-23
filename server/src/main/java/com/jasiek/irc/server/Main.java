package com.jasiek.irc.server;

class Main {

    public static void main(String[] args) {
        int port;
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 6789;
        }
        Server server = new Server(port);
        server.run();
    }
}
