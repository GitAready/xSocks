package net.iampaddy.socks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Description
 *
 * @author paddy.xie
 */
public class ProtocolDetectHandler implements CompletionHandler<Integer, ByteBuffer> {

    private Logger logger = LoggerFactory.getLogger(ProtocolDetectHandler.class);

    private AsynchronousSocketChannel socketChannel;

    public ProtocolDetectHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if(result <= 0) {
            logger.info("No data, don't know why...");
            return;
        }
        buffer.flip();
        byte version = buffer.get();
        byte methodNumber = buffer.get();
        byte[] methods = new byte[methodNumber];
        buffer.get(methods);

        String methodsString = "" + methods[0];
        logger.info("Version: {}", version);
        for(int i = 1; i < methods.length; i++) {
            methodsString += "," + methods[i];
        }
        logger.info("Supported Method: {}", methodsString);

        buffer.clear();
        buffer.put(new byte[]{(byte) 0x05, (byte) 0x00});
        buffer.flip();
        Future<Integer> future = socketChannel.write(buffer);
        try {
            future.get();
            buffer.clear();
            future = socketChannel.read(buffer);
            future.get();
            byte[] domainName = new byte[13];

            buffer.flip();
            byte[] array = buffer.array();
            int length = array[4];
            byte[] address = new byte[length];
            System.arraycopy(array, 5, address, 0, length);

            buffer.clear();
            buffer.put(new byte[]{0x05, 0x00, 0x00, 0x03, (byte) length});
            buffer.put(address);
            buffer.put(new byte[]{0x00, (byte) 0x80});
            buffer.flip();
            future = socketChannel.write(buffer);
            future.get();
            buffer.clear();
            future = socketChannel.read(buffer);
            future.get();
            buffer = ByteBuffer.allocate(1000);
            buffer.put(("HTTP/1.1 200 OK\n" +
                    "Connection: keep-alive\n" +
                    "Date: Thu, 26 Jul 2007 14:00:02 GMT\n" +
                    "Server: Microsoft-IIS/6.0\n" +
                    "X-Powered-By: ASP.NET\n" +
                    "Content-Length: 190\n" +
                    "Content-Type: text/html\n" +
                    "Set-Cookie: ASPSESSIONIDSAATTCSQ=JOPPKDCAMHHBEOICJPGPBJOB;\n" +
                    "path=/\n" +
                    "Cache-control: private\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<title>精通Unix下C语言编程</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<b>精通Unix下C语言编程与项目实战<br></b>\n" +
                    "<b>投票测试<br></b>\n" +
                    "感谢你为选手\n" +
                    "朱云翔\n" +
                    "投票!\n" +
                    "</body> \n" +
                    "</html>").getBytes());
            buffer.put((byte)-1);
            buffer.flip();
            future = socketChannel.write(buffer);
            future.get();
//            socketChannel.close();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        logger.error(exc.getMessage(), exc);
    }
}
