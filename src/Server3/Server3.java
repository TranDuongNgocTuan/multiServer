package Server3;

import java.net.*;
import java.io.*;
import java.util.Hashtable;

public class Server3
{
	public static void main(String args[])
	{
		int currentCircle=0;
		Hashtable hash = new Hashtable();
		
		try
		{
			GetState gs = new GetState("Server3");
			gs.getCurrentCircle();
			gs.sendUpdate("127.0.0.1",2001,"Server3");
	
			ServerSocket server = new ServerSocket(2003);
			while(true)
			{				
				int localPort = server.getLocalPort();
				//System.out.println("dfdsfsdf");
				System.out.println("Server 3 is listening on port "+localPort+".");
				System.out.println();
				
				Socket client = server.accept();
				Handler newHandler = new Handler(client,"Server3",3,currentCircle,hash);
				//Thread newHandlerThread=new Thread(newHandler);
				newHandler.run();
				currentCircle = newHandler.currentCircle;
				
				currentCircle++;
				hash.put(String.valueOf(currentCircle),newHandler.MESSAGE);
				
				//FileOutputStream fo = new FileOutputStream("Server3circle.txt");
				//fo.write(currentCircle);
				
			}
			
			//server.close();	
   		}
   		catch (IOException e)
   		{
   			System.out.println("<<<<<Loi khong the tao Server>>>>>\n"+e);
   		}
 	}
}
