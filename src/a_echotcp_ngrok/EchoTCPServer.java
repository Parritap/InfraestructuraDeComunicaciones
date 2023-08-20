package a_echotcp_ngrok;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

public class EchoTCPServer {
    public static final int PORT = 16288;
    private ServerSocket listener;
    private Socket serverSideSocket;
    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;
    public EchoTCPServer() {
        System.out.println("Echo TCP server is running on port: " + PORT);
    }
    public void init() throws Exception {
        listener = new ServerSocket(PORT);
        serverSideSocket = listener.accept(); //Blocks the program until a client connects.
        System.out.println("CLIENT CONNECTED");
        createStreams(serverSideSocket);
        System.out.println("STREAMS CREATED");
        while (!serverSideSocket.isClosed()) {
            protocol(serverSideSocket);
        }
    }
    public void protocol(Socket socket) throws Exception {
        String message = fromNetwork.readLine();

        if (message == null) {
            System.out.println("[Server] Client has closed the connection.");
            serverSideSocket.close();
            return;
        }
        System.out.println("[Server] From client: " + message);
        String answer = message;
        toNetwork.println(answer);
    }
    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    public static void main(String args[]) throws Exception {
        EchoTCPServer es = new EchoTCPServer();
        es.init();}}
