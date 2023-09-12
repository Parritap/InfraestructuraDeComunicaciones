package http;

import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Map;

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
            System.out.println("FROM BROWSER:\r\n" + message);
            protocol(message);
            socket.close();
            reqResMessage.limpiarMensaje();
        }
    }

    private void protocol(String message) {

        System.out.println(message);
        String[] lineaSolicitud = message.split(" ");
        reqResMessage.requestLine.putAll(
                Map.of(
                        "httpMethod", lineaSolicitud[0],
                        "resource", lineaSolicitud[1],
                        "httpVersion", lineaSolicitud[2]
                )
        );

        switch (reqResMessage.requestLine.get("httpMethod")) {
            case "GET":
                respondGET(reqResMessage.requestLine.get("resource"));
                break;
            default:
                System.out.println("Method not supported");
        }
    }

    private void respondGET(String url) {
        File file = new File(this.contextPath + url);
        StringBuilder res = new StringBuilder();
        if (file.exists()) {

            reqResMessage.responseLine.replace("httpStatusCode", "200");
            reqResMessage.responseLine.replace("httpStatusMessage", "OK");
            reqResMessage.responseHeaders.replace("Date:", new java.util.Date().toString());
            reqResMessage.responseHeaders.replace("Last-Modified:", new java.util.Date(file.lastModified()).toString());
            reqResMessage.responseHeaders.replace("Content-Length:", String.valueOf(file.length()));

            if (!Utils.isImage(url)) {

                reqResMessage.responseHeaders.replace("Content-Type:", "text/html");
                sendResponseAndCharge(constructResponse(res));
                sendTextFile(file);

            } else if (Utils.isImage(url)) {

                reqResMessage.responseHeaders.replace("Content-Type:", "image/png");
                reqResMessage.responseHeaders.replace("Accept-Ranges:", "bytes");
                sendResponseAndCharge(constructResponse(res));
                Utils.sendFile(file, socket);

            } else {
                reqResMessage.responseLine.replace("httpStatusMessage", "File founded but no charged");
            }
        } else {

            reqResMessage.responseLine.replace("httpStatusCode", "404");
            reqResMessage.responseLine.replace("httpStatusMessage", "Not Found");
            reqResMessage.responseHeaders.replace("Date:", new java.util.Date().toString());
            reqResMessage.responseHeaders.replace("Content-Length:", String.valueOf(file.length()));
            reqResMessage.responseHeaders.replace("Cache-Control:", "no-cache");
            reqResMessage.responseHeaders.replace("Content-Type:", "text/html");

            sendResponse(constructResponse(res));
        }

    }

    private String constructResponse(StringBuilder res) {

        res.append(
                String.join(" ", reqResMessage.responseLine.values())
        ).append("\n").append(
                reqResMessage.responseHeaders.keySet().stream()
                        .filter(key -> reqResMessage.responseHeaders.get(key) != null)
                        .reduce("", (acumulador, key) -> acumulador + "%s %s%n".formatted(
                                key, reqResMessage.responseHeaders.get(key)
                        ))
        ).append("\n");

        return res.toString();

    }

    private void sendResponseAndCharge(String res) {
        toNetwork.print(res);
        toNetwork.flush();
    }

    private void sendResponse(String res) {
        toNetwork.print(res);
    }

    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void sendTextFile(File file) {
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