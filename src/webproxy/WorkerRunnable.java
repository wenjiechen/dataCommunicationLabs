package webproxy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;

public class WorkerRunnable implements Runnable {

  private Socket clientSocket = null;
  private Proxy proxy = null;
  private String proxyIp;
  private int proxyPort;

  public WorkerRunnable(Socket clientSocket, String proxyIp, int proxyPort) {
    this.clientSocket = clientSocket;
    this.proxyIp = proxyIp;
    this.proxyPort = proxyPort;
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
    System.out.println("url:\n" + url);
    // instance remote proxy
    proxy = new Proxy(Proxy.Type.HTTP,
        new InetSocketAddress(proxyIp, proxyPort));
    // use proxy open a web page
    HttpURLConnection action = (HttpURLConnection) url.openConnection(proxy);
    InputStream in = action.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    StringBuilder sb = new StringBuilder();
    String lin = System.getProperty("line.separator");
    for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
      sb.append(temp + lin);
    }
    System.out.println("====get page===");
    httpResponseConstructer(sb.toString(), outputStream);
    br.close();
    outputStream.close();
    inputStream.close();
  }

  private URL getURL(InputStream inputStream) throws IOException {
    int count = 0;
    while (count == 0)
      count = inputStream.available();
    byte[] bytes = new byte[count];
    inputStream.read(bytes);
    String str = new String(bytes);
    if (str.startsWith("GET") == false)
      return null;
    String[] strs = str.split("\\n");
    System.out.println("head:\n" + strs[0]);
    String[] head = strs[0].split(" ");
    URL url = null;
    try {
      url = new URL(head[1]);
    } catch (MalformedURLException e) {
      throw new MalformedURLException(" ");
    }
    return url;
  }

  private void httpResponseConstructer(String response, OutputStream output)
      throws IOException {
    try {
      output.write(response.getBytes());
    } catch (FileNotFoundException e) {
      return;
    }
  }
}