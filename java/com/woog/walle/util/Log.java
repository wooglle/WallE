package com.woog.walle.util;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.woog.walle.WallE;

public class Log 
{
	static final String sFileChat = "/Log/chatlog.txt";
	public static void chatlog(String chat) 
	{
		File f = new File(WallE.wallepath + sFileChat);
		chat +=  "\r\n";
		try 
		{
			if (!f.exists())	//如果路径不存在则创建
			{
				File logdir = new File(f.getParent());
				if(!logdir.exists())
				{
					logdir.mkdirs();
				}
				f.createNewFile();
			}
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		try
		{
			FileWriter fw = new FileWriter(f,true);
			fw.write(chat);
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
