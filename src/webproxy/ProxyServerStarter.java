package webproxy;

public class ProxyServerStarter {
  private static final int RUN_TIME = 600;

  public static void main(String[] args) {
    int localPort;
    if (args.length < 1)
      localPort = 9000;
    else
      localPort = Integer.valueOf(args[0]);
    boolean useBufferReader = false;
    String threadType = "multithread";
    ProxyServer server = new ProxyServer(localPort, threadType, useBufferReader);
    new Thread(server).start();

    try {
      Thread.sleep(RUN_TIME * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Stopping Proxy Server");
    server.stop();
  }
}
