package com.javaio.study.bio.chapter01;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9090);
        while(true) {
            System.out.println("服务端阻塞等待客户端连接...");
            Socket socket = serverSocket.accept();

            // 接收并打印客户端发来的数据
            byte[] buf = new byte[1024];
            socket.getInputStream().read(buf);
            System.out.println(new String(buf));
        }
    }
}
