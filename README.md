dataCommunicationLabs
---
This is a course project of Data Communication and Networks, containing a simple http **web server** and a simple http **web proxy**.

Run
---
####These two program are in a Eclipse project. You can run them in Eclipse.

* **web server**
  - Run ServerStarter to Start the server. The default runtime is 600 seconds and listening port is 9000.
  - There are some test files in project directory: index.html, index2.html,index3.html, testFiles(directory).
  - Use web browser visit like http://localhost:9000/index.html or http://localhost:9000/testFiles/index2.html . Visit the     page that does not exist, you will get a 404 error page.
  - "localhost" can be 127.0.0.1

* **web proxy**
  - make sure the build path of the project contains commons-io-2.4.jar and lucene-core-4.7.1.jar
  - set up your browser's http proxy: 127.0.0.1, port 9000(default)
  - Run ProxyServerStarter to start the proxy. 
  - use web browser to visit www.google.com, www.nba.com, or www.youtube.com
  
