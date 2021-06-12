import com.google.common.primitives.Bytes;
import lombok.Value;

@Value
class VerifiedPacket implements Packet {
    Header header;
    byte[] headerBytes;
    byte[] payloadBytes;

    @Override
    public byte[] toBytes() {
        return Bytes.concat(headerBytes, payloadBytes);
    }
}