import java.util.concurrent.CountDownLatch;

public class MainClient {

    public static void main(String[] args) {
        CountDownLatch networkStarter = new CountDownLatch(1);
        new Thread(() -> Network.getInstance().start(networkStarter)).start();
        ClientControl clientControl = new ClientControl();
        clientControl.start();


        /*try {
            ProtoFileSender.sendFile(Paths.get("SourceTest/demo.txt"), Network.getInstance().getCurrentChannel(), future -> {
                if (!future.isSuccess()) {
                    future.cause().printStackTrace();
                }
                if (future.isSuccess()) {
                    System.out.println("Файл успешно передан");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*ProtoFileSender.sendReQuestFile("SourceTest/demo.txt", Network.getInstance().getCurrentChannel());*/
    }
}