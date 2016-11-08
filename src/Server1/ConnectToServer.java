package Server1;


import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.sql.*;

// lop tao doi tuong ket noi den mot Server khac neu Server co yeu cau
public class ConnectToServer extends Thread
{
	
String destination;
	int port;
	String serverName;
	int demtenfile;
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
				
			out = new DataOutputStream(
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
       try
        {
        	
		System.out.println("Send a message to: "+ serverName);
		demtenfile=0;
		//Ghi xuong file thong tin
								Ghixuongluong gh1=new Ghixuongluong("S13"+demtenfile+".txt","Da gui thong diep toi "+serverName);
								demtenfile++;

								//
        	System.out.println("Message: "+message.substring(message.lastIndexOf("%%")+2));
        	System.out.flush();
        	
        	out.writeBytes(message);
        	out.write(13);
        	out.write(10);
        	out.flush();
 
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
