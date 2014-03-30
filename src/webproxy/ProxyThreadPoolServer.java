package webproxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyThreadPoolServer implements Runnable {

  private int serverPort = 8080;
  private ServerSocket serverSocket = null;
  private boolean isStopped = false;
  private Thread runningThread = null;
  private static final int NUM_THREAD = 10;
  private ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREAD);
  private String proxyIp;
  private int proxyPort;

  public ProxyThreadPoolServer(int port, String proxyIp, int proxyPort) {
    this.serverPort = port;
    this.proxyIp = proxyIp;
    this.proxyPort = proxyPort;
  }

  public void run() {
    synchronized (this) {
      runningThread = Thread.currentThread();
    }
    openServerSocket();
    System.out.println("Proxy Server get started./n the proxy ip is " + proxyIp
        + ":" + proxyPort);
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
      threadPool.execute(new WorkerRunnable(clientSocket, proxyIp, proxyPort));
      // new Thread(new WorkerRunnable(clientSocket, proxyIp,
      // proxyPort)).start();
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