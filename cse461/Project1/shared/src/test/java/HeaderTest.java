import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class HeaderTest {

    // can use parameterized junit tests but that's too much work

    private static List<Header> headers = List.of(
            new Header(34, 3, (char) 0, (char) 122),
            new Header(101, 314, (char) 1, (char) Protocol.STUDENT_ID),
            new Header(200, 99, (char) 2, (char) Protocol.STUDENT_ID),
            new Header(500, 0, (char) 3, (char) 122));

    private static List<byte[]> headerBytes = List.of(
            new byte[] {0, 0, 55, 2,
                        0, 0, 3, 5,
                        0, 1, 0, 123},

            new byte[] {1, 2, 3, 4,
                        5, 6, 7, 8,
                        9, 10, 11, 12});

    @Test public void testHeaders() {
        headers.forEach(header -> {
            byte[] bytes = header.toBytes();
            Header headerFromBytes = Header.fromBytes(bytes);
            assertEquals(header, headerFromBytes);
        });
    }

    @Test public void testHeaderBytes() {
        headerBytes.forEach(bytes -> {
            Header header = Header.fromBytes(bytes);
            byte[] bytesFromHeader = header.toBytes();
            assertArrayEquals(bytes, bytesFromHeader);
        });
    }
}
