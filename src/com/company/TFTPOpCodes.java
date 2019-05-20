package com.company;

public class TFTPOpCodes {

    enum Modes {
        READREQUEST((byte)1), WRITEREQUEST((byte)2), DATA((byte)3), ACKNOWLEDGMENT((byte)4), ERROR((byte)5);

        private byte code;

        Modes(byte code) {
            this.code = code;
        }
    }

    static Modes getOPMode(byte opCode) {
        switch (opCode) {
            case 1:
                return Modes.READREQUEST;
            case 2:
                return Modes.WRITEREQUEST;
            case 3:
                return Modes.DATA;
            case 4:
                return Modes.ACKNOWLEDGMENT;
            case 5:
                return Modes.ERROR;
            default:
                System.err.println("Incorrect OP mode code.");
                return null;
        }
    }
}
