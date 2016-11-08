package Server3;

import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;
import java.sql.*;
import java.util.Hashtable;
import java.util.Vector;

// lop de thuc hien phan hoi cho Server khac hoac tu client gui den
class Handler extends Thread{

	Socket client;
	String serverName;
	String type;

	int pos;
	RountingTable rount;
	public int currentCircle;
	public String MESSAGE;
	Hashtable hash;
	
	 	DataOutputStream 	out;
	 	BufferedReader 		in;
		
	public Handler(Socket newSocket,String serverName,int pos, int curr, Hashtable hash){
		client=newSocket;
		this.serverName = serverName;
		rount = new RountingTable();
		this.pos = pos;
		this.currentCircle = curr;
		MESSAGE ="";
		this.hash = hash;
	}

	public void run()
	{
	try
		{
		String destName = client.getInetAddress().getHostName();
			int destPort = client.getPort();
			System.out.println("Accepted connection from "+destName +" on port "+destPort+".");
			
			BufferedReader inStream = new BufferedReader(
          		new InputStreamReader(client.getInputStream()));
          			
          	DataOutputStream outStream = new
               	DataOutputStream(client.getOutputStream());
               	
            boolean finished = false;
            //do 
            {
            	// lay goi tin nhan duoc
            	String inLine = inStream.readLine();
            	// tao doi tuong RecievedMessageProcessing de phan tich goi tin
            	RecievedMessageProcessing re = new RecievedMessageProcessing(inLine);
            	
            	String st = re.getStart();
            	String je = re.getJeton();
            	String lamport = re.getLamport();
            	String name = re.getServerName();
            	String type = re.getType();
            	String action = re.getAction();
            	String circle = re.getNumCircle();
            	String message = re.getMessage();
            	MESSAGE = message;
            	String jeton;
            	
             	int start = Integer.parseInt(st);
             	int act = Integer.parseInt(action);
          	
            	String t = "", rev;
            	
            	if (act==4) {
            		rev=je;
                	int po=pos+9;
                	//rev +="1";
                	try
                	{
                		rev = je.substring(1,po);
                		}            	
                		catch(Exception ex){}
                		t=rev;
                		//System.out.println("Jeton cua act=4 : "+t);
                	}
            	else
            	if (act==3) {
            		try
                	{
                		t = je.substring(0,pos-1);
                	}            	
                	catch(Exception ex){}
                	
        			jeton=je;
                	t +="1";
                	try
                	{
                		t += jeton.substring(pos);
                		}            	
                		catch(Exception ex){}
                		//System.out.println("Jeton cua act=3 : "+t);
                	}
            	else            	
            	if (act==2) {
            		try
                	{
                		t = je.substring(0,pos-1);
                	}            	
                	catch(Exception ex){}
                	
        			jeton=je;
                	t +="1";
                	try
                	{
                		t += jeton.substring(pos);
                		}            	
                		catch(Exception ex){}
                		//System.out.println("Jeton cua act=2 : "+t);
          		}
            	else
            		if (act==1) {
            			try
                    	{
                    		t = je.substring(0,pos-1);
                    	}            	
                    	catch(Exception ex){}
                    	
            			jeton=je;
                    	t +="1";
                    	try
                    	{
                    		t += jeton.substring(pos);
                    		}            	
                    		catch(Exception ex){} 
                    		//System.out.println("Jeton cua act=1"+t);
                    	}
            		int vt = pos;
            		if (vt>rount.max-1)
            			vt=0;
            		
//  xu ly thong tin Synchronymed va ket thuc vong tron ao
            	    
          if (type.equals("Synchronymed")&&(start==2))
                         	 {
                      			System.out.println("Hoan tat giao dich. Ket thuc vong tron ao");
                      			/*
                      			System.out.println("Da Received message from: "+this.serverName);
                        		String replyClientMessage = this.serverName + "  Hoan tat dang ky";
            	            	for(int i=0;i<replyClientMessage.length();++i)
            	            		outStream.write((byte)replyClientMessage.charAt(i));
            	            	System.out.println("Reply: " + replyClientMessage+"\n");
            	            	*/
                     	    }//dong if
            		
//xu ly thong tin updated va quay vong
            	    
         if (type.equals("Updated")&&(start==2))
              	 {
	    				int stt=start;
           			System.out.println("Ket thuc qua trinh cap nhat, kiem tra dong bo hoa TT va Quay vong nguoc");
           			stt=1;
           			act+=1;
	    		       	try {
	    	    		   int tam = pos-2;
	    	       		if (tam<0)
	    	       			tam=2;		
	    	       		if (t.charAt(tam)=='0')
	    	       		{
	    	       			System.out.println("\nServer"+ (tam+1) + " bi su co.");
	    	       			System.out.println("do jeton nhan duoc la: "+t+"\n");	            			
	    	       		
	    	       			tam--;
	    	       			
	    	       		}
	    	       		if (tam<0)
	    	       			tam=2;
	    	       		ConnectToServer co = new ConnectToServer(rount.table[tam].destination,rount.table[tam].port,rount.table[tam].name);
	    	       		co.connect();
	    	       		String replyServerMessage ="@$"+stt+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+"Synchronymed"+"|"+act+"|"+circle+"$$"+message+"$@";
	    	       		co.requestServer(replyServerMessage);
	    	       		co.shutdown();
	    		       	} catch(Exception Ex){}
          	       		
          	     	   }//dong if

//  xu ly thong tin temped va quay vong
            	    
             	   if (type.equals("Temped")&&(start==2))
         	       	{
             		int stt=start;
          			System.out.println("Ket thuc tao bang tam, cap nhat CSDL chinh Quay vong nguoc");
          			stt=1;
          			act+=1;
         	    		try
         	       		{	            	
         	           	ConnectToServer co = new ConnectToServer(rount.table[vt].destination,rount.table[vt].port,rount.table[vt].name);
         	           	co.connect();	            		
         	           	co.requestServer("@$"+stt+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+"Updated"+"|"+act+"|"+circle+"$$"+message+"$@");
         	           	co.shutdown();
        	       		}
         	           	//bi su co
         	       		catch(Exception ex)
         	           	{
         	           		System.out.println("\n"+rount.table[vt].name+": bi su co vo van 3, hien khong lien lac duoc\n");
         	           		vt++;
         	           		if (vt>rount.max-1)
         	           			vt=0;
         	           		
         	           		ConnectToServer con = new ConnectToServer(rount.table[vt].destination,rount.table[vt].port,rount.table[vt].name);
         		            	con.connect();	            		
         		            	con.requestServer("@$"+stt+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+"Updated"+"|"+act+"|"+circle+"$$"+message+"$@");
         		            	con.shutdown();
          	           	}
         	     	   }//dong if
            		

//quay vong nguoc lai cua thong diep locked
            if (type.equals("Locked")&&(start==2))
	    			{
	    				int stt=start;
            			System.out.println("Ket thuc khoa truong du lieu, tao bang tam va Quay vong nguoc");
            			stt=1;
            			act+=1;
	    		       	try {
	    	    		   int tam = pos-2;
	    	       		if (tam<0)
	    	       			tam=2;		
	    	       		if (t.charAt(tam)=='0')
	    	       		{
	    	       			System.out.println("\nServer"+ (tam+1) + " bi su co.");
	    	       			System.out.println("do jeton nhan duoc la: "+t+"\n");	            			
    	       			tam--;
	    	       		}
	    	       		if (tam<0)
	    	       			tam=2;
	    	       		ConnectToServer co = new ConnectToServer(rount.table[tam].destination,rount.table[tam].port,rount.table[tam].name);
	    	       		co.connect();
	    	       		String replyServerMessage ="@$"+stt+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+"Temped"+"|"+act+"|"+circle+"$$"+message+"$@";
	    	       		co.requestServer(replyServerMessage);
	    	       		co.shutdown();
	    		       	} catch(Exception Ex){}
	    		      } //dong if
            		
//    	 xu ly thong tin tu client
   	   if (start==0)
           {
    	   			start++;
    	   		
    	   			//System.out.println("Da Received message from: "+this.serverName);
            		String replyClientMessage = this.serverName + "  Hoan tat dang ky";
	            	for(int i=0;i<replyClientMessage.length();++i)
	            		outStream.write((byte)replyClientMessage.charAt(i));
	            	System.out.println("Reply: " + replyClientMessage+"\n");
	            	System.out.println("Thuc hien khoa truong DL. Chuyen thong diep ");
             	try
            	{	            	
	            	ConnectToServer co = new ConnectToServer(rount.table[vt].destination,rount.table[vt].port,rount.table[vt].name);
	            	co.connect();	            		
	            	co.requestServer("@$"+start+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+"Locked"+"|"+act+"|"+circle+"$$"+message+"$@");
	            	co.shutdown();
           	}
	            	//bi su co
            		catch(Exception ex)
	            	{
	            		System.out.println("\n"+rount.table[vt].name+": bi su co lang nhach 3, hien khong lien lac duoc\n");
	            		vt++;
	            		if (vt>rount.max-1)
	            			vt=0;
	            		
	            		ConnectToServer con = new ConnectToServer(rount.table[vt].destination,rount.table[vt].port,rount.table[vt].name);
		            	con.connect();	            		
		            	con.requestServer("@$"+start+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+"Locked"+"|"+act+"|"+circle+"$$"+message+"$@");
		            	con.shutdown();
	            	} 
           } 	//end if
    	   //ket thuc xu ly tu client
            		
// xu ly thong tin locked
    	   if (type.equals("Locked")&&(start!=2))
	       	{
    		   System.out.println("Chuyen thong diep thuc hien khoa truong DL");
    		   start++;
	    		try
	       		{	            	
	           	ConnectToServer co = new ConnectToServer(rount.table[vt].destination,rount.table[vt].port,rount.table[vt].name);
	           	co.connect();	            		
	           	co.requestServer("@$"+start+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+"Locked"+"|"+action+"|"+circle+"$$"+message+"$@");
	           	co.shutdown();
	       		}
	           	//bi su co
	       		catch(Exception ex)
	           		{
	           		System.out.println("\n"+rount.table[vt].name+": bi su co chet tiet 3, hien khong lien lac duoc\n");
	           		vt++;
	           		if (vt>rount.max-1)
	           			vt=0;
	           		
	           		ConnectToServer con = new ConnectToServer(rount.table[vt].destination,rount.table[vt].port,rount.table[vt].name);
		            	con.connect();	            		
		            	con.requestServer("@$"+start+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+type+"|"+action+"|"+circle+"$$"+message+"$@");
		            	con.shutdown();
	           		}
	     	   }//dong if
 
//    	 Xu ly thong diep temp
           if (type.equals("Temped")&&(start!=2))
	    			{
           			System.out.println("Chuyen thong diep thuc hien tao bang tam CSDL");
           			start++;
	    		       	try {
	    	    		   int tam = pos-2;
	    	       		if (tam<0)
	    	       			tam=2;		
	    	       		if (t.charAt(tam)=='0')
	    	       		{
	    	       			System.out.println("\nServer"+ (tam+1) + " bi su co.");
	    	       			System.out.println("do jeton nhan duoc la: "+t+"\n");	            			
	    	       			tam--;
	    	       		}
	    	       		if (tam<0)
	    	       			tam=2;
	    	       		ConnectToServer co = new ConnectToServer(rount.table[tam].destination,rount.table[tam].port,rount.table[tam].name);
	    	       		co.connect();
	    	       		String replyServerMessage ="@$"+start+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+type+"|"+act+"|"+circle+"$$"+message+"$@";
	    	       		co.requestServer(replyServerMessage);
	    	       		co.shutdown();
	    		       	} catch(Exception Ex){}
	    		      } //dong if
    	   
//         xu ly thong tin update
    	   if (type.equals("Updated")&&(start!=2))
	       	{
    		   System.out.println("Chuyen thong diep thuc hien cap nhat bang chinh CSDL");
    		   start++;
	    		try
	       		{	            	
	           	ConnectToServer co = new ConnectToServer(rount.table[vt].destination,rount.table[vt].port,rount.table[vt].name);
	           	co.connect();	            		
	           	co.requestServer("@$"+start+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+type+"|"+action+"|"+circle+"$$"+message+"$@");
	           	co.shutdown();
	       		}
	           	//bi su co
	       		catch(Exception ex)
	           		{
	           		System.out.println("\n"+rount.table[vt].name+": bi su co chi chi rau 3, hien khong lien lac duoc\n");
	           		vt++;
	           		if (vt>rount.max-1)
	           			vt=0;
	           		
	           		ConnectToServer con = new ConnectToServer(rount.table[vt].destination,rount.table[vt].port,rount.table[vt].name);
		            	con.connect();	            		
		            	con.requestServer("@$"+start+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+type+"|"+action+"|"+circle+"$$"+message+"$@");
		            	con.shutdown();
	           		}
	     	   }//dong if
    	   
//      	 Xu ly thong diep synchronym
           if (type.equals("Synchronymed")&&(start!=2))
	    			{
           			System.out.println("Chuyen thong diep kiem tra qua trinh dong bo hoa cac tien trinh");
           			start++;
	    		       	try {
	    	    		   int tam = pos-2;
	    	       		if (tam<0)
	    	       			tam=2;		
	    	       		/*
	    	       		 if (jeton.charAt(tam)=='0')
	    	       		{
	    	       			System.out.println("\nServer"+ (tam+1) + " bi su co.");
	    	       			System.out.println("do jeton nhan duoc la: "+jeton+"\n");	            			
	    	       			tam--;
	    	       		}
	    	       		if (tam<0)
	    	       			tam=2;
	    	       			*/
	    	       		ConnectToServer co = new ConnectToServer(rount.table[tam].destination,rount.table[tam].port,rount.table[tam].name);
	    	       		co.connect();
	    	       		String replyServerMessage ="@$"+start+"|"+t+"|"+lamport+"|"+rount.table[pos-1].name+"|"+type+"|"+action+"|"+circle+"$$"+message+"$@";
	    	       		co.requestServer(replyServerMessage);
	    	       		co.shutdown();
	    		       	} catch(Exception Ex){}
	    		      } //dong if
  
    	   outStream.write(13);
			outStream.write(10);
			outStream.flush();    			
			}
		//	while(!finished);
			//inStream.close();
			//outStream.close();

		}//try
		catch(Exception e)	{}

	}//run
	
}//class