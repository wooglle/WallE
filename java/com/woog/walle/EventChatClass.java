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

	private void handle() {
//		this.myName = "WooG";
//		chat = "<[居民]WooG> walle test sadjhd ads fish";
//		chat = "[[居民]foobar -> 我] walle woog shutdown";
//		[world]<[云隙]WooG> test
//		chat = "~~   player506 愉快的加入了岛屿生活~";
//		"^(\\S+)?\\s?<(\\[[^]]+\\])?[^>]+> .*"
//		String regx = "^.*(<|\\[)\\W+\\w+(\\s\\W{2}\\s\\W)?(>|]) .*$";
//		if(chat.matches("^.*(<|\\[)\\W+\\w+(\\s\\W{2}\\s\\W)?(>|]) .*$")) {
//		[列兵] [资源世界]<【市民】WooG> walle ai
		String chatem = chat;
		if(chatem.matches("^.*<【市民】woog> walle ai stop.*$")) {
			ai.setDoing(false);
		}else if(chatem.matches("^.*<【市民】woog> walle ai.*$")) {
			System.out.println("+++++++++++++++++++++++++++++++++++");
			ai = new AIManager();
		}
		
		if(this.isPlayerChat(chatem)) {
			this.getNameChat(chatem);
			buff = this.getNameChat(chat);
			String chatName = buff[0];
			String chatInfo = buff[1];
//			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】 " + this.myName + "  " + buff[1]));
			if(buff.length >= 2 ) {
				if(chatName.equals(this.myName)) {
					if(chatInfo.matches("^(wall-?e|瓦力).*$")) {
						if(chatInfo.matches("^.*(stop|停).*$")) {
							if(this.actionCurrent != null){
								AIManager.doing = false;
								actionCurrent.doing = false;
							}
							if(!ai.doing) {
								ai.setDoing(false);
							}
						}else if(chatInfo.matches("^.*(fish|魚|鱼).*$")) {
							actionCurrent = new Fishing();
						}else if(chatInfo.matches("^.*(dig|挖|掘).*$")) {
							actionCurrent = new DigChunk();
						}else if(chatInfo.matches("^.*(cobble|石|stone).*$")) {
							actionCurrent = new Digging();
						}else if(chatInfo.matches("^.*(aistop).*$")) {
							ai.setDoing(false);
						}else if(chatInfo.matches("^.*(ai).*$")) {
							ai = new AIManager();
						}
					}else if(chatInfo.matches("^disconnect$")) {
						mc.world.sendQuittingDisconnectingPacket();
					}else if(chatInfo.matches("^test$")) {
						System.out.println(APIPlayer.getFootWithOffset().getRadiantSquare(APIPlayer.getFootWithOffset(), 3));
//						System.out.println("重生点：" + mc.world.getSpawnPoint()); 你猜~~~~~
//						System.out.println("  " + mc.world.getSpawnPoint().getDistanceSquared(0, 117, 0));
//						System.out.println(APIInventory.getHeldItem().getItem().getUnlocalizedName());
//						new Stuffing();
//						mc.world.setBlockState(pos, state);
					}else if(chatInfo.matches("^eyeson$")) {
						mc.player.sendChatMessage(APIChunk.getBlockEyesOn().toString());
//						if(APIChunk.getBlockEyesOn() instanceof BlockBush){
//							mc.player.sendChatMessage(APIChunk.getBlockEyesOn().toString());
//						}
					}else if(WallE.myfriends.indexOf(chatName) > 0) {
					}
				}else if((buff.length >= 2) && this.isGuest(chatName)){
//					System.out.println("++++++++++++++11111111" + guests);
					this.id = chatName;
					this.info = chatInfo;
					new ChatRobot().start();
				}else if((!buff[1].equals(myName)) && this.isGuest(chatName)){
//					System.out.println("++++++++++++++3333333333" + guests);
					this.id = chatName;
					this.info = chatInfo;
					new ChatRobot().start();
					chattime0 = new Date().getTime();
				}else if(chatName.equals(WallE.master)) {
//					System.out.println("+++++++++++++");
					int last = buff.length - 1;
					if(chatInfo.matches(this.myName + ".*(wall-?e|瓦力)?.*$")) {
						if(chatInfo.matches("^.*(stop|停).*$")) {
							if((!WallE.acts.isEmpty())) {
								WallE.acts.get(0).doing = false;
								mc.player.sendChatMessage("Wall-E 远程控制已接受指令，停止" + WallE.acts.get(0).getActName());
							}
						}else if(chatInfo.matches("^.*(fish|魚|鱼).*$")) {
							actionCurrent = new Fishing();
							mc.player.sendChatMessage("Wall-E 远程控制已接受指令，开始钓鱼！！！！！");
						}else if(chatInfo.matches("^.*(cancel|取消)(power|关|shut).*(off|机|down).*$")) {
							try {
								Runtime.getRuntime().exec("shutdown -a");
							} catch (IOException e) {
								e.printStackTrace();
							}
							mc.player.sendChatMessage("Wall-E 远程控制已接受指令，取消关机计划！！！！！");
						}else if(chatInfo.matches("^.*(power|关|shut)(off|机|down).*$")) {
							mc.player.sendChatMessage("Wall-E 远程控制已接受指令，60秒后关闭计算机！！！！！");
							try {
								Runtime.getRuntime().exec("shutdown -s -t 60");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}else if(chatInfo.matches(".*(hold|切换).*")) {
							mc.player.inventory.changeCurrentItem(1);
						}else if(chatInfo.matches(".*(walk|走).*")) {
							new WalkOneStep();
						}
					}else if(chatInfo.matches("^.*(当前客户|chat\\s?guest).*$")) {
						String guestsName = "";
						for(int i = 0; i < this.guests.size(); i++) {
							Date da = new Date(this.guests.get(i).time);
							guestsName += this.guests.get(i).name +"=" + da.getHours() + ":" + da.getMinutes() + ":" + da.getSeconds() + "  ";
						}
						mc.player.sendChatMessage("当前客户： " + guestsName);
					}else if(chatInfo.matches("^r.*$")) {
						mc.player.sendChatMessage(chatInfo.substring(1, chatInfo.length()));
					}else if(chatInfo.matches("^c.*$")) {
						String command = chatInfo.substring(1,chatInfo.length());
						mc.player.sendChatMessage("Wall-E 远程控制已执行命令： " + command);
					}
				}
			}
		}else if(chatem.matches("^.+/login.*登.*$")) {
			if(!WallE.acts.isEmpty()) {
				WallE.acts.get(0).pause = true;
			}
			if(LoginPassword.GetPassword() != null) {
				mc.player.sendChatMessage("/l " + LoginPassword.GetPassword());
			}else{
//				mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText() .app("§e§o【Wall-E】 没有获取密码，请手动登陆!!!"));
				IDebug.PrintYellow("没有获取到密码，请手动登陆!!!");
			}
		}else if(chatem.matches("^.*(成功登录|登陆成功|登录成功|logged|欢迎回来！).*$")) {
			if(!WallE.acts.isEmpty()) {
				WallE.acts.get(0).pause = true;
			}
			actionCurrent = new LogInIsland();
			
		}else if(chatem.matches("^~+\\s+" + myName + "\\s+[\\u4E00-\\u9FA5]+~+$")) {
			if(actionCurrent != null && (actionCurrent.getActName().equals("Automatic Log In Island") | actionCurrent.getActName().equals("Automatic Walking to The Island"))) {
				actionCurrent.doing = false;
			}
		}else if(chatem.matches(".*红包\\S+口令:\\s+\\[.*\\]")){
			buff = chatem.split("\\[|\\]");
			sendMsg = buff[1].replace("\\S", "");
//			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】" + sendMsg));
//			mc.player.sendChatMessage("/qhb " + sendMsg);
//			new SendMsg().start();
		}else if(chatem.matches("^你.*")) {
			if(chatem.matches("^你太累了暂时无法使用该技能.*")) {
				//你太累了暂时无法使用该技能.(1s)
				String[] buf = chat.split("\\(|s");
				EventChatClass.skillTime = Integer.valueOf(buf[1]);
			}else if(chatem.matches("你.*技能已经可以再次使用了！")) {
				//你的 超级碎石机 技能已经可以再次使用了！
				EventChatClass.skillTime = 0;
			}
		}else if(chatem.matches("^\\*\\*.*已激活\\*\\*$")) {
//			**你拿起了你的矿锄**
//			**超级碎石机已激活**
			EventChatClass.skillTime = 999;
//			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】." + "已释放技能 。。。"));
			IDebug.PrintYellow("已释放技能 。。。");
		}else if(chatem.matches("已从.* 接收\\W*\\d.*")) {
//			~~~~专业陪聊机器人， 100元5分钟，输入/pay woog 100 付款~~~~
			String[] str = chatem.split("\\]|( 接收\\W?)");
			int n = (int)((Double.valueOf(str[2])) / 100);
			if(n > 0) {
				long delta = n * 5 * 60000;
				if(this.isGuest(str[1])) {
					mc.player.sendChatMessage("/pay " + str[1] + " " + str[2]);
				}else{
					if(n > 250) {
						mc.player.sendChatMessage("/pay " + str[1] + " " + str[2]);
					}else{
						this.guests.add(0, new NameTime(str[1], new Date().getTime() +  delta));
					}
					chats.add(new NameInfo(str[1], "已收到来自" + str[1] + "的" + str[2] + "元钱$_$，将服务" + n * 5 + "分钟。(智能收费聊天，100元5分钟！！！)"));
					new SendChats().start();
				}
				
				if(!this.isCheckingGuests) {
					new CheckGuests().start();
				}
			}else{
				if(this.isBlack(str[1])) {
				}else{
					chats.add(new NameInfo(str[1], "已退款" + str[2] + "元至" + str[1] + "，100块钱都不给我。。。(智能收费聊天，100元5分钟， 下次小于100将没收哦)"));
					mc.player.sendChatMessage("/pay " + str[1] +" " + str[2]);
					new SendChats().start();
					chattime0 = new Date().getTime();
				}
			}
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
	
	private boolean isVIP() {
		myName = "woog";
		if(this.myName != null) {
			for(int i = 0; i < WallE.vip.length; i++) {
				if(this.myName.toLowerCase().equals(WallE.vip[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void chatlog(String msg) {
		Date nowtime = new Date();
		SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SS");
		Log.chatlog(nowtime + "	" +msg);
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