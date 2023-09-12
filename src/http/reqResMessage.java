package http;

import java.util.HashMap;

public class reqResMessage {

    public static HashMap<String, String> requestLine = new HashMap<>() {{
        put("httpMethod", null);
        put("httpVersion", null);
        put("resource", null);
    }};
    public static HashMap<String, String> requestHeaders = new HashMap<>() {{
        put("Host:", null);
        put("Upgrade-Insecure-Requests:", null);
        put("Accept:", null);
        put("User-Agent:", null);
        put("Accept-Language:", null);
        put("Accept-Encoding:", null);
        put("Connection:", null);
    }};
    public static HashMap<String, String> responseLine = new HashMap<>() {{
        put("httpVersion", "HTTP/1.1");
        put("httpStatusCode", null);
        put("httpStatusMessage", null);
    }};
    public static HashMap<String, String> responseHeaders = new HashMap<>() {{
        put("Server:", "Juan's Web Server");
        put("Date:", null);
        put("Last-Modified:", null);
        put("Cache-Control:", "max-age=3600, public");
        put("Content-Length:", null);
        put("Content-Type:", null);
        put("Connection:", "close");
        put("Accept-Ranges:", null);
    }};

    public static void limpiarMensaje() {
        requestLine.keySet().forEach(e -> requestLine.replace(e, null));
        requestHeaders.keySet().forEach(e -> requestHeaders.replace(e, null));
        responseLine.keySet().stream().filter(e -> !e.equals("httpVersion"))
                .forEach(e -> responseLine.replace(e, null));
        responseHeaders.keySet().stream().filter(e -> !(e.equals("Server:") || e.equals("Cache-Control:") || e.equals("Connection")))
                .forEach(e -> responseHeaders.replace(e, null));
    }

}
