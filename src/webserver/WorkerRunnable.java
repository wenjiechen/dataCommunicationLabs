package webserver;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

/**

 */
public class WorkerRunnable implements Runnable {

  protected Socket clientSocket = null;
  protected String serverText = null;

  public WorkerRunnable(Socket clientSocket, String serverText) {
    this.clientSocket = clientSocket;
    this.serverText = serverText;
  }

  public void run() {
    try {
      InputStream input = clientSocket.getInputStream();
      OutputStream output = clientSocket.getOutputStream();

      // response the client
      Date now = new Date();
      DateFormat dataFormater = DateFormat.getDateTimeInstance();
      String date = dataFormater.format(now);
      String path = "testFiles\\index2.html";
      String responseContent = new Scanner(new File(path)).useDelimiter("\\Z")
          .next();

      output.write(("HTTP/1.1 200 OK\n\n" + responseContent).getBytes());
      output.close();
      input.close();
      System.out.println("Request processed: " + date);
    } catch (IOException e) {
      // report exception somewhere.
      e.printStackTrace();
    }
  }
}