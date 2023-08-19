package c_users;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class EchoTCPClient {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static final String SERVER = "localhost";
    public static final int PORT = 3400;
    private PrintWriter toNetwork; //Esta clase convierte los datos primitivos en texto plano formateado. (Muy parecido a System.out.println
    // pero no necesariamente escribe en la consola). 
    private BufferedReader fromNetwork;

    private Socket clientSideSocket;

    public static void main(String args[]) throws Exception {
        EchoTCPClient ec = new EchoTCPClient();
        ec.init();
    }

    public EchoTCPClient() {
        System.out.println("Echo TCP client is running on port: " + PORT);
    }


    public void init() throws Exception {
        clientSideSocket = new Socket(SERVER, PORT); //Sockect to connect to the server.
        createStreams(clientSideSocket); //Genera los streams de datos que se compartirań entre servidor y cliente.
        protocol(clientSideSocket); //
        clientSideSocket.close();
    }

    /**
     * Este método pide un mensaje por consola, el que luego será escrito con la clase PrintWriter.
     *
     * @param socket Socket del cliente.
     * @throws Exception
     */
    public void protocol(Socket socket) throws Exception {
        System.out.print("Ingrese un mensaje: ");
        String fromUser = SCANNER.nextLine();
        toNetwork.println(fromUser);
        String fromServer = fromNetwork.readLine();
        System.out.println("[Client] From server: " + fromServer);
    }


    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }


}