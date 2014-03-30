package proxy2;

import java.net.*;
import java.io.*;

public class ProxyTest {
  public static void main(String args[]) throws Exception {
    Proxy proxy = null;
    System.out.println("start server wenjie");
    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("91.210.44.196", 3129)); // 实例化本地代理对象，端口为8888
    URL url = new URL("http://www.google.cn");
    HttpURLConnection action = (HttpURLConnection) url.openConnection(proxy); // 使用代理打开网页
    InputStream in = action.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    StringBuilder sb = new StringBuilder();
    String lin = System.getProperty("line.separator");
    for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
      sb.append(temp + lin);
    }
    br.close();
    in.close();
    System.out.println(sb);
  }
}