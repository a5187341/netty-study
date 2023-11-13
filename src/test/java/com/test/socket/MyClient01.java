package com.test.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 客户端
 *
 * @author lifangao
 * @date 2023/11/12 01:16
 **/
public class MyClient01 {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8000));

        int read = 0;

        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            read += socketChannel.read(buffer);
            System.out.println("read = " + read);
            buffer.clear();
        }
    }
}
