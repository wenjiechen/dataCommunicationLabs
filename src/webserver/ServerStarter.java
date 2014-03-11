package webserver;

public class ServerStarter {
  private static final int RUN_TIME = 60;

  public static void main(String[] args) {
    MultiThreadedServer server = new MultiThreadedServer(9000);
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
