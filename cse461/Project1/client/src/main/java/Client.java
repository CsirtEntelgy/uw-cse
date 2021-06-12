import java.io.*;
import java.net.*;
import java.util.Optional;

public class Client {

    private static final DatagramSocket udpSocket;
    private ServerSocket tcpSocket; // created global for step C and D

    static {
        try {
            udpSocket = new DatagramSocket();   // any available socket on localhost
        } catch (SocketException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        while (true) {
            boolean complete = client.stageA()
                                     .flatMap(client::stageB)
                                     .flatMap(client::stageC)
                                     .flatMap(client::stageD)
                                     .isPresent();
            if (complete) {
                break;
            }
        }
    }

    private Optional<A2> stageA() {
        // encapsulates fromBytes and toBytes for this message
        A1 message = new A1("hello world\0");

        // send to server
        SocketAddress destinationA = new InetSocketAddress(Protocol.ATTU_HOSTNAME, Protocol.UDP_PORT);
        Utils.sendBytesUDP(message.toBytes(), udpSocket, destinationA);

        try {
            // wait for response
            byte[] responseBytes = new byte[Header.SIZE_BYTES + A2.SIZE_BYTES];
            DatagramPacket receivedPacket = new DatagramPacket(responseBytes, responseBytes.length);
            udpSocket.receive(receivedPacket);

            // parse response into A2 object
            A2 response = A2.fromBytes(receivedPacket.getData());
            return Optional.of(response);
        }

        catch (IOException | VerificationException e) {
            // DatagramSocket::receive() throws IOException
            // A2::fromBytes() throws IndexOutOfBoundsException
            e.printStackTrace();
            return Optional.empty();
        }
    }



    private Optional<B2> stageB(A2 prev) {
        SocketAddress destinationB = new InetSocketAddress(Protocol.ATTU_HOSTNAME, prev.getUdpPort());

        // establish 500 ms timeout for resending
        try {
            udpSocket.setSoTimeout(500);
        } catch (SocketException e) {
            return Optional.empty();
        }

        int counter = 0;
        Header header = Header.forClient(prev.getLen() + 4, prev.getSecretA());

        while (counter < prev.getNum()) {
            // creating B1 packet to send
            B1 packet = new B1(header, counter, prev.getLen());
            byte[] packetData = packet.toBytes();

            // resend loop for current B1 packet
            while (true) {
                try {
                    // sending and receiving...
                    Utils.sendBytesUDP(packetData, udpSocket, destinationB);
                    byte[] response = Utils.receiveBytesUDP(udpSocket, Header.SIZE_BYTES + B1Ack.SIZE_BYTES);
                    B1Ack responsePacket = B1Ack.fromBytes(response);

                    // only move to next packet if response matches
                    if (counter == responsePacket.getPacketId()) {
                        counter++;
                        break;
                    }
                } catch (SocketException | VerificationException e) {
                    // no response in .5 seconds or unverifiable message -> retry
                } catch (IOException ie) {
                    ie.printStackTrace(); // null pointer issues -> retry
                } catch (Exception e) {
                    e.printStackTrace();
                    return Optional.empty();
                }
            }
        }

        try {
            udpSocket.setSoTimeout(0);  // remove timeout, set to infinite

            // receive and return B2
            byte[] responseBytes = Utils.receiveBytesUDP(udpSocket, Header.SIZE_BYTES + B2.SIZE_BYTES);
            B2 response = B2.fromBytes(responseBytes);
            return Optional.of(response);
        } catch (IOException | VerificationException e) {
            // UDP IO error or unverifiable message -> fail stage
            e.printStackTrace();
            return Optional.empty();
        }

        /* some thoughts to share (before i forget them)
        * 1. from<->to seems redundant? maybe "int -> 4byte" "int -> 2byte" is sufficient (since most fields are ints)
        * 2. char is 2byte in java, need to parse that
        * 3. need a huge & precise packet checker for "server.java"
        * 4. stages should return something? (global variable could work since we go back to stage A when we fail?)
        * 5. 4byte alignment (created a method @Utils.java)
        * 6. Connections seem to change, so maybe a "connect method" that returns a socket when given port & UDP|TCP?
        * */

        /*
         * tried to address some of these concerns with classes that handle each message type and their
         * translation to and from byte[] representations in the shared package.
         *
         * Packet::verify() currently does header checking, we can add packet alignment checks to that too
         * (like that payloadLen % 4 == 0 or something)
         *
         * tried to address stage sequencing with Optionals for intermediate stages and a while loop in main()
         *
         * don't think we need multiple sockets
         *
         * received byte[] will be parsed into message in previous stage
         */
    }

    private Optional<C2> stageC(B2 prev) {
        Socket soc = new Socket();
        SocketAddress destinationC = new InetSocketAddress(Protocol.ATTU_HOSTNAME, prev.getTcpPortNum());

        // Making TCP Connection via server socket
        try {
            tcpSocket.bind(destinationC);
            soc = tcpSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read from server & convert to byte array - !!!need to discuss the size boundary (char being 1 byte)
        byte[] data = new byte[25]; // 12 for header 13 for payload (??)
        try {
            soc.getInputStream().read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create C2 object and return
        C2 response = C2.fromBytes(data);
        return Optional.of(response);
    }

    private Optional<D2> stageD(C2 prev) {
        int counter = 0;
        Socket soc = new Socket();
        Header header = Header.forClient(prev.getLen2(), prev.getSecretC());

        // Create Tcp Send-Connection
        try {
            soc = tcpSocket.accept(); // Already bound from stage C
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Send Num2 number of packets via TCP
        while(counter < prev.getNum2()){
            D1 packet = new D1(header, prev.getC(), prev.getLen2());
            try {
                soc.getOutputStream().write(packet.toBytes());
                soc.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        }

        // Read from server
        byte[] data = new byte[16]; //12 for header, 4 for payload
        try{
            soc.getInputStream().read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create D2 object and return
        D2 response = D2.fromBytes(data);
        return Optional.of(response);
    }
}
