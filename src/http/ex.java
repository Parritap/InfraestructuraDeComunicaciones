package http;

import jdk.jshell.execution.Util;

import java.io.File;

public class ex {
    public static void main(String[] args) {

        System.out.println(Utils.isTextFile("src/http/index.html"));
        System.out.println(Utils.isTextFile("src/http/favicon.ico"));
    }
}