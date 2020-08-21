package com.javaio.study.nio.chapter02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 9090);
        serverSocketChannel.bind(socketAddress);
        // 仍然使用其阻塞模式
        serverSocketChannel.configureBlocking(true);

        while(true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            // 仍然使用其阻塞模式
            try {
                socketChannel.configureBlocking(false);

                ByteBuffer byteBuffers = ByteBuffer.allocate(1024);
                // 虽然是read, 其实是客户端数据写入buffer
                int read = read = socketChannel.read(byteBuffers);
                if(read > 0) {
                    byteBuffers.flip();
                    byte[] bytes = new byte[read];
                    byteBuffers.get(bytes);
                    System.out.println("线程" + Thread.currentThread().getName() + "收到数据:" + new String(bytes));
                    byteBuffers.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
