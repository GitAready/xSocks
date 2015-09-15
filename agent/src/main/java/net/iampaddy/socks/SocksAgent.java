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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
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
//            NetworkInterface nif;
//            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
//            while ((interfaces.hasMoreElements())) {
//                nif = interfaces.nextElement();
//                if (nif.isLoopback() || nif.isPointToPoint() || nif.isVirtual() || !nif.isUp()) {
//                    continue;
//                }
//                Enumeration<InetAddress> addresses = nif.getInetAddresses();
//                while (addresses.hasMoreElements()) {
//                    InetAddress address = addresses.nextElement();
//                    logger.info(nif.getName() + ":" + address.getHostAddress());
//                }
//            }

            Proxy proxy = new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("localhost", 1080));
            Socket socket = new Socket(proxy);
            socket.connect(InetSocketAddress.createUnresolved("www.baidu.com", 80));

            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int length = is.read(buffer);

            System.out.println(new String(buffer, 0, length));

            OutputStream os = socket.getOutputStream();
            os.write("test".getBytes());
            os.flush();

            buffer = new byte[1024];
            length = is.read(buffer);

            System.out.println(new String(buffer, 0, length));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
