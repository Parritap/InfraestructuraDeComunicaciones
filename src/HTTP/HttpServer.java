package HTTP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private static final int PORT = 8000;

    private ServerSocket serverSocket;

    private Socket socket;

    private PrintWriter toNetwork;

    private BufferedReader fromNetwork;


    public HttpServer(){
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
            toNetwork.println("HTTP/1.1 200 OK");
            toNetwork.println("Content-Type: text/html");
            toNetwork.println("\r\n");
            toNetwork.println("<h1> Hello World </h1>");
            toNetwork.println("<p> This is a paragraph </p>");
            toNetwork.println("<p> This is another paragraph </p>");

            socket.close();

        }
    }


    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }



}