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
            String message = fromNetwork.readLine();
            System.out.println("FROM BROWSER:\r\n"+message);
            protocol(message);
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
            case "GET": respondGET(url, httpVersion);
            case "POST": respondePOST();
        }
    }

    private void respondGET(String url, String httpVersion) {
        File file = new File(this.contextPath + url);
        StringBuilder res = new StringBuilder(httpVersion).append(" ");
        if (file.exists()) {

            if (!Utils.isImage(url)) {
                res.append("HTTP/1.1 200 OK");
                res.append("Server:").append(socket.getLocalAddress().getHostName());
                res.append("Date:").append(new java.util.Date());
                res.append("Last-Modified:").append(new java.util.Date(file.lastModified()));

                res.append("Cache-Control: max-age=3600, public\r\n");
                res.append("Content-Length: ").append(file.length()).append("\r\n");
                res.append("Content-Type: text/html\r\n");
                res.append("Connection: close\r\n\r\n");
                toNetwork.print(res);
                toNetwork.flush();
                sendTextFile(file);
            } else{
                //if it is indeed an image then:
                res.append("HTTP/1.1 200 OK");
                res.append("Server:").append(socket.getLocalAddress().getHostName());
                res.append("Date:").append(new java.util.Date());
                res.append("Last-Modified:").append(new java.util.Date(file.lastModified()));

                res.append("Cache-Control: max-age=3600, public\r\n");
                res.append("Content-Type: image/png\r\n");
                res.append("Accept-Ranges: bytes\r\n");
                res.append("Content-Length: ").append(file.length()).append("\r\n");
                res.append("Connection: close\r\n\r\n");

                toNetwork.print(res);
                toNetwork.flush();
                Utils.sendFile(file, socket);

            }
        }
        else {
            res.append("404 Not Found\r\n");
            res.append("Server: Apache");
            res.append("Date: ").append(Utils.getCurrentDate()).append("\r\n");
            res.append("Content-Type: text/html");
            res.append("Content-Length: ").append(file.length());
            res.append("Cache-Control: no-cache");
            res.append("\r\n\r\n");
            toNetwork.println(res);
        }

    }


    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void respondePOST() {
    }


    private void sendTextFile (File file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Append each line to the StringBuilder
                this.toNetwork.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}