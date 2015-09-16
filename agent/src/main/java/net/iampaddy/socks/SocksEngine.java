package net.iampaddy.socks;

/**
 * Description
 *
 * @author paddy.xie
 */
public interface SocksEngine {

    public void startup(Context context);

    public void shutdown();

    public EngineerStatus status();

    public boolean isRunning();

}
