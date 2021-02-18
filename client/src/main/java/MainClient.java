import java.util.concurrent.CountDownLatch;

public class MainClient {

    public static void main(String[] args) {
        CountDownLatch networkStarter = new CountDownLatch(1);
        new Thread(() -> Network.getInstance().start(networkStarter)).start();
        ClientControl clientControl = new ClientControl();
        clientControl.start();
    }
}