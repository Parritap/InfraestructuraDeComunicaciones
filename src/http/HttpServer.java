package http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * <h1>Autores: Juan Esteban Parra Parra </h1>
 * <h1>         Juan Esteban Castaño Osma</h1>
 */

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
            HttpMessage.limpiarMensajes();
        }
    }

    private void protocol(String message) {

        System.out.println(message);
        String[] lineaSolicitud = message.split(" ");
        HttpMessage.requestLine.putAll(
                Map.of(
                        "httpMethod", lineaSolicitud[0],
                        "resource", lineaSolicitud[1],
                        "httpVersion", lineaSolicitud[2]
                )
        );

        switch (HttpMessage.requestLine.get("httpMethod")) {
            case "GET":
                respondGET(HttpMessage.requestLine.get("resource"));
                break;
            default:
                System.out.println("Method not supported");
        }
    }

    private void respondGET(String url) {
        File file = new File(this.contextPath + url);
        StringBuilder res = new StringBuilder();
        if (file.exists()) {

            HttpMessage.responseLine.replace("httpStatusCode", "200");
            HttpMessage.responseLine.replace("httpStatusMessage", "OK");
            HttpMessage.responseHeaders.replace("Date:", new java.util.Date().toString());
            HttpMessage.responseHeaders.replace("Last-Modified:", new java.util.Date(file.lastModified()).toString());
            HttpMessage.responseHeaders.replace("Content-Length:", String.valueOf(file.length()));

            if (!Utils.isImage(url)) {

                HttpMessage.responseHeaders.replace("Content-Type:", "text/html");
                sendResponseAndFlush(constructResponse(res));
                sendTextFile(file);

            } else if (Utils.isImage(url)) {

                HttpMessage.responseHeaders.replace("Content-Type:", "image/png");
                HttpMessage.responseHeaders.replace("Accept-Ranges:", "bytes");
                sendResponseAndFlush(constructResponse(res));
                Utils.sendFile(file, socket);

            } else {
                HttpMessage.responseLine.replace("httpStatusMessage", "File founded but no charged");
            }
        } else {

            HttpMessage.responseLine.replace("httpStatusCode", "404");
            HttpMessage.responseLine.replace("httpStatusMessage", "Not Found");
            HttpMessage.responseHeaders.replace("Date:", new java.util.Date().toString());
            HttpMessage.responseHeaders.replace("Content-Length:", String.valueOf(file.length()));
            HttpMessage.responseHeaders.replace("Cache-Control:", "no-cache");
            HttpMessage.responseHeaders.replace("Content-Type:", "text/html");

            sendResponse(constructResponse(res));
        }

    }

    private String constructResponse(StringBuilder res) {

        res.append(
                String.join(" ", HttpMessage.responseLine.values())
        ).append("\n").append(
                HttpMessage.responseHeaders.keySet().stream()
                        .filter(key -> HttpMessage.responseHeaders.get(key) != null)
                        .reduce("", (acumulador, key) -> acumulador + "%s %s%n".formatted(
                                key, HttpMessage.responseHeaders.get(key)
                        ))
        ).append("\n");

        return res.toString();

    }

    private void sendResponseAndFlush(String res) {
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