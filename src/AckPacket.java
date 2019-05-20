import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class AckPacket {

    private byte[] block;
    private final byte OPCODE = 4;
    static final int PACKETLENGTH = 4;

    AckPacket(final short block) {

        ByteBuffer dbuf = ByteBuffer.allocate(2);
        dbuf.putShort(block);
        this.block = dbuf.array();

    }

    byte[] getPacket() {

        List<Byte> packet = new ArrayList<Byte>();

        packet.add((byte) 0);
        packet.add(OPCODE);

        for (byte blockNum : block) {
            packet.add(blockNum);
        }

        return Utils.toPrimitive(packet.toArray(new Byte[0]));
    }

}
