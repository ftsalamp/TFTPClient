package com.company;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TFTPClient {

    private static final int MAXDATAPACKETSIZE = 516;

    private static InetAddress ip;
    private static DatagramSocket socket;

    private static List<Byte> data = new ArrayList<Byte>();
    private static short block = 0;
    private static int tid = 69;
    private static String fileName;

    private static void initiateConnection(String serverIp, byte opCode, String file) throws IOException {
        ip = InetAddress.getByName(serverIp);
        socket = new DatagramSocket();
        RequestPacket pck = new RequestPacket(opCode, file, "octet");
        byte[] toSend = pck.getPacket();
        DatagramPacket datagramToSend = new DatagramPacket(toSend, toSend.length, ip, tid);

        socket.send(datagramToSend);
    }

    private static void getReceiveResponse() throws IOException {
        byte[] receivedPacketBytes = new byte[MAXDATAPACKETSIZE];
        DatagramPacket receivedPacket =
                new DatagramPacket(receivedPacketBytes, MAXDATAPACKETSIZE, ip, socket.getLocalPort());
        socket.receive(receivedPacket);
        tid = receivedPacket.getPort();
        byte opCode = receivedPacketBytes[1];

        switch (Objects.requireNonNull(TFTPOpCodes.getOPMode(opCode))) {
            case DATA:
                DataPacket pck = new DataPacket(receivedPacketBytes, (short) receivedPacket.getLength());
                getDataFromPacket(pck);
                break;
            case READREQUEST:
            case WRITEREQUEST:
            case ACKNOWLEDGMENT:
            case ERROR:
            default:
                System.err.println("An error has occurred. Closing connection..");
                break;
        }
    }

    public static void main(String[] args) throws IOException {
        String serverIp = "83.212.100.61";
        byte opCode = 1;
        fileName = "test.txt";
        switch (Objects.requireNonNull(TFTPOpCodes.getOPMode(opCode))) {
            case READREQUEST:
                initiateConnection(serverIp, opCode, fileName);
                getReceiveResponse();
                writeFile();
                break;
            case WRITEREQUEST:
            case ACKNOWLEDGMENT:
            case DATA:
                System.out.println("Not implemented yet.");
                break;
            case ERROR:
            default:
                System.err.println("An error has occurred. Closing connection..");
        }
    }

    private static void writeFile() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(Utils.toPrimitive(data.toArray(new Byte[0])));
            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside
            //the try. And this will automatically close the OutputStream
        }
        System.out.println("File received!");
    }

    private static void getDataFromPacket(DataPacket responsePacket) throws IOException {

        if (responsePacket.getBlock() == block + 1){
            data.addAll(responsePacket.getPacketData());
            block = (short) (block + 1);

        }
        sendAck(responsePacket.getBlock());
        if (!responsePacket.isLast()){
            getReceiveResponse();
        }
    }

    private static void sendAck(short block) throws IOException {
        AckPacket pck = new AckPacket(block);
        DatagramPacket datagramToSend = new DatagramPacket(pck.getPacket(),
                AckPacket.PACKETLENGTH, ip, tid);
        socket.send(datagramToSend);
    }
}
