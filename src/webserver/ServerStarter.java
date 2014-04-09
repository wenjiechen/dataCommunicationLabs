package webserver;

public class ServerStarter {
  private static final int RUN_TIME = 600;
  private static int port = 9000;

  public static void main(String[] args) {
    MultiThreadedServer server = new MultiThreadedServer(port);
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
