package com.woog.walle.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import com.woog.walle.WallE;

import net.minecraft.client.Minecraft;

public class Config {
	static final String sFileConfig =  "WallE.properties";
	public static Minecraft mc = Minecraft.getMinecraft();
	public static String myfriends = "Strideyouyou,mcfy,st_,WooG";
	public static String[] vip = {"woog", "sdsas", "mr_caigi", "lzd", "ck250", "ls", "panjy", "yunzhongyan", "strideyouyou", "ann", "david", "fant", "st", "020", "ez12", "virginia", "reden", "touxin", "candy_1", "mcfy"};
	public static boolean sAll, sFeed, sFish, sRelogin, sActionSheild;
	
	/**
	 * 获取配置文件，如果没有则创建
	 * @return
	 */
	public static File getFile(){
		File f= new File(WallE.wallepath + sFileConfig);
		try {
			if (!f.exists()) {
				File configdir = new File(f.getParent());
				if (!configdir.exists()) {
					configdir.mkdirs();
				}
				f.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return f;
	}
	
	public static String getKey(String key) {
		Properties p=new Properties();
		try {
			InputStream input = new FileInputStream(getFile());
			p.load(input);
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p.getProperty(key);
	}
	
	public int getKey2int(String key) {
		String s = getKey(key);
		int v = Integer.valueOf(s).intValue();
		return v;
	}
	
	public boolean getKey2boolean(String key) {
		String s = getKey(key);
		return s.toLowerCase().equals("true") ? true : false;
	}
	
	/**
	 * 添加String类型的key
	 * @param key
	 * @param str
	 */
	public static void addKey(String key, String value) {
		if(isDefined(key)) {
			setKey(key, value);
		}else{
			Properties p=new Properties();
			try {
				OutputStream output = new FileOutputStream(getFile());
				p.store(output, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 添加int类型的key
	 * @param key
	 * @param value
	 */
	public static void addKey(String key, int value) {
		addKey(key, String.valueOf(value));
	}
	
	public static void addKey(String key, boolean value) {
		String bool = value ? "true" : "false";
		addKey(key, bool);
	}
	
	private static void setKey(String key, String value) {
		Properties p=new Properties();
		try {
			InputStream input = new FileInputStream(getFile());
			p.load(input);
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(p.containsKey(key)) {
			p.setProperty(key, value);
			try {
				OutputStream output = new FileOutputStream(getFile());
				p.store(output, null);
				output.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			addKey(key, value);
		}
	}
	
	/**
	 * 查找是否存在key
	 * @param key
	 * @return
	 */
	private static boolean isDefined(String key) {
		return getKey(key) == null ? false : true;
	}
}
