package HTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private static final int PORT = 6666;

    private ServerSocket serverSocket;

    private Socket socket;

    private PrintWriter toNetwork;

    private BufferedReader fromNetwork;


    public HttpServer() {
        System.out.println(" The web server is running on port " + PORT);
    }


    public static void main(String[] args) throws Exception {
        HttpServer server = new HttpServer();
        server.init();
    }

    public void init() throws Exception {
        serverSocket = new ServerSocket(PORT);
        while (true) {
            socket = serverSocket.accept();
            createStreams(socket);
            String message = fromNetwork.readLine();

            while (message != null && !message.isEmpty()) {
                System.out.println(message);
                message = fromNetwork.readLine();
            }
            protocol(message);

            socket.close();

        }
    }

    private void protocol(String message) {
        String[] lineaSolicitud = (message.split("\n"))[0].split(" ");
        String method = lineaSolicitud[0];
        String URL = lineaSolicitud[1];



    }


    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }


}