package com.javaio.study.bio.chapter03;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9090);

        while(true) {
            System.out.println("服务端阻塞等待客户端连接...");
            Socket socket = serverSocket.accept();

            System.out.println("客户端连接成功，服务器为其创建子线程");
            new Thread(() -> {
                while(true) {   // 内部不断检测客户端socket是否有数据

                    // 检测客户端是否断开
                    try {
                        socket.sendUrgentData(0xFF);
                    } catch (IOException e) {
                        System.out.println("线程" + Thread.currentThread().getName() + "的客户端已断开!");
                        return;
                    }

                    // 在线程内部读取客户端发送来的数据
                    try {
                        byte[] buf = new byte[1024];
                        System.out.println("线程" + Thread.currentThread().getName() + "阻塞等待客户端数据...");
                        socket.getInputStream().read(buf);
                        System.out.println("线程" + Thread.currentThread().getName() + "收到数据:" + new String(buf));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
