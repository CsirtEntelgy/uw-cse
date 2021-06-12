import lombok.Value;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Value
public class A2 implements Packet {
    public static final int SIZE_BYTES = 16;  // excluding Header

    Header header;
    int num, len, udpPort, secretA;

    public static A2 fromBytes(byte[] bytes) throws VerificationException {
        VerifiedPacket verified = Packet.verify(bytes, SIZE_BYTES);  // using this overload since size is known
        byte[] payloadBytes = verified.getPayloadBytes();

        // pull out payload fields
        ByteBuffer buffer = ByteBuffer.wrap(payloadBytes);
        int num = buffer.getInt();
        int len = buffer.getInt();
        int udpPort = buffer.getInt();
        int secretA = buffer.getInt();

        return new A2(verified.getHeader(), num, len, udpPort, secretA);
    }

    @Override
    public byte[] toBytes() {
        return ByteBuffer.allocate(Header.SIZE_BYTES + SIZE_BYTES)
                         .order(ByteOrder.BIG_ENDIAN)
                         .put(header.toBytes())
                         .putInt(num)
                         .putInt(len)
                         .putInt(udpPort)
                         .putInt(secretA)
                         .array();
    }
}
