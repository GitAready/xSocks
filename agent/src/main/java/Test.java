import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

/**
 * Created by xpjsk on 2015/9/16.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("localhost", 1080));
        Socket socket = new Socket(proxy);
        socket.connect(InetSocketAddress.createUnresolved("www.baidu.com", 443));

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

    }
}
