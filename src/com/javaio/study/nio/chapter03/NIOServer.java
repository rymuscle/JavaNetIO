package com.javaio.study.nio.chapter03;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class NIOServer {
    public static List<SocketChannel> socketChannelList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 9090);
        serverSocketChannel.bind(socketAddress);
        // 设置非阻塞(保证 accept() 为非阻塞)
        serverSocketChannel.configureBlocking(false);


        while(true) {
            // 此处已经非阻塞了
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                // 将接客户端socket连接也设置为非阻塞(接下来的read就非阻塞了)
                socketChannel.configureBlocking(false);
                socketChannelList.add(socketChannel);
                System.out.println("客户端" +  socketChannelList.size() + "连接成功");
            }

            if (socketChannelList != null && !socketChannelList.isEmpty()) {
                for (SocketChannel socketChannelVal : socketChannelList) {
                    ByteBuffer byteBuffers = ByteBuffer.allocate(1024);
                    // 此处也已经非阻塞了。这里的 read 其实是将客户端数据 写入到Buffer
                    int read = socketChannelVal.read(byteBuffers);

                    if(read > 0) {
                        byteBuffers.flip();
                        byte[] bytes = new byte[read];
                        byteBuffers.get(bytes);
                        System.out.println("收到数据:" + new String(bytes));
                        // 如果一次没有读完，需要将 buffer的position归0，才能继读取去客户端数据写入到buffer
                        byteBuffers.clear();
                    } else if (read == -1) {
                        socketChannelList.remove(socketChannelVal);
                        System.out.println("客户端断开");
                        break;  // 不然remove会报错
                    }
                }
            }
        }
    }
}
