import lombok.Value;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Value
public class B1Ack implements Packet {
    public static final int SIZE_BYTES = 4;  // excluding Header

    Header header;
    int packetId;

    public static B1Ack fromBytes(byte[] bytes) throws VerificationException {
        VerifiedPacket verified = Packet.verify(bytes, SIZE_BYTES);
        byte[] payloadBytes = verified.getPayloadBytes();

        // pull out id field
        ByteBuffer buffer = ByteBuffer.wrap(payloadBytes);
        int packetId = buffer.getInt();

        return new B1Ack(verified.getHeader(), packetId);
    }

    @Override
    public byte[] toBytes() {
        return ByteBuffer.allocate(Header.SIZE_BYTES + SIZE_BYTES)
                         .order(ByteOrder.BIG_ENDIAN)
                         .put(header.toBytes())
                         .putInt(packetId)
                         .array();
    }
}
