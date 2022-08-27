public class Main {

    public static void main(String[] args) {

        Thread client = new Client();
        Thread server = new Server();
        client.start();
        server.start();
    }
}
