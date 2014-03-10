package wenjie.chen.nyu.webserver;

public class StartServer {

  public static void main(String[] args) {
    SingleThreadedServer server = new SingleThreadedServer(9000);
    new Thread(server).start();

    try {
      Thread.sleep(20 * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Stopping Server");
    server.stop();
  }
}