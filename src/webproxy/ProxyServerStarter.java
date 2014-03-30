package webproxy;

public class ProxyServerStarter {
  private static final int RUN_TIME = 600;

  public static void main(String[] args) {
    int localPort = 9000;
    String proxyIp = "91.210.44.196";
    int proxyPort = 3129;
    ProxyThreadPoolServer server = new ProxyThreadPoolServer(localPort,
        proxyIp, proxyPort);
    new Thread(server).start();

    try {
      Thread.sleep(RUN_TIME * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Stopping Server");
    server.stop();
  }
}
