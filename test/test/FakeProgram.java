package test;

import java.io.*;
import java.net.*;

public class FakeProgram {
	
	public static void main(String args[]) {
		System.out.println("Hello from the fakeProgram!");	
		Thread t1 = new Thread(new Master());
		t1.start();
	}
	
	static class Master extends Thread {
		int value = 0;
		
		@SuppressWarnings("unused")
		public static boolean isNumeric(String s) {
			try {
				int svalue = Integer.parseInt(s);
			} catch (NumberFormatException nfe) {
				return false;
			}
			return true;
		}
		
		public void run() { 							// overwrite run method
			int i = 0;
			
				try {
					ServerSocket ss = new ServerSocket(9999);
					Socket s = ss.accept(); 
					
					DataOutputStream dos = new DataOutputStream(s.getOutputStream());
					
					while (true) {
						Thread.sleep(10000);				// sleep for 10 seconds
						i++;
						dos.writeInt(1);
						System.out.println("Sending job to slave...");
						
						if (i > 100) {
							break;
						}
						
					}
					
					ss.close();
					System.exit(0);
					
				} catch(Exception e) {
					
				}
		}
	}
	
}