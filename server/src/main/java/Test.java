import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by paddy.xie on 2015/2/27.
 */
public class Test {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("localhost", 1080));
        Socket socket = serverSocket.accept();
        InputStream is = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        byte[] buffer = read(is);

    }

    public static byte[] read(InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[100];
        int i;
        while((i = is.read(buffer)) != -1){
            result.write(buffer, 0, i);
        }
        return result.toByteArray();
    }

}
