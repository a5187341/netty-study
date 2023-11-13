package com.test.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

/**
 * 服务端
 *
 * @author lifangao
 * @date 2023/11/12 01:16
 **/
public class MyServer01 {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(8000));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            if (iterator.hasNext()) {
                SelectionKey ssckey = iterator.next();
                iterator.remove();

                if (ssckey.isAcceptable()) {
                    SocketChannel sc = serverSocketChannel.accept();
                    sc.configureBlocking(false);
                    SelectionKey sckey = sc.register(selector, SelectionKey.OP_READ);

                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < 2000000; i++) {
                        stringBuffer.append("S");
                    }

                    ByteBuffer buffer = Charset.defaultCharset().encode(stringBuffer.toString());

                    //直接第一次写
                    int write = sc.write(buffer);
                    sckey.interestOps(sckey.interestOps() + SelectionKey.OP_WRITE);
                    sckey.attach(buffer);
                    System.out.println("write = " + write);
                    
                }else if (ssckey.isWritable()) {
                    ByteBuffer buffer = (ByteBuffer) ssckey.attachment();
                    SocketChannel channel = (SocketChannel) ssckey.channel();
                    if (buffer.hasRemaining()){
                        int write = channel.write(buffer);
                        System.out.println("write = " + write);
                    }
                }

            }
        }

    }
}
