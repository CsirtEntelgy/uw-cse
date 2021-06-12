import java.util.Arrays;

interface Packet {

    byte[] toBytes();

    // verification when the actual payload length is variable
    // checks that:
    // - the input byte array has a valid header
    // - the header's payload_length field matches the actual payload length (the remainder of the array)
    // returns a VerifiedPacket object containing the parsed Header and payload byte[]
    static VerifiedPacket verify(byte[] bytes) throws VerificationException {
        // check that bytes can contain a header
        if (bytes.length < Header.SIZE_BYTES) {
            throw new VerificationException("message shorter than a header");
        }

        byte[] headerBytes = Arrays.copyOfRange(bytes, 0, Header.SIZE_BYTES);
        byte[] payloadBytes = Arrays.copyOfRange(bytes, Header.SIZE_BYTES, bytes.length);
        Header header = Header.fromBytes(headerBytes);

        // check that header payload_length field matches payload length
        if (header.getPayloadLen() != payloadBytes.length) {
            throw new VerificationException("incorrect payload size in header");
        }

        return new VerifiedPacket(header, headerBytes, payloadBytes);
    }

    // verification when the actual payload length is known
    // checks that:
    // - the input byte array has a valid header
    // - the header's payload_length field matches the actual payload length (the remainder of the array)
    // - the actual payload length matches the expected payload length
    // returns a VerifiedPacket object containing the parsed Header and payload byte[]
    static VerifiedPacket verify(byte[] bytes, int expectedPayloadLength) throws VerificationException {
        VerifiedPacket headerVerified = verify(bytes);

        // check that content length is as expected
        if (headerVerified.getPayloadBytes().length != expectedPayloadLength) {
            throw new VerificationException("unexpected payload length");
        }

        return headerVerified;
    }
}
