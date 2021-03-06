import java.util.ArrayList;
import java.util.List;

public class RequestPacket {

    private byte opcode;
    private char[] file, mode;

    public RequestPacket(final byte opcode, final String file, final String mode) {
        this.opcode = opcode;
        this.file = file.toCharArray();
        this.mode = mode.toCharArray();
    }

    public byte[] getPacket() {

        List<Byte> packet = new ArrayList<Byte>();

        packet.add((byte) 0);
        packet.add(opcode);

        for (char character : file)
        {
            packet.add((byte) character);
        }
        packet.add((byte) 0);

        for (char modeChar : mode)
        {
            packet.add((byte) modeChar);
        }
        packet.add((byte) 0);

        return Utils.toPrimitive(packet.toArray(new Byte[0]));
    }

}
