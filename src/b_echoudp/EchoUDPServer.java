package b_echoudp;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class EchoUDPServer {
    public static final int PORT = 3500;
    private DatagramSocket serverSideSocket; //Se instancia en init() con el port como parámetro.
    // Datagram Socket puede solicitar información mediante el método recieve(), lo que congela el hilo
    // hasta recibir por completo el mensaje dentro de buffer.
    private InetAddress clientIPAddress; // Es un IP. La clase no posee constructores, solo métodos estáticos.
    private int clientPort;


    public static void main(String args[]) throws Exception {
        EchoUDPServer es = new EchoUDPServer();
        es.init();
    }


    public EchoUDPServer() throws Exception {
        System.out.println("Echo UDP server is running on port: " + PORT);
    }


    public void init() throws Exception {
        serverSideSocket = new DatagramSocket(PORT); // A comparación del Client, el Server necesita saber
        // desde qué socket estar recibiendo los Datagramas. (Client does not use PORT as parameter in DatagramSocket constructor).
        while (true) {
            protocol();
        }
    }


    public void protocol() throws Exception {
        String message = receive(); // No need for casting, receive(String str) already returns a String.
        System.out.println("[Server] From client: " + message);
        String answer = message;
        send(answer);
    }


    private void send(String messageToSend) throws Exception {
        byte[] bufferToSend = messageToSend.getBytes(); //Turns the message into an array of bytes.
        DatagramPacket packetToSend = new DatagramPacket
                (bufferToSend, bufferToSend.length, clientIPAddress, clientPort); // In theory, the message cannot be larger than 1500 bytes. -> According to the UPV
        serverSideSocket.send(packetToSend);
    }


    private String receive() throws Exception {
        byte[] bufferToReceive = new byte[1024];
        DatagramPacket packetToReceive = new DatagramPacket(bufferToReceive, bufferToReceive.length);
        serverSideSocket.receive(packetToReceive); //El método receive() bloquea el hilo hasta que recibe un paquete.
        //Tambien, al ser guardada toda la info dentro del Datagrama se incluye la direccion IP y el puerto del cliente.
        //Es por eso que en las siguientes dos lineas se obtienen esos datos aunque al principio solo había un array de bytes limpios.

        clientIPAddress = packetToReceive.getAddress();
        clientPort = packetToReceive.getPort();
        /*Explicación de la siguiente linea. Se desea crear un String, pero existe un problema,
        el array de bytes no solo contiene la información del String (mensaje), sino que tambien contiene la dirección IP del
        cliente, como tambien el puerto de su conexion, por lo cual, debemos construir el String sabiendo desde donde empezar
        y terminar de leer, por lo cual se dan 3 parametros, el buffer, byte de inicio y byte de llegada.

        El método getLength() de la clase DatagramPacket retorna unicamente el tamaño DEL MENSAJE, y no incluye a
        dicho tamaño la info extra (IP y numero del Socket).
        */
        String receivedMessage = new String(packetToReceive.getData(), 0, packetToReceive.getLength());



        return receivedMessage;
    }



}
