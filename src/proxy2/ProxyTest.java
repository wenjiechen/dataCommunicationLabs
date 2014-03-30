package proxy2;

import java.net.*;
import java.io.*;

public class ProxyTest {
  public static void main(String args[]) throws Exception {
    Proxy proxy = null;
    System.out.println("start server wenjie");
    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("91.210.44.196",
        3129)); // 实例化本地代理对象，端口为8888
    URL url = new URL("http://www.google.com");
    HttpURLConnection action = (HttpURLConnection) url.openConnection(proxy); // 使用代理打开网页
    InputStream inputStream = action.getInputStream();
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
    
    inputStream.close();
    System.out.println(new String(bytes));
  }
}