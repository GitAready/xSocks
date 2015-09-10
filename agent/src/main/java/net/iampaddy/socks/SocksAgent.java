package net.iampaddy.socks;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.iampaddy.socks.codec.Socks5AuthCodec;
import net.iampaddy.socks.handler.FlushHandler;
import net.iampaddy.socks.handler.Socks5AuthHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author Paddy
 */
public class SocksAgent {

    private Logger logger = LoggerFactory.getLogger(SocksAgent.class);

    public static void main(String[] args) {
        SocksAgent agent = new SocksAgent();
        agent.start();
    }

    private void start() {
        logger.info("Starting xSocks agent...");

//        EventLoopGroup acceptorGroup = new NioEventLoopGroup(5, new NamedThreadFactory("SocksAcceptor"));
//        EventLoopGroup workerGroup = new NioEventLoopGroup(5, new NamedThreadFactory("SocksWorker"));
//
//        ServerBootstrap serverBootstrap = new ServerBootstrap();
//        serverBootstrap.group(acceptorGroup, workerGroup)
//                .channel(NioServerSocketChannel.class)
//                .option(ChannelOption.SO_BACKLOG, 128)
//                .childHandler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel socketChannel) throws Exception {
//                        socketChannel.pipeline()
//                                .addLast(FlushHandler.class.getName(), new FlushHandler())
//                                .addLast(Socks5AuthCodec.class.getName(), new Socks5AuthCodec())
//                                .addLast(Socks5AuthHandler.class.getName(), new Socks5AuthHandler());
//                    }
//                })
//                .childOption(ChannelOption.SO_KEEPALIVE, true)
//                .childOption(ChannelOption.TCP_NODELAY, true);

        try {
            NetworkInterface nif;
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while ((interfaces.hasMoreElements())) {
                nif = interfaces.nextElement();
                if (nif.isLoopback() || nif.isPointToPoint() || nif.isVirtual() || !nif.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = nif.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    logger.info(nif.getName() + ":" + address.getHostAddress());
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

}
