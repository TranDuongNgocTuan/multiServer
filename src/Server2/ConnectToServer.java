package Server2;

import java.net.*;
import java.io.*;
import java.lang.Thread;

// lop tao doi tuong ket noi den mot Server khac neu Server co yeu cau
public class ConnectToServer extends Thread
{
	String destination;
	int port;
	String serverName;
	
	Socket connection;
 	DataOutputStream out;
 	BufferedReader in;
	
	public ConnectToServer(String destination,int port, String serverName)
	{
		this.destination = destination;
		this.port = port;
		this.serverName = serverName;
	}
	
	public void connect()
	{
		try
		{
			connection = new Socket(destination,port);
			
			in = new BufferedReader(new 
				InputStreamReader(connection.getInputStream()));
				
			out= new DataOutputStream(
                           connection.getOutputStream());
		
			System.out.println("Connected to "+ serverName +" at port "+ port+".");
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
	}

	public void requestServer(String message)
 	{
 		/*BufferedReader keyboardInput = new BufferedReader(
        	new InputStreamReader(System.in)); // yeu cau nhap tu ban phim
        */
        //boolean finished=false;
        try
        {
        	System.out.println("Send a message to: "+ serverName);
        	System.out.println("Message: "+message.substring(message.lastIndexOf("%%")+2));
        	System.out.flush();
        	
        	out.writeBytes(message);
        	out.write(13);
        	out.write(10);
        	out.flush();
 
 			/*
     		// nhan thong diep tu server hoi dap lai
     		int inByte;
     		System.out.print("\nRecieved: ");
     		while ((inByte = in.read()) != '\n')
     		System.out.write(inByte);
     		
     		*/
     		System.out.println();
    
   		}
   		catch (Exception e)
   		{
   			System.out.println(e);
   		}
	}
	
	public void shutdown()
	{
		try
		{
			connection.close();
		}catch (IOException ex)
		{
			System.out.println("IO error closing socket");
		}
	}
	
	public void run(String s)
	{
		requestServer(s);
		shutdown();
	}

}
