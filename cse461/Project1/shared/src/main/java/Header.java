import lombok.Value;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Value  // generates constructor, getters, equals, toString, hashcode, etc.
public class Header {
    public static final int SIZE_BYTES = 12;
    int payloadLen, psecret;    // signed, 32-bit
    char step, studentID;       // unsigned, 16-bit

    public static Header fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, SIZE_BYTES);
        int payloadLen = buffer.getInt();
        int psecret = buffer.getInt();
        char step = buffer.getChar();
        char studentID = buffer.getChar();

        return new Header(payloadLen, psecret, step, studentID);
    }

    public static Header forClient(int payloadLen, int psecret) {
        return new Header(payloadLen, psecret, (char) 1, (char) Protocol.STUDENT_ID);
    }

    public static Header forServer(int payloadLen, int psecret) {
        return new Header(payloadLen, psecret, (char) 2, (char) Protocol.STUDENT_ID);
    }

    public byte[] toBytes() {
        return ByteBuffer.allocate(SIZE_BYTES)
                         .order(ByteOrder.BIG_ENDIAN)
                         .putInt(payloadLen)
                         .putInt(psecret)
                         .putChar(step)
                         .putChar(studentID)
                         .array();
    }
}
