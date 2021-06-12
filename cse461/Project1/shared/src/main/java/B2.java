import lombok.Value;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Value
public class B2 implements Packet {
    public static final int SIZE_BYTES = 8;  // excluding Header

    Header header;
    int tcpPortNum;
    int secretB;

    public static B2 fromBytes(byte[] bytes) throws VerificationException {
        VerifiedPacket verified = Packet.verify(bytes, SIZE_BYTES);
        byte[] payloadBytes = verified.getPayloadBytes();

        // pull out payload fields
        ByteBuffer buffer = ByteBuffer.wrap(payloadBytes);
        int tcpPortNum = buffer.getInt();
        int secretB = buffer.getInt();

        return new B2(verified.getHeader(), tcpPortNum, secretB);
    }

    @Override
    public byte[] toBytes() {
        return ByteBuffer.allocate(Header.SIZE_BYTES + SIZE_BYTES)
                         .order(ByteOrder.BIG_ENDIAN)
                         .put(header.toBytes())
                         .putInt(tcpPortNum)
                         .putInt(secretB)
                         .array();
    }
}
