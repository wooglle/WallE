package com.woog.walle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import io.netty.handler.codec.http.HttpResponse;

public class TuringChat {
	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
	String url = "";
	String key = "";
	String info;
	String id;
	public String answer;
	
	@SuppressWarnings("resource")
	public void postJson() {
		String request = url + "?key=" + key + "&info=" + info + "&userid=" + id;
		String apiUrl = "http://www.tuling123.com/openapi/api?key=" + key + "&info=";
		String param = "";
		try {
			param = apiUrl + URLEncoder.encode(info, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(param);
		try {
			post.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			StringEntity se;
			se = new StringEntity(param);
			se.setContentType(CONTENT_TYPE_TEXT_JSON);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
			post.setEntity(se);
			httpClient.execute(post);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
