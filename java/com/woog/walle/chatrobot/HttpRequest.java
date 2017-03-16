package com.woog.walle.chatrobot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.protocol.HTTP;

import scala.util.parsing.json.JSONObject;


public class HttpRequest {
	private static final String server = "http://www.tuling123.com/openapi/api";
	private static final String key = "d1e7b6f695dcf15d8301043436821509";
	public static String info;
	public static String playerID;
	public static String answer;
	
	public HttpRequest(String info, String playerID) {
		this.info = URLEncoder.encode(info);
		this.playerID = playerID;
		sendGet();
	}
	
	public static void sendGet() {
		try {
			String getURL = "http://www.tuling123.com/openapi/api?key=" + key + "&info=" + info + "&userid=" + playerID;
			
			URL url = new URL(getURL);
			HttpURLConnection conect = (HttpURLConnection) url.openConnection();
			conect.setDoOutput(true);
			conect.connect();
			
			OutputStreamWriter out = new OutputStreamWriter(conect.getOutputStream(), "UTF-8");
//			System.out.println(url);
			// 取得输入流，并使用Reader读取
	        BufferedReader reader = new BufferedReader(new InputStreamReader(conect.getInputStream(), "utf-8"));
	        StringBuffer sb = new StringBuffer();
	        String line = "";
	        
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	        reader.close();
	        
	        // 断开连接
	        conect.disconnect();
	        String s = sb.toString();
	        String[] ss = s.split(":");
	        String sss = ss[2].substring(1, ss[2].length() - 2);
	        answer = sss;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
