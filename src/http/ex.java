package http;

import java.io.File;

public class ex {
    public static void main(String[] args) {
        File x = new File("src/HTTP/index.html");
        System.out.println(x.exists());
    }
}