package ors.pdcl.jetty;

import java.util.concurrent.atomic.AtomicInteger;

public class Worker extends Thread {
  private static AtomicInteger counter = new AtomicInteger(0);

  @Override
  public void run() {
    while (true) {
      try {
        Thread.sleep(1000);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      counter.incrementAndGet();
    }
  }

  public static int getCounter() {
    return counter.get();
  }
}
