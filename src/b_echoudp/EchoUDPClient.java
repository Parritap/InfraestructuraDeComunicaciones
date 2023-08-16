package b_echoudp;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class EchoUDPClient {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static final String SERVER ="localhost";
    public static final int PORT = 3500;



    public EchoUDPClient() {
        System.out.println("Echo UDP Client running on port: " + PORT);
    }


    public void protocol() throws Exception {
        DatagramSocket clientSideSocket = new DatagramSocket();

        System.out.print("Ingrese un mensaje: ");
        String fromUser = SCANNER.nextLine();

        send(fromUser, clientSideSocket);

        String fromServer = (String) receive(clientSideSocket);
        System.out.println("[Client] From server: " + fromServer);

        clientSideSocket.close();
    }


    private void send(String messageToSend, DatagramSocket socket) throws Exception {
        byte[] ba = messageToSend.getBytes();

        InetAddress serverIPAddress = InetAddress.getByName(SERVER);
        DatagramPacket packetToSend = new DatagramPacket(ba, ba.length, serverIPAddress, PORT);
        socket.send(packetToSend);
    }


    private String receive(DatagramSocket socket) throws Exception {
        byte[] bufferToReceive = new byte [ 1024 ];
        DatagramPacket packetToReceive = new DatagramPacket(bufferToReceive, bufferToReceive.length);
        socket.receive(packetToReceive);
        String receivedMessage = new String(packetToReceive.getData(), 0, packetToReceive.getLength());


        return receivedMessage;
    }


    public static void main(String args[]) throws Exception {
        EchoUDPClient ec = new EchoUDPClient();
        ec.protocol();
    }
}
