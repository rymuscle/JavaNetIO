package com.javaio.study.nio.chapter03;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class BIOClient2 {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9090));

        Scanner scanner = new Scanner(System.in);
        ByteBuffer buf = ByteBuffer.allocate(1024);

        while(true) {
            if (socketChannel.finishConnect()) {
                System.out.println("请输入信息，并按回车发送给服务端：");
                String next = scanner.next();
                buf.put(next.getBytes());
                buf.flip();
                socketChannel.write(buf);
                buf.clear();
                System.out.println("发送成功!");
            }
        }
    }
}
