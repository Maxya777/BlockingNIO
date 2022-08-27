import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server extends Thread {

    @Override
    public void run() {
        ServerSocketChannel serverChannel = null;
        try {
            serverChannel = ServerSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert serverChannel != null;
            serverChannel.bind(new InetSocketAddress("localhost", 23334));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()) {

                    int bytesCount = socketChannel.read(inputBuffer);

                    final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    System.out.println("Получено сообщение от клиента: " + msg);

                    socketChannel.write(ByteBuffer
                            .wrap(("Сейчас будет пранк: " + msg.replaceAll("\\s+", ""))
                                    .getBytes(StandardCharsets.UTF_8)));
                }
            } catch (IOException i) {
                System.out.println(i.getMessage());
            }
        }
    }
}
