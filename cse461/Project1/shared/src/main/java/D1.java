import lombok.Value;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

@Value
public class D1 implements Packet {

    Header header;
    byte ch;
    int len2;

    public static D1 fromBytes(byte[] bytes) throws VerificationException {
        VerifiedPacket verified = Packet.verify(bytes);  // using this overload since size is variable
        byte[] payloadBytes = verified.getPayloadBytes();

        // pull out payload fields
        ByteBuffer buffer = ByteBuffer.wrap(payloadBytes);
        byte ch = buffer.get();
        int len2 = 1;

        // increment payloadLen while next byte is c
        while (true) {
            try {
                if (buffer.get() == ch) {
                    len2++;
                } else {
                    throw new VerificationException("payload contains non-c byte");
                }
            } catch (BufferUnderflowException e) {
                break;
            }
        }

        return new D1(verified.getHeader(), ch, len2);
    }

    @Override
    public byte[] toBytes() {
        byte[] arr = new byte[len2];
        Arrays.fill(arr, ch);

        return ByteBuffer.allocate(Header.SIZE_BYTES + len2)
                .order(ByteOrder.BIG_ENDIAN)
                .put(header.toBytes())
                .put(arr)
                .array();
    }
}
