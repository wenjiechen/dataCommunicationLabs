package proxy2;

import java.net.*;
import java.io.*;

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
    BufferedReader br = new BufferedReader(new InputStreamReader(
        this.socket.getInputStream()));
    for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
      System.out.println(temp);
    }
    br.close();
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