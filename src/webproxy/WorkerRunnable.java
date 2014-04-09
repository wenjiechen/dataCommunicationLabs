package webproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;

public class WorkerRunnable implements Runnable {

  private Socket clientSocket = null;
  boolean useBufferReader;
  private ConcurrentLRUCache<URL, byte[]> contentCache;
  private Object lock;

  public WorkerRunnable(Socket clientSocket, boolean useBufferReader,
      ConcurrentLRUCache<URL, byte[]> contentCache, Object lock) {
    this.clientSocket = clientSocket;
    this.useBufferReader = useBufferReader;
    this.contentCache = contentCache;
    this.lock = lock;
  }

  public void run() {
    try {
      InputStream inputStream = clientSocket.getInputStream();
      OutputStream outputStream = clientSocket.getOutputStream();
      proxyHandler(inputStream, outputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void proxyHandler(InputStream inputStream, OutputStream outputStream)
      throws IOException {
    URL url = getURL(inputStream);
    if (url == null)
      return;
    if (contentCache.containsKey(url)) {
      // hit in cache
      System.out.println("hit cache");
      outputStream.write(contentCache.get(url));
    } else {
      // miss in cache
      System.out.println("miss cache: " + url);
      HttpURLConnection action = (HttpURLConnection) url.openConnection();
      InputStream in = action.getInputStream();
      if (useBufferReader == false) {
        byte[] pageData = receiveData2(in);
        outputStream.write(pageData);
        LRUReplacement(url, pageData);
      } else {
        // encode receive data
        String res = bufferReceivedData(in);
        outputStream.write(res.getBytes());
        LRUReplacement(url, res.getBytes());
      }
    }
    outputStream.close();
    inputStream.close();
    clientSocket.close();
  }

  private void writeToFile(byte[] data) {

  }

  private void LRUReplacement(URL url, byte[] data) {
    synchronized (lock) {
      contentCache.put(url, data);
    }
  }

  private byte[] receiveData(InputStream inputStream) {
    int count = 0;
    byte[] bytes = null;
    while (count == 0) {
      try {
        count = inputStream.available();
        bytes = new byte[count];
        inputStream.read(bytes);
        Thread.sleep(20);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return bytes;
  }

  private byte[] receiveData2(InputStream inputStream) throws IOException {
    byte[] bytes = IOUtils.toByteArray(inputStream);
    return bytes;
  }

  private String bufferReceivedData(InputStream input) throws IOException {
    // BufferedReader br = new BufferedReader(
    // new InputStreamReader(input, "UTF-8"));
    BufferedReader br = new BufferedReader(new InputStreamReader(input));
    StringBuilder sb = new StringBuilder();
    String lin = System.getProperty("line.separator");
    for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
      sb.append(temp + lin);
    }
    return sb.toString();
  }

  private URL getURL(InputStream inputStream) throws IOException {

    String str = new String(receiveData(inputStream));
    if (str.startsWith("GET") == false)
      return null;
    String[] strs = str.split("\\n");
    // System.out.println("head:\n" + strs[0]);
    String[] head = strs[0].split(" ");
    URL url = null;
    try {
      url = new URL(head[1]);
    } catch (MalformedURLException e) {
      throw new MalformedURLException("MalformedURLException: " + url);
    }
    return url;
  }

  private void httpResponseConstructer(String response, OutputStream output)
      throws IOException {
    output.write(response.getBytes());
  }
}