package com.company;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ErrorPacket {

    private byte errorcode;
    private char[] errorMessage;
    private int packetLength;

    private final byte OPCODE = 5;
    private final int OPCODELENGTH = 2;
    private final int ERRORCODELENGTH = 2;
    private final int PADDINGLENGTH = 1;

    public ErrorPacket(final byte errorcode, @NotNull final String errorMessage) {

        packetLength = OPCODELENGTH + ERRORCODELENGTH + errorMessage.length() + PADDINGLENGTH;
        this.errorMessage = errorMessage.toCharArray();
        this.errorcode = errorcode;

    }

    public byte[] getPacket() {

        List<Byte> packet = new ArrayList<Byte>();

        packet.add((byte) 0);
        packet.add(OPCODE);

        packet.add((byte) 0);
        packet.add(errorcode);

        for (char character : errorMessage) {
            packet.add((byte) character);
        }
        packet.add((byte) 0);


        return Utils.toPrimitive(packet.toArray(new Byte[0]));
    }

}


