package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by liyuanchao on 3/13/18.
 */
public class TestNio {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        int[] ports = {1234, 5678};
        for (int port : ports) {
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.socket().bind(new InetSocketAddress("localhost", port));
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);
        }

        while (true) {
            if (selector.select(3000) == 0) {
                System.out.println("no ready connection");
                continue;
            }
            Iterator<SelectionKey> keyIt = selector.selectedKeys().iterator();
            while (keyIt.hasNext()) {
                SelectionKey key = keyIt.next();
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(32));
                }
                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    long bytes = socketChannel.read(buffer);
                    System.out.println(bytes);
                }
            }
            keyIt.remove();
        }
    }
}
