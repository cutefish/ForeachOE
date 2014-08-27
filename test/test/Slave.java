package test;

import java.net.*;
import java.io.*;
import java.util.*;

public class Slave {
	
	static Queue<Job> JobsToRun = new LinkedList<Job>();
	static Queue<Job> RunningJobs = new LinkedList<Job>();
	static Queue<Job> FinishedJobs = new LinkedList<Job>();
	static int maxNumExecutors = 16;
	static String[] args = null;
	
	public static void main(String args[]){
		maxNumExecutors = Integer.parseInt(args[0]);
		Thread ta = new Thread(new TaskAdmission());
		Thread launcher = new Thread(new Launcher());
		Thread sr = new Thread(new StatusReport());
		ta.start();
		launcher.start();
		sr.start();
	}
	
	public synchronized static void updateEntry(Job j1) {				// method to enter jobs into the queue
		if (JobsToRun.contains(j1) == false) {
			JobsToRun.add(j1);
			System.out.println("Added job to Job Queue!");
		}
	}
	
	public static class TaskAdmission extends Thread {
		public synchronized void run() { 								// overwrite run method
			try {
				@SuppressWarnings("resource")
				Socket s = new Socket("localhost", 9999); 				// create new socket connection between master and slave
				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));	// buffered reader to read input from master
				while (true) {
					Thread.sleep(2000);
					int job = br.read();	// put the next value from buffered reader into job
					System.out.println("Read job from master!");
					Job j = new Job(job,"new");
					updateEntry(j);
				}
			} catch (Exception e) {
				
			}
		}
	}
	
	public static class Launcher extends Thread {	// launcher thread that checks queue and starts executor threads
		public synchronized void run() {							// must overrun run function when using threads
			try {
				while (true) {
					Thread.sleep(500);
					if (JobsToRun.size() > 0 && RunningJobs.size() < maxNumExecutors) { // there is at least 1 job that needs to run!
						new Executor().start();			// create new executor thread to do the job
					} else {
						continue;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class Executor extends Thread {	// thread that actually performs the action needed on the job
		int taskId;
		String curState;
		public synchronized void run() {
			try {
				Job newjob = JobsToRun.poll();		// gets the first value off of the new jobs queue
				this.taskId = newjob.taskId;		// sets local values equal to the values of that job
				this.curState = newjob.curState;
				
				newjob.curState = "running";
				
				synchronized(RunningJobs) {
					RunningJobs.add(newjob);		// since job is now running, add it to the runningjobs queue					
				}
				
				FindValues.main(args);
				
				System.out.println("Job completed!");
				
				newjob.curState = "finished";
				
				synchronized(RunningJobs) {
					synchronized(FinishedJobs) {
						RunningJobs.remove(newjob);
						FinishedJobs.add(newjob);						
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class StatusReport extends Thread {
		int j1, j2, j3;
		
		public synchronized void run() { 				// overwrite run method
			try {
				while (true) {
					Thread.sleep(5000);
					j1 = JobsToRun.size();
					System.out.println("There are " + j1 + " jobs on the queue, waiting to run.");
					j2 = RunningJobs.size();
					System.out.println("There are " + j2 + " jobs running at the moment!");
					j3 = FinishedJobs.size();
					System.out.println("There are " + j3 + " jobs that have finished.");
				}
			} catch (Exception e) {
				
			}
		}
	}

	
}