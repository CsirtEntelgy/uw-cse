import org.junit.Test;
import static org.junit.Assert.assertFalse;

public class UtilsTest {
    @Test public void testFromLong() {
        long input = 512;
        byte[] result = Utils.fromLong(input);

        boolean allZero = true;
        for (byte b : result) {
            if (b != 0) {
                allZero = false;
            }
        }
        assertFalse(allZero);
    }
}
