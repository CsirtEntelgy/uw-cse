import lombok.Value;
import java.nio.charset.StandardCharsets;
import com.google.common.primitives.Bytes;

@Value
public class A1 implements Packet {

    Header header;
    String content;

    public A1(String content) {
        // check for \0 terminator
        if (content.charAt(content.length() - 1) != '\0') {
            throw new IllegalArgumentException("No terminator for argument: " + content);
        }

        // UTF-8 encoding => one byte per common character
        this.content = content;
        byte[] contentBytes = content.getBytes(Protocol.CHARSET);
        header = Header.forClient(contentBytes.length, 0);
    }

    private A1(Header header, String content) {
        this.header = header;
        this.content = content;
    }

    public static A1 fromBytes(byte[] bytes) throws VerificationException {
        VerifiedPacket verified = Packet.verify(bytes);  // using this overload since size is variable
        byte[] payloadBytes = verified.getPayloadBytes();

        // pull out content String
        String content = new String(payloadBytes, Protocol.CHARSET);

        return new A1(verified.getHeader(), content);
    }

    @Override
    public byte[] toBytes() {
        return Bytes.concat(header.toBytes(),
                            content.getBytes(Protocol.CHARSET));
    }
}
