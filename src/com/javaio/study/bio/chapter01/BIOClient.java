package com.javaio.study.bio.chapter01;

import java.io.IOException;
import java.net.Socket;

public class BIOClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9090);
        socket.getOutputStream().write("Hello World!".getBytes());
        socket.close();
    }
}
