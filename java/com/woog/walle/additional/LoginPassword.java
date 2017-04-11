package com.woog.walle.additional;

import com.woog.walle.WallE;

import net.minecraft.client.Minecraft;

public class LoginPassword {
	private static Minecraft mc = Minecraft.getMinecraft();
	private static boolean isDetecting = false;
	
	public LoginPassword() {
		
	}
	
	public static String GetPassword() {
		try {
			String str = WallE.config.getKey(WallE.username.toLowerCase()  + "@" + getServerName());
			if(str == null) 
				return null;
//			System.out.println("                    " + Crypto.deBase64(str) + "   " + getServerName());
			return Crypto.deBase64(str);
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public static void DetectAndSavePassword() {
		if(!isDetecting) {
			isDetecting = true;
			new Password().start();
		}else{
			return;
		}
	}
	
	private static class Password extends Thread {
		public void run() {
			this.setName("WallE-Password");
			if (true) {
				for (int i = 0; i < mc.ingameGUI.getChatGUI().getSentMessages().size(); i++) {
					String buff = (String) mc.ingameGUI.getChatGUI().getSentMessages().get(i).toString().toLowerCase();
					if (buff.matches("^/l(ogin)?\\s+\\w+$")) {
						String password = buff.substring(buff.indexOf(" ") + 1, buff.length());
						WallE.config.setKey(WallE.username.toLowerCase() + "@" + getServerName(), Crypto.enBase64(password));
						IDebug.PrintYellow("已获得登陆密码，密码已经保存!!!");
					}else if (buff.matches("^/r(eg)?(ister)?\\s+\\w+$")) {
						String[] tem = buff.split(" ");
//						String password = buff.substring(buff.indexOf(" ") + 1, buff.length());
						String password = tem[1];
						WallE.config.setKey(WallE.username.toLowerCase() + "@" + getServerName(), Crypto.enBase64(password));
						IDebug.PrintYellow("已获得登陆密码，密码已经保存!!!");
					}
				}
			}
			isDetecting = false;
			Thread.yield();
		}
	}
	
	private static String getServerName() {
		String temp = Minecraft.getMinecraft().getConnection().getNetworkManager().getRemoteAddress().toString();
		String[] tem = temp.split("/");
		return tem[0];
	}
	
	private static void delay(int x) {
		try {
			Thread.sleep(x);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
