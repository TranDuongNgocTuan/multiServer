package Server1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
class Ghixuongluong
{
	Ghixuongluong(String tenfile,String mess)
	{
		try{
		FileOutputStream os = new FileOutputStream(tenfile);
		for(int i=0;i<mess.length();i++)
		os.write(mess.charAt(i));
		//os.write('t');
		os.close();
		}catch(Exception ef){System.out.println("Co loi viec truy xuat file");}
	}
}