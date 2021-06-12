import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Utils {

    // from: https://stackoverflow.com/questions/44853653/java-uint32-stored-as-long-to-4-byte-array
    public static byte[] fromLong(long value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putLong(value);
        return Arrays.copyOfRange(bytes, 4, 8);
    }

    public static long toLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8).put(new byte[]{0, 0, 0, 0}).put(bytes);
        buffer.position(0);
        return buffer.getLong();
    }

    public static byte[] fromInt(int value) {
        byte[] bytes = new byte[4];
        ByteBuffer.wrap(bytes).putInt(value);
        return Arrays.copyOfRange(bytes, 2, 4);
    }

    public static int toInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4).put(new byte[]{0, 0}).put(bytes);
        buffer.position(0);
        return buffer.getInt();
    }

    public static byte[] makeHeader(long payloadLen, byte[] psecret, int step) {
        if (psecret.length != 4) {
            throw new IllegalArgumentException();
        }

        byte[] payloadLenBytes = fromLong(payloadLen);
        byte[] stepBytes = fromInt(step);
        byte[] studentId = fromInt(Protocol.STUDENT_ID);

        byte[] tmp = concat(payloadLenBytes, psecret);
        tmp = concat(tmp, stepBytes);
        tmp = concat(tmp, studentId);

        return tmp;
    }

    public static byte[] concat(byte[] a, byte[] b) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            outputStream.write(a);
            outputStream.write(b);
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

    public static void sendBytesUDP(byte[] data, DatagramSocket udpSocket, SocketAddress destination) {
        DatagramPacket packet = new DatagramPacket(data, data.length, destination);
        try {
            udpSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // receives and returns byte array with given length (probably should add length error check)
    public static byte[] receiveBytesUDP(DatagramSocket udpSocket, int length) throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[length], length);
        udpSocket.receive(packet);
        return packet.getData();
    }

    // align to 4 bytes?
    public static byte[] byteAlign(byte[] b){
        if(b.length % 4 == 0)
            return b;
        return concat(b, new byte[4 - b.length % 4]);
    }

    // parse given byte array into array of ints, assuming 4 bytes as an instance of int (cast to int)
    public static int[] parseResponse(byte[] response){
        int[] result = new int[response.length/4];

        for(int i = 0; i < response.length; i += 4)
            result[i] = (int) toLong(Arrays.copyOfRange(response, i, i + 4));

        return result;
    }
}
