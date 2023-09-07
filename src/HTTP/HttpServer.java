package HTTP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;

public class HttpServer {

    private static final int PORT = 10000;
    private final String contextPath = "src/HTTP/";

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
        //System.out.println(message);
        String[] lineaSolicitud = (message.split("\n"))[0].split(" ");
        for (String line : lineaSolicitud) System.out.println(line);
        String method = lineaSolicitud[0];
        String url = lineaSolicitud[1];
        String httpVersion = lineaSolicitud[2];

        switch (method) {
            case "GET": respondGET(url, httpVersion);
            case "POST": respondePOST();
        }
    }

    private void respondGET(String url, String httpVersion) {

        StringBuilder res = new StringBuilder(httpVersion).append(" ");
        File askedResource = new File(this.contextPath + url);
        File x = new File("src/HTTP/index.html");
        if (askedResource.exists()) {
            res.append("200 OK\r\n");
            res.append("Server: Apache\r\n");
            res.append("Date: ").append(Utils.getCurrentDate()).append("\r\n");
            res.append("Content-Type: text/html\r\n");
            res.append("Content-Length: ").append(askedResource.length()).append("\r\n");
            res.append("Cache-Control: no-cache\r\n");
            res.append("\r\n");

            try (BufferedReader reader = new BufferedReader(new FileReader(askedResource))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    res.append(line);
                }

            } catch (IOException e) {
                System.out.println("ERROR WHEN WRITING INTO THE RESPONSE");
            }
        }
        else {
            res.append("404 Not Found\r\n");
            res.append("Server: Apache");
            res.append("Date: ").append(Utils.getCurrentDate()).append("\r\n");
            res.append("Content-Type: text/html");
            res.append("Content-Length: ").append(askedResource.length());
            res.append("Cache-Control: no-cache");
            res.append("\r\n\r\n");
        }
        toNetwork.println(res);

    }


    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void respondePOST() {
    }


}