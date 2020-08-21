package com.javaio.study.bio.chapter02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9090);

        while(true) {
            System.out.println("服务端阻塞等待客户端连接...");
            Socket socket = serverSocket.accept();

            byte[] buf = new byte[1024];
            System.out.println("服务端阻塞等待客户端数据...");
            socket.getInputStream().read(buf);
            System.out.println(new String(buf));
        }
    }
}
