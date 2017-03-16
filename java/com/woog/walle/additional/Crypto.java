package com.woog.walle.additional;

import java.io.UnsupportedEncodingException;

public class Crypto {
	static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
	static private byte[] codes = new byte[256];
	static {
		for (int i = 0; i < 256; i++)  
			codes[i] = -1;  
		for (int i = 'A'; i <= 'Z'; i++)  
			codes[i] = (byte) (i - 'A');  
		for (int i = 'a'; i <= 'z'; i++)  
			codes[i] = (byte) (26 + i - 'a');  
		for (int i = '0'; i <= '9'; i++)  
			codes[i] = (byte) (52 + i - '0');  
		codes['+'] = 62;  
		codes['/'] = 63;  
	}
	
	public static String enCrypt(String str) {
		System.out.println("【Crypto】" + str);
		String encode = enBase64(str);
		String decode = deBase64(encode);
		
		char[] chars = str.toCharArray();
		
		String ascii = "";
		for(int i = 0; i < chars.length; i++) {
			ascii += "0" + Integer.toBinaryString(chars[i]);
		}
//		System.out.println("【Crypto】" + ascii);
		String binary38 = "";
		int l = ascii.length() % 3;
		int n = l == 0 ? l : l + 1;
		
        return null;
	}
	public static String enBase64(String str) {
		char[] chars = str.toCharArray();
		char[] out = new char[((chars.length + 2) / 3) * 4];
		for (int i = 0, index = 0; i < chars.length; i += 3, index += 4)
		{
			boolean quad = false;
			boolean trip = false;
			int val = (0xFF & (int) chars[i]);
			val <<= 8;
			if ((i + 1) < chars.length) {
				val |= (0xFF & (int) chars[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < chars.length) {
				val |= (0xFF & (int) chars[i + 2]);
				quad = true;
			}
			out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = alphabet[val & 0x3F];
			val >>= 6;
			out[index + 0] = alphabet[val & 0x3F];
		}
		System.out.println("【Crypto】" + String.valueOf(out));
		return String.valueOf(out);
	}
	
	static public String deBase64(String str) {
		char[] data = str.toCharArray();
		int len = ((data.length + 3) / 4) * 3;
		if(data.length > 0 && data[data.length - 1] == '=')
			--len;
		if(data.length > 1 && data[data.length - 2] == '=')
			--len;
		byte[] out = new byte[len];
		int shift = 0;
		int accum = 0;
		int index = 0;
		for(int ix = 0; ix < data.length; ix++) {
			int value = codes[data[ix] & 0xFF];
			if(value >= 0) {
				accum <<= 6;
				shift += 6;
				accum |= value;
				if(shift >= 8) {
					shift -= 8;
					out[index++] = (byte)((accum >> shift) & 0xff);
				}
			}
		}
		if(index != out.length)  
			throw new Error("miscalculated data length!");
		try {
			String s = new String(out, "ASCII");
			System.out.println("【Crypto】" + s);
			return s;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
    }
}
