import lombok.Value;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Value
public class B1 implements Packet {

    Header header;
    int packetId;
    int payloadLen;

    public static B1 fromBytes(byte[] bytes) throws VerificationException {
        VerifiedPacket verified = Packet.verify(bytes);  // using this overload since size is variable
        byte[] payloadBytes = verified.getPayloadBytes();

        // pull out payload fields
        ByteBuffer buffer = ByteBuffer.wrap(payloadBytes);
        int packetId = buffer.getInt();
        int payloadLen = 0;

        // increment payloadLen while next byte is 0
        while (true) {
            try {
                if (buffer.get() == 0) {
                    payloadLen++;
                } else {
                    throw new VerificationException("payload contains nonzero byte");
                }
            } catch (BufferUnderflowException e) {
                break;
            }
        }

        return new B1(verified.getHeader(), packetId, payloadLen);
    }

    @Override
    public byte[] toBytes() {
        return ByteBuffer.allocate(Header.SIZE_BYTES + 4 + payloadLen)
                         .order(ByteOrder.BIG_ENDIAN)
                         .put(header.toBytes())
                         .putInt(packetId)
                         // payload is already zeroed out
                         .array();
    }
}
