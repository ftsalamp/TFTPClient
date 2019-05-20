import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ErrorPacket {

    private int errorcode;
    private List<Character> errorMessage = new ArrayList<Character>();
    private int packetLength;

    private final byte OPCODE = 5;

    ErrorPacket(final byte[] data, int packetLength) {
        this.errorcode = new Byte(data[1]).intValue();
        this.packetLength = packetLength;

        for (int i = 4; i < packetLength - 1; i++){
            this.errorMessage.add((char) data[i]);
        }

    }

    String getErrorMessage() {
        return Utils.toString(errorMessage);
    }

    public byte[] getPacket() {

        List<Byte> packet = new ArrayList<Byte>();

        packet.add((byte) 0);
        packet.add(OPCODE);

        packet.add((byte) 0);
        packet.add((byte) errorcode);

        for (char character : errorMessage) {
            packet.add((byte) character);
        }
        packet.add((byte) 0);


        return Utils.toPrimitive(packet.toArray(new Byte[0]));
    }

}


