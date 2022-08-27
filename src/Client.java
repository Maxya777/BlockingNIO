import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client extends Thread {

    @Override
    public void run() {
        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 23334);
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert socketChannel != null;
            socketChannel.connect(socketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

            String msg;

            while (true) {
                System.out.println("Напишите своё сообщение серверу ");
                msg = scanner.nextLine();
                if ("end".equals(msg)) break;

                socketChannel.write(
                        ByteBuffer.wrap(
                                msg.getBytes(StandardCharsets.UTF_8)));

                Thread.sleep(2000);

                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }

        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
