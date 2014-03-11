package webserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

public class SingleThreadedServer implements Runnable {

  protected int serverPort = 8080;
  protected ServerSocket serverSocket = null;
  protected boolean isStopped = false;
  protected Thread runningThread = null;

  public SingleThreadedServer(int port) {
    this.serverPort = port;
  }

  public void run() {
    synchronized (this) {
      this.runningThread = Thread.currentThread();
    }
    openServerSocket();

    while (!isStopped()) {
      Socket clientSocket = null;
      try {
        clientSocket = this.serverSocket.accept();
      } catch (IOException e) {
        if (isStopped()) {
          System.out.println("Server Stopped.");
          return;
        }
        throw new RuntimeException("Error accepting client connection", e);
      }
      try {
        processClientRequest(clientSocket);
      } catch (IOException e) {
        // log exception and go on to next request.

      }
    }

    System.out.println("Server Stopped.");
  }

  private void processClientRequest(Socket clientSocket) throws IOException {
    InputStream input = clientSocket.getInputStream();
    OutputStream output = clientSocket.getOutputStream();

    // response the client
    Date now = new Date();
    DateFormat dataFormater = DateFormat.getDateTimeInstance();
    String date = dataFormater.format(now);
    String responseContent = new Scanner(new File("testFiles\\index2.html"))
        .useDelimiter("\\Z").next();

    output.write(("HTTP/1.1 200 OK\n\n" + responseContent).getBytes());
    output.close();
    input.close();
    System.out.println("Request processed: " + date);
  }

  private synchronized boolean isStopped() {
    return this.isStopped;
  }

  public synchronized void stop() {
    this.isStopped = true;
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      throw new RuntimeException("Error closing server", e);
    }
  }

  private void openServerSocket() {
    try {
      this.serverSocket = new ServerSocket(this.serverPort);
    } catch (IOException e) {
      throw new RuntimeException("Cannot open port 8080", e);
    }
  }
}