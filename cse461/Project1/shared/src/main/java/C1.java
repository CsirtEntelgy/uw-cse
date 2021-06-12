import lombok.Value;

@Value
public class C1 implements Packet {

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }
}
