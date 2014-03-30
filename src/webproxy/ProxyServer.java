package webproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyServer implements Runnable {

  private int serverPort = 8080;
  private ServerSocket serverSocket = null;
  private boolean isStopped = false;
  private Thread runningThread = null;
  private static final int NUM_THREAD = 20;
  private ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREAD);

  private String threadtype;
  private boolean useBufferReader;
  private ConcurrentHashMap<URL, byte[]> contentCache = new ConcurrentHashMap<URL, byte[]>(
      new HashMap<URL, byte[]>());
  private Object lock;

  public ProxyServer(int port, String threadtype, boolean useBufferReader) {
    this.serverPort = port;
    this.threadtype = threadtype;
    this.useBufferReader = useBufferReader;
    this.lock = new Object();
  }

  public void run() {
    synchronized (this) {
      runningThread = Thread.currentThread();
    }
    openServerSocket();
    System.out.println("Proxy Server get started.");
    while (!isStopped()) {
      Socket clientSocket = null;
      try {
        clientSocket = serverSocket.accept();
      } catch (IOException e) {
        if (isStopped()) {
          System.out.println("Proxy Server Stopped.");
          return;
        }
        throw new RuntimeException("Error accepting client connection", e);
      }
      switch (threadtype) {
      case "thread pool":
        threadPool.execute(new WorkerRunnable(clientSocket, useBufferReader,
            contentCache, lock));
        break;
      case "multithread":
        new Thread(new WorkerRunnable(clientSocket, useBufferReader,
            contentCache, lock)).start();
        break;
      case "single thread":
        try {
          processClientRequest(clientSocket);
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      }
    }
    if (threadtype.equals("thread pool")) {
      threadPool.shutdownNow();
    }
    System.out.println("Server Stopped.");
  }

  private void processClientRequest(Socket clientSocket) throws IOException {
    InputStream input = clientSocket.getInputStream();
    OutputStream output = clientSocket.getOutputStream();
    proxyHandler(input, output);
    clientSocket.close();
  }

  private void proxyHandler(InputStream inputStream, OutputStream outputStream)
      throws IOException {
    URL url = getURL(inputStream);
    if (url == null)
      return;
    // System.out.println("url:\n" + url);
    HttpURLConnection action = (HttpURLConnection) url.openConnection();
    InputStream in = action.getInputStream();
    if (useBufferReader == false) {
      byte[] pageData = receiveData(in);
      outputStream.write(pageData);
    } else {
      String res = encodeReceivedData(in);
      outputStream.write(res.getBytes());
    }
    // System.out.println("====get page===");
    outputStream.close();
    inputStream.close();
  }

  private synchronized boolean isStopped() {
    return isStopped;
  }

  private String encodeReceivedData(InputStream input) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(input));
    StringBuilder sb = new StringBuilder();
    String lin = System.getProperty("line.separator");
    for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
      sb.append(temp + lin);
    }
    return br.toString();
  }

  private byte[] receiveData(InputStream inputStream) {
    int count = 0;
    byte[] bytes = null;
    while (count == 0)
      try {
        count = inputStream.available();
        bytes = new byte[count];
        inputStream.read(bytes);
      } catch (IOException e) {
        e.printStackTrace();
      }
    return bytes;
  }

  public synchronized void stop() {
    isStopped = true;
    try {
      serverSocket.close();
    } catch (IOException e) {
      throw new RuntimeException("Error closing server", e);
    }
  }

  private URL getURL(InputStream inputStream) throws IOException {

    String str = new String(receiveData(inputStream));
    if (str.startsWith("GET") == false)
      return null;
    String[] strs = str.split("\\n");
    System.out.println("head:\n" + strs[0]);
    String[] head = strs[0].split(" ");
    URL url = null;
    try {
      url = new URL(head[1]);
    } catch (MalformedURLException e) {
      throw new MalformedURLException("MalformedURLException: " + url);
    }
    return url;
  }

  private void openServerSocket() {
    try {
      serverSocket = new ServerSocket(serverPort);
    } catch (IOException e) {
      throw new RuntimeException("Cannot open port" + serverPort, e);
    }
  }
}