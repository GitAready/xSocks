package com.github.xsocks;

import com.github.xsocks.acceptor.SocketAcceptor;

import java.io.IOException;

public class SocksServer {

    private Configuration conf;

    SocksServer(Configuration conf) {
        this.conf = conf;
    }

    public static void main(String[] args) throws IOException {

        SocksServer server = new SocksServer(null);
        server.start();

    }

    public void start() {
        SocketAcceptor acceptor = new SocketAcceptor();
        acceptor.setAddress("localhost");
        acceptor.setPort(8778);
        acceptor.accept();

    }

}
