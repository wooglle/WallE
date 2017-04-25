package com.woog.walle;

import java.awt.AWTException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import com.woog.walle.additional.Crypto;
import com.woog.walle.additional.IDebug;
import com.woog.walle.additional.LoginPassword;
import com.woog.walle.ai.AIManager;
import com.woog.walle.ai.ActionBase;
import com.woog.walle.ai.DigChunk;
import com.woog.walle.ai.Digging;
import com.woog.walle.ai.FaceTo;
import com.woog.walle.ai.Fishing;
import com.woog.walle.ai.HeldReload;
import com.woog.walle.ai.LogInIsland;
import com.woog.walle.ai.Stuffing;
import com.woog.walle.ai.Walk;
import com.woog.walle.ai.WalkOneStep;
import com.woog.walle.ai.WalkToIsland;
import com.woog.walle.chatrobot.HttpRequest;
import com.woog.walle.util.Log;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.actors.threadpool.Arrays;
import scala.collection.immutable.List;

public class EventChatClass 
{
	public static String[] lastTenChat = new String[10];
	private static Minecraft mc = Minecraft.getMinecraft();
	private int last = 0;
	private static String chat;
	private static String myName = getMyName();
	private String[] buff;
	public ActionBase actionCurrent;
	public AIManager ai;
	public String sendMsg = null;
	public static int skillTime = 999;
//	private long t0 = new Date().getTime();
	private String info = "";
	private String id = "";
//	private ArrayList<String> guests = new ArrayList(20);
	public static ArrayList<NameTime> guests = new ArrayList(20);
	private ArrayList<String> blackList = new ArrayList(20);
//	private int chattime = 0;
	protected static LinkedList<NameInfo> chats = new LinkedList();
	static long chattime0 = 0;
	private boolean isCheckingGuests = false;
	
	@SubscribeEvent
	public void chatReceived(ClientChatReceivedEvent event) throws AWTException{
		chat = event.getMessage().getUnformattedText().toLowerCase();
//		chat = event.getMessage().getFormattedText().toLowerCase();
		if(last > 9) {
			last -= 9;
		}
		lastTenChat[last] = chat;
		last++;
//		loggedMsg();
//		System.out.println("          " + event.getMessage().getSiblings().get(0));
		HandleEventChat.main(chat);
		
		
		if(mc.isSingleplayer()) {
//			HandleEventChat.main(chat);
//			mc.player.sendChatMessage("123");
//			test();
		}else{
//			HandleEventChat.main(chat);
//			handle();
			
//			System.out.println("          " + Minecraft.getMinecraft().getConnection().getNetworkManager().getRemoteAddress());
//			System.out.println("          " + Minecraft.getMinecraft().getConnection().getNetworkManager().getRemoteAddress().toString());
//			String temp = Minecraft.getMinecraft().getConnection().getNetworkManager().getRemoteAddress().toString();
//			String[] tem = temp.split("/");
//			System.out.println("          " + tem[0]);
		}
	}

	//传送到地标 s
	private static String getMyName() {
		return FMLClientHandler.instance().getClient().getSession().getUsername().toLowerCase();
	}
	
	private void test() {
//		chat = "          红包随机口令:    [ 苦心孤诣 ]";
		if(chat.matches(".*stop.*(fish|魚|鱼).*$")) {
			actionCurrent.doing = false;
		}else if(chat.matches(".*(fish|魚|鱼).*$")) {
			actionCurrent = new Fishing();
		}else if(chat.matches("^.*stop.*dig.*")) {
			actionCurrent.doing = false;
		}else if(chat.matches("^.*dig.*")){
			actionCurrent = new DigChunk();
		}else if(chat.matches("^.*crypto.*")){
			Crypto.enCrypt("sadhkjasdnklj");
		}else if(chat.matches("^.*face.*$")){
			V3D tar = new V3D(1, 0, 0);
			new FaceTo(APIPlayer.getFootWithOffset().add(tar), 1);
		}else if(chat.matches("^.*walk.*$")) {
			RayTraceTarget rtt = new RayTraceTarget(10, false);
			Walk w= new Walk();
			rtt.info();
			System.out.println("[][][][] " + WallE.way.size());
			w.run();
//			new FaceTo(WallE.way.get(0));
		}else if(chat.matches("^.*(stop|停|cancel).*(cobble|石|stone).*$")) {
			WallE.acts.get(0).doing = false;
		}else if(chat.matches("^.*(cobble|石|stone).*$")) {
			new Digging();
		}else if(chat.matches("^.*(held|hold).*$")) {
			new HeldReload();
		}else if(chat.matches("^.*(change).*$")) {
			System.out.println("+++++++++++ Change");
			new Stuffing();
		}else if(chat.matches("^.*t$")) {
//			mc.world.sendQuittingDisconnectingPacket();
//			System.out.println(APIInventory.boxInventory);
//			APIInventory.printStacks(APIInventory.getPacketInventory());
			new Stuffing();
//			System.out.println("【STUFF】" + APIInventory.getPacketInventory() );
		}else if(chat.matches("^.*aistop$")) {
			ai.setDoing(false);
		}else if(chat.matches("^.*ai$")) {
			ai = new AIManager();
		}else if(chat.matches("^.*eyeson$")){
			System.out.println(APIChunk.getBlockEyesOn());
//			buff = chat.split(">\\s|\\]");
//			HttpRequest http = new HttpRequest(buff[1], "woog");
//			System.out.println("++++++" + http.answer);
		}else if(chat.matches("^.*light.*off$")) {
			mc.gameSettings.gammaSetting = 1.0F;
		}
		else if(chat.matches("^.*light.*on$")) {
			mc.gameSettings.gammaSetting = 100F;
		}
	}

	private boolean isPlayerChat(String chat) {
//		❤Huier❤ <[Elite]dmc2002> 恩
//		<[居民]why100> @dmc2002 真的么？？
//		[[居民]foobar -> 我] walle woog shutdown
//		[world]<[云隙]foobar> sadsd
//		❤3033877517❤ **Happy <[CEO]sxl> @jianb 你给我，我给你钱，我CEO，我骗你，你靠，我封号，我不值
//		^.*(<|\\[)\\W+\\w+(\\s\\W{2}\\s\\W)?(>|]) .*$
//		System.out.println(chat);
		if(chat.matches("^\\[.*\\]?<\\[.*\\].*>.*$")) {
			return true;
		}else if(chat.matches("^(❤.*❤ )?(\\S+\\s)?<\\[.*\\]?\\w+>.*$")) {
			return true;
		}else if(chat.matches("^\\[(❤.*❤ )?\\[.*\\]\\w+ -> 我\\].*$")) {
			return true;
		}
		return false;
	}
	
	private String[] getNameChat(String chat) {
//		[[居民]foobar -> 我] walle woog shutdown
//		[[居民]foobar -> 我] wewe
		String[] nc = new String[2];
		String cha;
		if(chat.matches("^\\[.*\\]?<\\[.*\\].*>.*$")) {
			String[] tem = chat.split("]|>\\s", 4);
			nc[0] = tem[2];
			nc[1] = tem[3];
		}else if(chat.matches("^(❤.*❤ )?<\\[.*\\]?\\w+>.*$")){
			String[] tem = chat.split(">\\s|\\]");
			nc[0] = tem[1];
			nc[1] = tem[2];
		}else if(chat.matches("^(❤.*❤ )?(\\S+\\s)<\\[.*\\]?\\w+>.*$")) {
			String[] tem = chat.split(">\\s|\\]");
			nc[0] = tem[1];
			nc[1] = tem[2];
		}else if(chat.matches("^\\[(❤.*❤ )?\\[.*\\]\\w+ -> 我\\].*$")) {
			String[] tem = chat.split("\\[\\[\\S+\\]| -> 我\\]\\s");
			nc[0] = tem[1];
			nc[1] = tem[2];
			System.out.println(nc[0] + "   4  " + nc[1]); 
		}else if(chat.matches("^\\[(❤.*❤ )?(\\S+\\s)\\[\\S+\\]\\w+ -> 我\\].*$")) {
			String[] tem = chat.split("\\[\\S+\\]| -> 我\\]\\s");
			nc[0] = tem[1];
			nc[1] = tem[2];
			System.out.println(nc[0] + "   5  " + nc[1]); 
		}
		return nc;
	}
	
	private boolean isGuest(String name) {
		for(int i = 0; i < guests.size(); i++) {
			if(name.equals(guests.get(i).name)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isBlack(String name) {
		for(int i = 0; i < blackList.size(); i++) {
			if(name.equals(blackList.get(i))) {
				return true;
			}
		}
		blackList.add(name);
		return false;
	}
	
	public static boolean isLogined() {
		if(lastTenChat[0] != null) {
			for(int i = 0; i < lastTenChat.length; i++) {
				if(lastTenChat[i] != null && lastTenChat[i].matches("^.*(登陆成功|登录成功|logged).*$")) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void delay(int x) {
		try {
			Thread.sleep(x);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private class ChatRobot extends Thread {
		@Override
		public void run() {
			HttpRequest http = new HttpRequest(info, id);
			int max = 25;
//			System.out.println(http.answer);专业智能被调戏型聊天机器人，可同时多人聊天，100元5分钟，付款上不封顶，付款不足100是侮辱我会被退款。。。
			String str = http.answer.replace("<br>", "");
			ArrayList<String> s = new ArrayList(20);
			for(int i = 0; i * max < str.length(); i++) {
				int start = i * max;
				if(str.length() - start  > max) {
					s.add(0, str.substring(start, start + max));
				}else{
					s.add(0, str.substring(start, str.length()));
				}
			}
			int n = s.size();
			for(int i = 0; i < n; i++) {
				try{
					chats.add(0, new NameInfo(id, s.get(0)));
					s.remove(0);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
//			mc.player.sendChatMessage("/bukkit:msg " + id + " " + http.answer);
			while(new Date().getTime() - chattime0 < 10000) {
				delay(1000);
			}
			new SendChats().start();
		}
	}
	
	private class SendChats extends Thread {
		@Override
		public void run() {
			while(chats.size() > 0) {
				if(new Date().getTime() - chattime0 > 6000) {
					mc.player.sendChatMessage(chats.get(0).name + "◤" + chats.get(0).info);
					chats.remove(0);
					chattime0 = new Date().getTime();
				}else{
					delay(1000);
				}
			}
		}
	}
	
	private class CheckGuests extends Thread {
		@Override
		public void run() {
			isCheckingGuests = true;
			while(!guests.isEmpty()) {
				long now = new Date().getTime();
				int size = guests.size();
				for(int i = 0; i < guests.size(); i++) {
					if(guests.get(i).time < now) {
						chats.add(0, new NameInfo(guests.get(i).name, "客户▓ " + guests.get(i).name + " ▓的服务已经结束。"));
						guests.remove(i);
						i--;
					}
				}
				if(guests.size() < size) {
					new SendChats().start();
				}
				try {
					this.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			isCheckingGuests = false;
		}
	}
	
	class NameTime {
		public String name;
		public long time;
		public NameTime(String name, long time) {
			this.name = name;
			this.time = time;
		}
		public void setTime(long newtime) {
			this.time = newtime;
		}
	}
	
	private class NameInfo {
		public String name;
		public String info;
		public NameInfo(String name, String info) {
			this.name = name;
			this.info = info;
		}
	}
	
	private class SendMsg extends Thread {
		@Override
		public void run() {
			Random random = new Random();
			int r = random.nextInt(4800) % 3801 + 1000;
			delay(r);
//			mc.player.sendChatMessage("/qhb " + sendMsg);
		}
	}
	
	private class LogtoIsland extends Thread {
		@Override
		public void run() {
			delay(5000);
			EventGuiClass.conecting = false;
			WalkToIsland.walking = false;
			new WalkToIsland();
		}
	}
	
	private class GetPlayerName extends Thread {
		@Override
		public void run() {
			while(mc.player == null) {
				delay(200);
			}
			////////////////////////////////////////////
			////////////////////////////////////////////
			////////////////////////////////////////////
			////////////////////////////////////////////
			WallE.username = mc.player.getDisplayName().toString();
			myName = mc.player.getDisplayName().toString().toLowerCase();
		}
	}
	
	private class SeaIsland extends Thread{
		public void run() {
			if(true) {
				delay(3000);
				mc.gameSettings.keyBindInventory.setKeyBindState(mc.gameSettings.keyBindInventory.getKeyCode(), true);
				delay(3000);
			}
		}
	}
}