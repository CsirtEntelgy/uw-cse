import lombok.Value;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Value
public class C2 implements Packet {
    public static final int SIZE_BYTES = 13;  // excluding Header
    public static final int ALT_SIZE_BYTES = 16;

    Header header;
    int num2, len2, secretC;
    byte ch;     // avoids ambiguity with two byte chars in Java

    public static C2 fromBytes(byte[] bytes) throws VerificationException {
        VerifiedPacket verified;

        // handles possible alternate size of 16 bytes
        try {
            verified = Packet.verify(bytes, SIZE_BYTES);
        } catch (VerificationException e) {
            verified = Packet.verify(bytes, ALT_SIZE_BYTES);
        }
        byte[] payloadBytes = verified.getPayloadBytes();

        // pull out payload fields
        ByteBuffer buffer = ByteBuffer.wrap(payloadBytes);
        int num2 = buffer.getInt();
        int len2 = buffer.getInt();
        int secretC = buffer.getInt();
        byte ch = buffer.get();

        return new C2(verified.getHeader(), num2, len2, secretC, ch);
    }

    @Override
    public byte[] toBytes() {
        return ByteBuffer.allocate(Header.SIZE_BYTES + SIZE_BYTES)
                .order(ByteOrder.BIG_ENDIAN)
                .put(header.toBytes())
                .putInt(num2)
                .putInt(len2)
                .putInt(secretC)
                .put(ch)
                .array();
    }
}
