import java.io.IOException;
import java.net.*;

public class Server {

    private static final DatagramSocket udpSocket;

    static {
        try {
            udpSocket = new DatagramSocket(Protocol.UDP_PORT);  // on localhost
        } catch (SocketException e) {
            e.printStackTrace();
            throw new RuntimeException(e);  // crash early
        }
    }

    public static void main(String[] args) {
        while (true) {
            // receive new datagram
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            // start new handler thread, pass in datagram
            try {
                udpSocket.receive(packet);
                new Thread(new ClientHandler(packet)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
