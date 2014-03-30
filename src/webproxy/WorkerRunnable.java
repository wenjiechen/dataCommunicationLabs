package webproxy;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;

public class WorkerRunnable implements Runnable {

  protected Socket clientSocket = null;
  protected String serverText = null;
  protected Proxy proxy = null;

  public WorkerRunnable(Socket clientSocket, String serverText) {
    this.clientSocket = clientSocket;
    this.serverText = serverText;
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
    int count = 0;
    while (count == 0)
      count = inputStream.available();
    byte[] bytes = new byte[count];
    inputStream.read(bytes);
    String str = new String(bytes);
    String[] strs = str.split("\\n");
    System.out.println("head:\n"+strs[0]);
    String[] head = strs[0].split(" ");
    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("91.210.44.196",
        3129)); // 实例化本地代理对象，端口为8888
    URL url = new URL(head[1]);
    System.out.println("url:\n"+url);
    HttpURLConnection action = (HttpURLConnection) url.openConnection(proxy); // 使用代理打开网页
    InputStream in = action.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
    StringBuilder sb = new StringBuilder();
    String lin = System.getProperty("line.separator");
    for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
      sb.append(temp + lin);
    }
    System.out.println(sb.toString());
    httpResponseConstructer(sb.toString(), outputStream);
    br.close();
    outputStream.close();
    inputStream.close();
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