package net.iampaddy.socks;

import java.io.IOException;

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
        SocksEngine engineer = SocksEngineImpl.createNewEngineer();
        engineer.startup(new Context());
    }

}
