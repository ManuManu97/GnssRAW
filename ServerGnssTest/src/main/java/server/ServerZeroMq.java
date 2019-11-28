package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ServerZeroMq {

    
	  public static void main(String[] args) throws Exception
	  {
	    try (ZContext context = new ZContext()) {
	      //  Socket to talk to clients
	    	File file = new File("C:\\Percorso del progetto+Nome directory");
	        	if (!file.exists()) {
	        		if (file.mkdir()) {
	        			System.out.println("Directory is created!");
	        		}else{
	                 System.out.println("Failed to create directory!");
	        		}
	         }
	    	ZMQ.Socket socket = context.createSocket(SocketType.REP);
		    int port = 5555;
		    socket.bind("tcp://192.168.1.187:"+port);
		    System.out.println("In ascolto su porta "+ port+ "... ");

	      while (true) {
	        byte[] reply = socket.recv(0);
	        
	        new ServerWorker(new String(reply, ZMQ.CHARSET)).start();
	        String response = "try";
	        socket.send(response.getBytes(ZMQ.CHARSET), 0);
	      }
	    }
	  }
}
	  
	  class ServerWorker extends Thread {

		  	String message;
		  	BufferedWriter myBufferedWriter;
		  	
		    public ServerWorker(String message) {
		        this.message = message;
		    }

		    public void run() {
		    	String file_name = this.message.substring(0, this.message.indexOf('#'));
		    	System.out.println(file_name);
		    	File file = new File("C:\\Percorso del progetto+Nome directory", file_name+".txt");
		    	String currentFilePath = file.getAbsolutePath();
		        try {
		            myBufferedWriter = new BufferedWriter(new FileWriter(file, true));
		        } catch (Exception e) {
		            System.out.println("Could not open file: " + currentFilePath);
		            return;
		        }
		        
		        try {
					myBufferedWriter.write(this.message.substring(this.message.indexOf('#')+1,this.message.indexOf('*')));
					myBufferedWriter.newLine();
					myBufferedWriter.write(this.message.substring(this.message.indexOf('*')+1));
					myBufferedWriter.flush();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}

