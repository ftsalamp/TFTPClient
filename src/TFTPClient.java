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
    private static short retryCount = 4;
    private static String fileName;

    private static void initiateConnection(String serverIp, byte opCode, String file){
        try {
            ip = InetAddress.getByName(serverIp);
        } catch (UnknownHostException e) {
            System.err.println("Cannot resolve IP address. " + e.getMessage());
            System.exit(1);
        }
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.err.println("Failed to initialize UDP socket. " + e.getMessage());
            System.exit(1);
        }
        try {
            socket.setSoTimeout(10000);
        } catch (SocketException e) {
            System.err.println("Connection timed out.");
        }
        canSaveFile();
        RequestPacket pck = new RequestPacket(opCode, file, "octet");
        byte[] toSend = pck.getPacket();
        DatagramPacket datagramToSend = new DatagramPacket(toSend, toSend.length, ip, tid);

        try {
            socket.send(datagramToSend);
        } catch (IOException e) {
            System.err.println("Failed to send TFTP Packet. " + e.getMessage());
            System.exit(1);
        }
    }

    private static void getReceiveResponse(){
        byte[] receivedPacketBytes = new byte[MAXDATAPACKETSIZE];
        DatagramPacket receivedPacket =
                new DatagramPacket(receivedPacketBytes, MAXDATAPACKETSIZE, ip, socket.getLocalPort());
        try {
            socket.receive(receivedPacket);
        } catch (IOException e) {
            System.err.println("Failed to receive TFTP Packet. " + e.getMessage());
            System.exit(1);
        }
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
                ErrorPacket errorPck = new ErrorPacket(receivedPacketBytes, receivedPacket.getLength());
                System.err.println("Server returned the following error: " + errorPck.getErrorMessage());
                System.err.println("Exiting..");
                System.exit(1);
            default:
                System.err.println("An error has occurred. Exiting..");
                System.exit(1);
        }
    }

    public static void main(String[] args) {
        if (args.length == 3){
            String serverIp = args[0];
            fileName = args[1];
            byte opCode = 0;
            try {
                opCode = (byte)Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid mode parameter. Accepted values are 1 for receive and 2 for send.");
                System.exit(1);
            }
            switch (Objects.requireNonNull(TFTPOpCodes.getOPMode(opCode))) {
                case READREQUEST:
                    System.out.println("Requesting file: " + fileName + " ....");
                    initiateConnection(serverIp, opCode, fileName);
                    getReceiveResponse();
                    writeFile();
                    break;
                case WRITEREQUEST:
                case ACKNOWLEDGMENT:
                case DATA:
                case ERROR:
                default:
                    System.err.println("An error has occurred. Exiting..");
            }
        } else {
            System.err.println("Invalid number of arguments! Please type first the IP of the TFTP server, second the " +
                    "filename and last the mode (1 for read or 2 for write).");
        }
    }

    private static void writeFile() {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(Utils.toPrimitive(data.toArray(new Byte[0])));
            fos.close();
        } catch (IOException e) {
            System.err.println("Cannot write file. " + e.getMessage());
            System.exit(1);
        }
        System.out.println("File received!");
    }

    private static void getDataFromPacket(DataPacket responsePacket) {

        if (retryCount > 0) {
            if (responsePacket.getBlock() < block + 1) {
                retryCount--;
                System.out.println("Got again block #" + block);
                sendAck(block);
                if (!responsePacket.isLast()) {
                    getReceiveResponse();
                }
            } else {
                if (responsePacket.getBlock() == block + 1) {
                    retryCount = 4;
                    data.addAll(responsePacket.getPacketData());
                    block = (short) (block + 1);
                    System.out.println("1st Got block #" + block);
                }
                sendAck(responsePacket.getBlock());
                if (!responsePacket.isLast()) {
                    getReceiveResponse();
                }
            }
        } else {
            System.err.println("Reached maximum retries..");
            System.exit(1);
        }
    }

    private static void sendAck(short block) {
        AckPacket pck = new AckPacket(block);
        DatagramPacket datagramToSend = new DatagramPacket(pck.getPacket(),
                AckPacket.PACKETLENGTH, ip, tid);
        System.out.println("Sending ACK #" + block);
        try {
            socket.send(datagramToSend);
        } catch (IOException e) {
            System.err.println("Failed to send ACK number " + pck.getBlock() +". " + e.getMessage());
            System.exit(1);
        }
    }

    private static void canSaveFile() {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.close();
        } catch (IOException e) {
            System.err.println("Cannot write file. " + e.getMessage());
            System.exit(1);
        }
    }
}
