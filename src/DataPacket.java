import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DataPacket {

    private int packetLength;
    private List<Byte> data = new ArrayList<Byte>();
    private short block;

    DataPacket(final byte[] data, short length) {
        this.packetLength = length;

        ByteBuffer tempBlockNumBuff = ByteBuffer.wrap(new byte[]{data[2], data[3]});
        this.block = tempBlockNumBuff.getShort();

        for (int i = 4; i < packetLength; i++){
            this.data.add(data[i]);
        }


    }

    short getBlock() {
        return block;
    }

    List<Byte> getPacketData() {
        return data;
    }

    boolean isLast() {
        return packetLength < 516;
    }

}
