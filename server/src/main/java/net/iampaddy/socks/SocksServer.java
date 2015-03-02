package net.iampaddy.socks;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;

public class SocksServer {

    public static void main(String[] args) throws IOException {

        SocksServer server = new SocksServer();
        server.startup();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startup() {
        SocksEngineer engineer = AsynchronousSocksEngineer.createNewEngineer();
        engineer.startup(new Context());
    }

}
