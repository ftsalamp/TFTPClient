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

    public int getBlock() {
        byte[] arr = { block[0], block[1] };
        ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
        return wrapped.getInt();
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
