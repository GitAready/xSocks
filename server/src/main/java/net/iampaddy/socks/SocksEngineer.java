package net.iampaddy.socks;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * Description
 *
 * @author paddy.xie
 */
public interface SocksEngineer {

    public void startup(Context context);

    public void shutdown();

    public void submit(AsynchronousSocksWork socksWork);

    public EngineerStatus status();

    public boolean isRunning();

}
