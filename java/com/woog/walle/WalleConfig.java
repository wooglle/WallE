package com.woog.walle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class WalleConfig {
	static final String sFileConfig =  "WallE.properties";
	File file = new File(WallE.wallepath + sFileConfig);
	Properties properties = new Properties();
	
	public WalleConfig() {
		if(!file.exists()) {
			iniConfig();
		}else{
			try {
				InputStream in = new FileInputStream(file);
				properties.load(in);
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private Properties getProperties() {
		return null;
	}

	private void addKey(String key, String value) {
		try {
			InputStream in = new FileInputStream(file);
			properties.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		properties.put(key, value);
	}
	
	public void setKey(String key, String value) {
		try {
			InputStream in = new FileInputStream(file);
			properties.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(properties.containsKey(key)) {
			properties.setProperty(key, value);
		}else{
			addKey(key, value);
		}
		save2File();
	}
	public void setKey(String key, int value) {
		setKey(key, String.valueOf(value));
	}
	public void setKey(String key, boolean value) {
		setKey(key, String.valueOf(value));
	}
	
	public String getKey(String key) {
		return properties.getProperty(key, "none..");
	}

	private void iniConfig() {
		File filedir = file.getParentFile();
		if(!filedir.exists()) {
			filedir.mkdirs();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		all2Default();
		save2File();
	}

	private void save2File() {
		try {
			OutputStream out = new FileOutputStream(file);
			properties.store(out, null);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void all2Default() {
		properties.clear();
		properties.put("friends", "woog");
		properties.put("frie", "杀毒看僵尸");
		properties.put("friend", String.valueOf(true));
		properties.put("frien", String.valueOf(12345));
	}
}
