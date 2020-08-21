package com.javaio.study.nio.chapter02;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class BIOClient1 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9090);
        // 客户端不是直接发送数据，而是等待用户输入。服务器不会立即接收到客户端的数据，而是需要阻塞等待
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("请输入信息，并按回车发送给服务端：");
            String next = scanner.next();
            socket.getOutputStream().write(next.getBytes());
            System.out.println("发送成功!");
        }
    }
}