package net.iampaddy.socks;

import net.iampaddy.socks.acceptor.Acceptor;

import java.io.IOException;
import java.util.List;

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
        List<Acceptor> acceptors = conf.getAcceptor();

    }

}
