import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ClientHandler implements Runnable {

    private static final A1 expectedA1 = new A1("hello world\0");

    // random number limits
    private static final int SECRET_LIMIT = 10000;   // arbitrary
    private static final int UDP_NUM_LIMIT = 75;     // don't want the unreliable sequence to take forever
    private static final int UDP_LEN_LIMIT = 10000;  // will be multiplied by 4 for alignment
    private static final int TCP_NUM_LIMIT = 1000;   // arbitrary
    private static final int TCP_LEN_LIMIT = 10000;  // will be multiplied by 4 for alignment

    // instance variables
    private final ThreadLocalRandom random;
    private final DatagramPacket initialPacket;
    private final SocketAddress clientAddress;

    // shared state for UDP stages
    private final int udpNum;
    private final int udpLen;
    private final int secretA;
    private final int secretB;
    private DatagramSocket udpSocket;

    // shared state for TCP stages
    private final byte ch;
    private final int tcpNum;
    private final int tcpLen;
    private final int secretC;
    private final int secretD;
    private InputStream reader;
    private OutputStream writer;
    private Socket tcpClientSocket;
    private ServerSocket tcpServerSocket;

    ClientHandler(DatagramPacket packet) {
        initialPacket = packet;

        // sender of initialPacket is the only client for this thread
        clientAddress = new InetSocketAddress(packet.getAddress(), packet.getPort());

        // can generate randoms early
        random = ThreadLocalRandom.current();
        secretA = random.nextInt(1, SECRET_LIMIT);
        secretB = random.nextInt(1, SECRET_LIMIT);
        secretC = random.nextInt(1, SECRET_LIMIT);
        secretD = random.nextInt(1, SECRET_LIMIT);
        udpNum = random.nextInt(1, UDP_NUM_LIMIT);
        tcpNum = random.nextInt(1, TCP_NUM_LIMIT);

        // multiplied by 4 to ensure alignment
        udpLen = random.nextInt(1, UDP_LEN_LIMIT) * 4;
        tcpLen = random.nextInt(1, TCP_LEN_LIMIT) * 4;

        // workaround for generating one random byte
        byte[] chs = new byte[1];
        random.nextBytes(chs);
        ch = chs[0];
    }

    @Override
    public void run() throws RuntimeException {
        try {
            checkInitialPacket();
            stageA();
            stageB();
            stageC();
            stageD();
        } catch (Exception e) {
            // prints stack and terminates the thread upon failure of any stage
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                cleanup();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkInitialPacket() throws VerificationException {
        byte[] expectedBytes = expectedA1.toBytes();
        byte[] actualBytes = initialPacket.getData();

        if (Arrays.equals(expectedBytes, actualBytes)) {
            A1 actualA1 = A1.fromBytes(actualBytes);
            if (expectedA1.equals(actualA1)) {
                return;
            }
        }

        throw new VerificationException("unexpected A1 packet");
    }

    // UDP Stages

    private void stageA() throws SocketException, IllegalThreadStateException {
        // effectively random socket selection, binds to any available UDP port on localhost
        udpSocket = new DatagramSocket();
        int udpPort = udpSocket.getLocalPort();
        if (udpPort < 1024) {
            throw new IllegalThreadStateException("UDP socket bound to an administrative port");
        }

        // send A2 datagram to client
        Header header = Header.forServer(A2.SIZE_BYTES, 0);  // prev secret is 0 for stage A
        A2 datagram = new A2(header, udpNum, udpLen, udpPort, secretA);
        Utils.sendBytesUDP(datagram.toBytes(), udpSocket, clientAddress);
    }

    private void stageB() throws IOException, VerificationException, IllegalThreadStateException {
        int currId = 0;
        Header ackHeader = Header.forServer(B1Ack.SIZE_BYTES, secretA);

        // ack loop
        while (currId <= udpNum - 1) {

            // receive the next B1 packet
            byte[] packetBytes = new byte[Header.SIZE_BYTES + 4 + udpLen];
            DatagramPacket packet = new DatagramPacket(packetBytes, packetBytes.length);
            udpSocket.receive(packet);
            B1 recv = B1.fromBytes(packet.getData());

            // verify payload length
            if (recv.getPayloadLen() != udpLen) {
                throw new IllegalThreadStateException("unexpected payload length");
            }

            // received prev ID -> prev ack was not received, need to re-ack
            if (recv.getPacketId() == currId - 1) {
                if (random.nextBoolean()) {
                    B1Ack prevAck = new B1Ack(ackHeader, currId - 1);
                    Utils.sendBytesUDP(prevAck.toBytes(), udpSocket, clientAddress);
                }
            }

            // received current ID -> send ack and increment
            else if (recv.getPacketId() == currId) {
                if (random.nextBoolean()) {
                    B1Ack ack = new B1Ack(ackHeader, currId);
                    Utils.sendBytesUDP(ack.toBytes(), udpSocket, clientAddress);
                    currId++;
                }
            }

            // received older or newer ID, breaks protocol
            else {
                throw new IllegalThreadStateException("received out-of-order ID: " + recv.getPacketId());
            }
        }

        // TODO: may need to enable NO_DELAY option to reduce buffering
        // effectively random socket selection, binds to any available TCP port on localhost
        tcpServerSocket = new ServerSocket(0);
        int tcpPort = tcpServerSocket.getLocalPort();
        if (tcpPort < 1024) {
            throw new IllegalThreadStateException("TCP socket bound to an administrative port");
        }

        // send B2 datagram to client
        Header header = Header.forServer(B2.SIZE_BYTES, secretA);
        B2 datagram = new B2(header, tcpPort, secretB);
        Utils.sendBytesUDP(datagram.toBytes(), udpSocket, clientAddress);
    }

    // TCP Stages

    private void stageC() throws IOException {
        // accept next client that connects and setup IO streams
        tcpClientSocket = tcpServerSocket.accept();
        reader = tcpClientSocket.getInputStream();
        writer = tcpClientSocket.getOutputStream();

        // send C2 segment to client
        Header header = Header.forServer(C2.SIZE_BYTES, secretB);
        C2 segment = new C2(header, tcpNum, tcpLen, secretC, ch);
        writer.write(segment.toBytes());
        writer.flush();
    }

    private void stageD() throws IOException, VerificationException {
        int d1Count = 0;
        int d1Length = Header.SIZE_BYTES + tcpLen;

        // consume all D1 segments
        while (d1Count < tcpNum) {
            byte[] d1Bytes = reader.readNBytes(d1Length);
            D1 segment = D1.fromBytes(d1Bytes);
            if (segment.getCh() != ch) {
                throw new VerificationException("ch mismatch");
            }
            d1Count++;
        }

        // send D2 segment to client
        Header header = Header.forServer(D2.SIZE_BYTES, secretD);
        D2 segment = new D2(header, secretD);
        writer.write(segment.toBytes());
        writer.flush();
    }

    private void cleanup() throws IOException {
        if (udpSocket != null) {
            udpSocket.disconnect();
            udpSocket.close();
        }

        if (reader != null) {
            reader.close();
        }

        if (writer != null) {
            writer.close();
        }

        if (tcpClientSocket != null) {
            tcpClientSocket.close();
        }

        if (tcpServerSocket != null) {
            tcpServerSocket.close();
        }
    }
}
