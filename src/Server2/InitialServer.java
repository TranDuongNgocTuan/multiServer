package Server2;

import java.io.*;

public class InitialServer
{
	public static void main(String args[])
	{
		try{
		
		FileOutputStream fo1 = new FileOutputStream("Server1circle.txt");
		FileOutputStream fo2 = new FileOutputStream("Server2circle.txt");
		FileOutputStream fo3 = new FileOutputStream("Server3circle.txt");
		FileOutputStream fo4 = new FileOutputStream("Server4circle.txt");
		FileOutputStream fo5 = new FileOutputStream("Server5circle.txt");
		}
		catch(Exception ex){}
			
	}
}