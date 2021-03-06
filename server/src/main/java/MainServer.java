import DataBase.DataBaseList;

public class MainServer {
    public static void main(String[] args) throws Exception {
        NetWorkSerwer netWorkSerwer = new NetWorkSerwer(8189);
        DataBaseList.getUserList();
        netWorkSerwer.run();
    }
}
