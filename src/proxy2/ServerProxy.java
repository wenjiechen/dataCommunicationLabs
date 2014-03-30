package proxy2;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

class ActionSocket extends Thread {
  private Socket socket = null;

  public ActionSocket(Socket s) {
    this.socket = s;
  }

  public void run() {
    try {
      this.action();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void action() throws Exception {
    if (this.socket == null) {
      return;
    }
    int count = 0;
    InputStream inputStream = this.socket.getInputStream();
    while (count == 0)
      count = inputStream.available();
    byte[] b = new byte[count];
    inputStream.read(b);
    String str = new String(b);
    String[] strs = str.split("\\n");
    System.out.println(Arrays.toString(strs));
  }
}

public class ServerProxy {
  public static void main(String args[]) throws Exception {
    System.out.println("test");
    ServerSocket server = new ServerSocket(9000);
    while (true) {
      Socket socket = server.accept();
      ActionSocket ap = new ActionSocket(socket);
      ap.start();
    }
  }
}