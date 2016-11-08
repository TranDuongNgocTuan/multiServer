package Server1;

import java.net.*;
import java.io.*;
import java.util.Hashtable;

public class Server1
{
	public static void main(String args[])
	{
		int currentCircle=0;
		Hashtable hash = new Hashtable();
		
		try
		{	
			GetState gs = new GetState("Server1");
			gs.getCurrentCircle();
			gs.sendUpdate("127.0.0.2",2002,"Server1");
									
			ServerSocket server = new ServerSocket(2001);
			while(true)
			{				
				int localPort = server.getLocalPort();
				System.out.println("Server 1 is listening on port "+localPort+".");
				System.out.println();
				Socket client = server.accept();
				Handler newHandler = new Handler(client,"Server1",1,currentCircle,hash);
				newHandler.run();
				currentCircle = newHandler.currentCircle;
				currentCircle++;
				hash.put(String.valueOf(currentCircle),newHandler.MESSAGE);
			}
			
   		}
   		catch (IOException e)
   		{
   			System.out.println("<<<<<Loi khong the tao Server>>>>>\n"+e);
   		}
 	}
}
