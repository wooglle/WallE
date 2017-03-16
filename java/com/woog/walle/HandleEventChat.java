package com.woog.walle;

public class HandleEventChat implements Runnable {
	private String message;
	
	public static void main(String arg){
		HandleEventChat handle = new HandleEventChat();
		handle.setMessage(arg);
		Thread thread = new Thread(handle);
		thread.setName("WooG Chats Handle Thread");
		thread.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(this.message);
//		System.runFinalization();
//		System.gc();
	}
	
	public void setMessage(String arg){
		this.message = arg;
	}
}
