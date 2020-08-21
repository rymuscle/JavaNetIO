package com.javaio.study.nio.iomultiplexing;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) {
        try {
            // 1. 获取通道 （其实这里的通道类似于之前的 socket连接）
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            // 2. 切换非阻塞模式
            serverSocket.configureBlocking(false);
            // 3. 绑定连接
            serverSocket.bind(new InetSocketAddress("127.0.0.1", 9091));

            // 4. 获取选择器
            Selector selector = Selector.open();
            // 5. 将通道注册到选择器，并指定"监听接收事件"为accept
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            // 6. 轮询式地获取选择器上已经准备就绪的事件
            while (true) {
                System.out.println("selector.select() ： IO多路复用对应用程序阻塞");
                // IO多路复用整体对用户空间进程来说，依然是阻塞的. 因其只不过其在内核中对 非阻塞客户端socket 做了轮询
                selector.select(); // 阻塞等待就绪的Channel，这是关键点之一

                System.out.println("selector.select() ： IO多路复用已有事件准备好");
                // 7. 获取当前选择器中所有注册的"选择键(已就绪的监听事件)"
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    // 8. 获取就绪事件
                    SelectionKey key = iter.next();

                    // 9. 判断具体是什么事件，如果客户端连接上来了，那就将对其的读事件注册进IO多路复用器中
                    if (key.isAcceptable()) {
                        // 10. 如果是接收就绪，则获取客户端socket通道
                        SocketChannel clientSocketChannel = serverSocket.accept();
                        // 11. 将客户端socket通道设置为非阻塞
                        clientSocketChannel.configureBlocking(false);
                        // 12. 将该通道的读事件注册到选择器上
                        clientSocketChannel.register(selector, SelectionKey.OP_READ);

                    } else if (key.isReadable()) { // 如果读事件就绪
                        // 13. 获取当前选择器上就绪的通道
                        SocketChannel clientSocketReadChannel = (SocketChannel) key.channel();
                        // 14. 读取数据
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        int len = 0;
                        while ((len = clientSocketReadChannel.read(buf)) > 0) { // 这里的读其实是将客户端数据 写入到Buffer
                            buf.flip();
                            System.out.println(new String(buf.array(), 0, len));
                            buf.clear();    // 如果一次没有读完，需要将 buffer的position归0，才能继读取去客户端数据写入到buffer
                        }
                    }
                    // 15. 如果没有连接上来，也没有数据，则移除客户端的句柄
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
