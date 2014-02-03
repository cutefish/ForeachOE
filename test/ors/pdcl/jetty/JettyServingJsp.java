package ors.pdcl.jetty;

import java.net.URL;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServingJsp {
  public static void main(String[] args) throws Exception {
    Worker w = new Worker();
    w.start();

    Server server = new Server(8080);
    URL warUrl = JettyServingJsp.class.getClassLoader().getResource("");
    String warUrlString = warUrl.toExternalForm();
    System.out.println(warUrlString);
    WebAppContext webapp = new WebAppContext(warUrlString, "");
    server.setHandler(webapp);
    try {
      server.start();
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
    server.join();
  }
}
