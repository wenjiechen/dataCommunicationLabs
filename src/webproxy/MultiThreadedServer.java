package webproxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedServer implements Runnable {

  protected int serverPort = 8080;
  protected ServerSocket serverSocket = null;
  protected boolean isStopped = false;
  protected Thread runningThread = null;
  protected static final int NUM_THREAD = 10;
  protected ExecutorService threadPool = Executors
      .newFixedThreadPool(NUM_THREAD);

  public MultiThreadedServer(int port) {
    this.serverPort = port;
  }

  public void run() {
    synchronized (this) {
      runningThread = Thread.currentThread();
    }
    openServerSocket();
    System.out.println("Server get started.");
    while (!isStopped()) {
      Socket clientSocket = null;
      try {
        clientSocket = serverSocket.accept();
      } catch (IOException e) {
        if (isStopped()) {
          System.out.println("Server Stopped.");
          return;
        }
        throw new RuntimeException("Error accepting client connection", e);
      }
//      threadPool.execute(new WorkerRunnable(clientSocket,
//          "Thread Pooled Server"));
      new Thread(new WorkerRunnable(clientSocket,"Thread")).start();
    }
    threadPool.shutdown();
    System.out.println("Server Stopped.");
  }

  private synchronized boolean isStopped() {
    return isStopped;
  }

  public synchronized void stop() {
    isStopped = true;
    try {
      serverSocket.close();
    } catch (IOException e) {
      throw new RuntimeException("Error closing server", e);
    }
  }

  private void openServerSocket() {
    try {
      serverSocket = new ServerSocket(serverPort);
    } catch (IOException e) {
      throw new RuntimeException("Cannot open port" + serverPort, e);
    }
  }
}