package com.company;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DataPacket {

    private int packetLength;
    private List<Byte> data = new ArrayList<Byte>();
    private short block;

    DataPacket(final byte[] data, short length) {
        this.packetLength = length;

        for (int i = 4; i < packetLength; i++){
            this.data.add(data[i]);
        }

        ByteBuffer tempBlockNumBuff = ByteBuffer.wrap(new byte[]{data[2], data[3]});
        this.block = tempBlockNumBuff.getShort();

    }

    public int getPacketLength() {
        return packetLength;
    }

    short getBlock() {
        return block;
    }

//    byte[] getPacketData() {
//        return Utils.toPrimitive(data.toArray(new Byte[0]));
//    }

    List<Byte> getPacketData() {
        return data;
    }

    boolean isLast() {
        return packetLength < 516;
    }

}
