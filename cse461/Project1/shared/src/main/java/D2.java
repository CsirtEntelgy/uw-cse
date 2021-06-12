import lombok.Value;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Value
public class D2 implements Packet {
    public static final int SIZE_BYTES = 4;  // excluding Header

    Header header;
    int secretD;

    public static D2 fromBytes(byte[] bytes) throws VerificationException {
        VerifiedPacket verified = Packet.verify(bytes, SIZE_BYTES);
        byte[] payloadBytes = verified.getPayloadBytes();

        // pull out payload fields
        ByteBuffer buffer = ByteBuffer.wrap(payloadBytes);
        int secretD = buffer.getInt();

        return new D2(verified.getHeader(), secretD);
    }

    @Override
    public byte[] toBytes() {
        return ByteBuffer.allocate(Header.SIZE_BYTES + SIZE_BYTES)
                .order(ByteOrder.BIG_ENDIAN)
                .put(header.toBytes())
                .putInt(secretD)
                .array();
    }
}
