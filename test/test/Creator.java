package test;

import java.util.*;

public class Creator {
	public static void main(String args[]){
		LinkedList<Integer> jobs = new LinkedList<Integer>();
		int numberOfJobs = 0;
		Random r = new Random();
		
		while (numberOfJobs < 10) {
			int newRandom = r.nextInt(100);
			jobs.addFirst(newRandom);
			numberOfJobs++;
			if (numberOfJobs == 1) {
				System.out.println("There is 1 job to be done.");
			} else {
				System.out.println("There are " + numberOfJobs + " jobs to be done.");
			}
			new Workers(jobs,numberOfJobs).start();
		}
	}
	
	static class Workers extends Thread {
		int numJob;
		Integer jobToDo = 0;
		Integer output = 0;
		
		public Workers(LinkedList<Integer> list, int numberOfJobs) {
			numJob = numberOfJobs;
			jobToDo = list.pollLast();
			if (jobToDo == null) {
				System.out.println("There is no job to do on the list.");
			}
		}
		
		// need to overwrite run method
		public void run() {
			output = jobToDo/2;
			System.out.println("The output of job #" + numJob + " is: " + output);
		}
		
	}
}