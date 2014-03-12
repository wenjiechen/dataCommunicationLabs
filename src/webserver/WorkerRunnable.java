package webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

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
      httpHandler(input, output);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void httpHandler(InputStream input, OutputStream output)
      throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(input));
    String requestLine = br.readLine();
    if (requestLine == null || requestLine == "")
      return;
    String[] req = requestLine.split(" ");
    httpResponseConstructer(req, output);
    output.close();
    input.close();
  }

  private void httpResponseConstructer(String[] req, OutputStream output)
      throws IOException {
    // request date and time
    Date now = new Date();
    DateFormat dataFormater = DateFormat.getDateTimeInstance(DateFormat.LONG,
        DateFormat.LONG);
    String date = dataFormater.format(now) + "\n";

    String method = req[0];
    String URL = req[1];
    String version = req[2];
    StringBuilder statusLine = new StringBuilder(version + " ");

    if (method.equals("GET")) {
      
    } else if (method.equals("POST")) {
      
    } else if (method.equals("HEAD")) {
      
    } else {

    }

    // get request file
    try {
      String filePath = URL.substring(1);
      System.out.println(filePath);
      File file = new File(filePath);
      String lastModified = "Last-Modified: "
          + dataFormater.format(new Date(file.lastModified())) + "\n";
      String ContentLength = "Content-Length: " + file.length() + "\n";
      String ContentType = "Content-Type: " + "text/html\n";
      String responseContent = new Scanner(file).useDelimiter("\\Z").next();
      String response = statusLine.append("200 OK\n") + date + lastModified
          + ContentLength + ContentType + "\n" + responseContent;
      output.write(response.getBytes());
    } catch (FileNotFoundException e) {
      fileNotExist(output, URL);
      return;
    }

  }

  private void fileNotExist(OutputStream output, String url) throws IOException {
    String notFound = "<!DOCTYPE html> <html><body>"
        + " <h1>404 Error: File Not Found</h1></body></html>";
    output.write(("HTTP/1.1 404 Not Found\n\n" + notFound).getBytes());
  }
}