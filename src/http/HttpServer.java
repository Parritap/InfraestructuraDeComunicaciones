package http;

import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

public class HttpServer {

    private static final int PORT = 8080;
    private final String contextPath = "src/http";

    private ServerSocket serverSocket;

    private Socket socket;

    private PrintWriter toNetwork;

    private BufferedReader fromNetwork;


    public HttpServer() {
        System.out.println("The web server is running on port " + PORT);
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
            System.out.println("Streams Created!");
            StringBuilder message = new StringBuilder(fromNetwork.readLine());
            System.out.println("FROM BROWSER:\r\n");
            String line;
            while ((line = fromNetwork.readLine()) != null && !(line).isEmpty()) {
                message.append(line).append("\r\n");
            }
            System.out.println(message);
            protocol(message.toString());
            socket.close();
        }
    }

    private void protocol(String message) {
        System.out.println(message);
        String[] lineaSolicitud = message.split(" ");
        String method = lineaSolicitud[0];
        String url = lineaSolicitud[1];
        String httpVersion = lineaSolicitud[2];

        switch (method) {
            case "GET":
                respondGET(url, httpVersion);
            case "POST":
                respondePOST();
        }
    }

    private void respondGET(String url, String httpVersion) {
        File askedResource = new File(this.contextPath + url);
        if (askedResource.exists()) {
            if (Utils.isTextFile(askedResource)) respondGetTxt(askedResource, httpVersion);
            else respondGetBinary(askedResource, httpVersion);
        } else { // RESOURCE NOT FOUND
            StringBuilder res = new StringBuilder();
            res.append("404 Not Found\r\n");
            res.append("Server: Apache");
            res.append("Date: ").append(Utils.getCurrentDate()).append("\r\n");
            res.append("Content-Type: text/html");
            res.append("Content-Length: ").append(askedResource.length());
            res.append("Cache-Control: no-cache");
            res.append("\r\n\r\n");
            toNetwork.println(res);
        }

    }

    private void respondGetBinary(File askedResource, String httpVersion) {
        StringBuilder res = new StringBuilder(httpVersion).append(" ");
        res.append("200 OK\r\n");
        res.append("Server: Apache\r\n");
        res.append("Date: ").append(Utils.getCurrentDate()).append("\r\n");
        res.append("Content-Type: image/png\r\n");
        res.append("Content-Length: ").append(askedResource.length()).append("\r\n");
        res.append("Cache-Control: no-cache\r\n");
        res.append("\r\n");
        this.toNetwork.println(res);
        Utils.sendFile(askedResource.getPath(), socket);

        //ENVIAR EL ARCHIVO BINARIO AQUÍ! <------------------
    }

    private void respondGetTxt(File askedResource, String httpVersion) {
        StringBuilder res = new StringBuilder(httpVersion).append(" ");
        res.append("200 OK\r\n");
        res.append("Server: Apache\r\n");
        res.append("Date: ").append(Utils.getCurrentDate()).append("\r\n");
        res.append("Content-Type: text/html\r\n");
        res.append("Content-Length: ").append(askedResource.length()).append("\r\n");
        res.append("Cache-Control: no-cache\r\n");
        res.append("\r\n");
        toNetwork.println(res);
        toNetwork.println(Utils.fileToString(askedResource));

    }


    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void respondePOST() {
    }
}