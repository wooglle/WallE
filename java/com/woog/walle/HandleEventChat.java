package com.woog.walle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.woog.walle.additional.GLDraw;
import com.woog.walle.additional.ICFarming;
import com.woog.walle.additional.ICLighting;
import com.woog.walle.additional.IChunkFlooring;
import com.woog.walle.additional.IChunkFlooring2;
import com.woog.walle.additional.IDebug;
import com.woog.walle.additional.LoginPassword;
import com.woog.walle.additional.RebuildTree;
import com.woog.walle.ai.AIManager;
import com.woog.walle.ai.CutTrees;
import com.woog.walle.ai.DigChunk;
import com.woog.walle.ai.Digging;
import com.woog.walle.ai.FaceTo;
import com.woog.walle.ai.Farming;
import com.woog.walle.ai.Fishing;
import com.woog.walle.ai.Flooring;
import com.woog.walle.ai.Lighting;
import com.woog.walle.ai.LogInIsland;
import com.woog.walle.ai.Stuffing;
import com.woog.walle.ai.Walk2There;
import com.woog.walle.ai.WalkOneStep;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.client.FMLClientHandler;

public class HandleEventChat implements Runnable {
	private static Minecraft mc = FMLClientHandler.instance().getClient();
	private static String myName = FMLClientHandler.instance().getClient().getSession().getUsername().toLowerCase();
	public static AIManager ai;
	private String message;
	// private String chatem;
	// private String[] buff;
	private String id;
	private String info;
	private long chattime0;
	protected boolean isLogged = false;

	public static void main(String arg) {
		HandleEventChat handle = new HandleEventChat();
		handle.setMessage(arg);
		Thread thread = new Thread(handle);
		thread.setName("Chats Handle Thread");
		thread.start();
	}

	private String Pertreat(String str) {
		String b = null;
		int n = str.indexOf("§");
		if (n == 0) {
			b = str.substring(2, str.length());
			b = Pertreat(b);
		} else if (n > 0) {
			b = str.substring(0, n) + str.substring(n + 2, str.length());
			b = Pertreat(b);
		} else {
			b = str;
		}
		return b;
	}

	@Override
	public void run() {
		if (message.matches("^.+/l.*(登|login).*$")) { // 非控制指令
			if (!WallE.acts.isEmpty()) {
				WallE.acts.get(0).pause = true;
			}
			String psw = LoginPassword.GetPassword();
			if (psw != null) {
				mc.player.sendChatMessage("/l " + psw);
			} else {
				IDebug.PrintYellow("没有获取到密码，请手动登陆!!!");
			}
		} else if (message.matches("^.*(已登录|成功登录|登陆成功|登录成功|logged|欢迎回来！).*$")) {
			System.out.println("+++++++++++++++++++++++++++++登录");
			if (LoginPassword.GetPassword() == null) {
				LoginPassword.DetectAndSavePassword();
			}
			if (!WallE.acts.isEmpty()) {
				WallE.acts.get(0).pause = true;
			}
			new LogInIsland();
		} else if (message.matches("^~+\\s+" + myName + "\\s+[\\u4E00-\\u9FA5]+~+$")) {
			if (WallE.currentAct != null && (WallE.currentAct.getActName().equals("Automatic Log In Island")
					| WallE.currentAct.getActName().equals("Automatic Walking to The Island"))) {
				WallE.currentAct.doing = false;
			}
		} else if (message.matches(".*红包\\S+口令:\\s+\\[.*\\]")) {
			// buff = message.split("\\[|\\]");
			// sendMsg = buff[1].replace("\\S", "");
			// mc.ingameGUI.getChatGUI().printChatMessage(new
			// ChatComponentText("§e§o【Wall-E】" + sendMsg));
			// mc.player.sendChatMessage("/qhb " + sendMsg);
			// new SendMsg().start();
		} else if (message.matches("^你.*")) {
			if (message.matches("^你太累了暂时无法使用该技能.*")) {
				// 你太累了暂时无法使用该技能.(1s)
				String[] buf = message.split("\\(|s");
				EventChatClass.skillTime = Integer.valueOf(buf[1]);
			} else if (message.matches("你.*技能已经可以再次使用了！")) {
				// 你的 超级碎石机 技能已经可以再次使用了！
				EventChatClass.skillTime = 0;
			}
		} else if (message.matches("^.*kq1 is resetting.*$")) {
			// **你拿起了你的矿锄**
			// **超级碎石机已激活**
//			100, 4, -1837
			if (!WallE.acts.isEmpty()) {
				new Thread(){public void run() {
					try {
//						WallE.acts.get(0).pause = true;
						sleep(10);
						WallE.way.clear();
						new RayTraceTarget(new V3D(100, 4, -1838), false);
//						Walk2There w2t = new Walk2There();
						new Walk2There();
//						WallE.acts.add(w2t);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}}.start();
			}
			// mc.ingameGUI.getChatGUI().printChatMessage(new
			// ChatComponentText("§e§o【Wall-E】." + "已释放技能 。。。"));
		} else if (message.matches("^\\*\\*.*已激活\\*\\*$")) {
			// **你拿起了你的矿锄**
			// **超级碎石机已激活**
			EventChatClass.skillTime = 999;
			// mc.ingameGUI.getChatGUI().printChatMessage(new 谁能发一条信息？：woog你好
			// ChatComponentText("§e§o【Wall-E】." + "已释放技能 。。。"));
			IDebug.PrintYellow("已释放技能 。。。");
		} else if(message.matches("^.*死亡信息.*woog.*$")) {	//[死亡信息] WooG 被 Timb_b 使用 风花雪月~~~惟丶吾伤 杀死了！
			System.out.println("88888888888888888888888888");
			delay(1000);
			mc.player.sendChatMessage("/back");
//			mc.player.sendChatMessage("/kq");
		} else if (this.isPlayerChat()) {
			String[] buff = this.getNameChat2();
			String chatName = buff[0];
			String chatInfo = buff[1];
//			System.out.println(chatName + "     +++++       " + chatInfo + "  " + chatInfo.matches("^\\W+$"));
//			System.out.println("-----" + chatInfo.substring(0, this.myName.length()));
			if(chatName != null && this.isController(chatName)) { 	// 控制指令
				String[] str = chatInfo.split(" ");
				if(str.length > 1) {
					String strName = str[0];
					String strInfo = "";
					for(int i = 1; i < str.length; i++) {
						strInfo += str[i] + " ";
					}
					System.out.println("Chat   +" + strName +"+   +" + strInfo + "+   " + myName + " " + strName.equals(myName));
					if (strName.equals(myName)) { // 指令格式：name + 指令
						if (strInfo.matches("^.*test.*$")) {
//							System.out.println("     " + APIChunk.canHavest(APIPlayer.getEyesOn()));
//							System.out.println("     " + APIChunk.isEmpty(new V3D(884, 21, 214)));
//							ICFarming icf = new ICFarming();
//							System.out.println("     " + EnumFacing.WEST.rotateY().equals(EnumFacing.NORTH));
							new Farming();
							
//							new FaceTo(new V3D(-983, 26, -1067), EnumFacing.UP, 2);
//							System.out.println("     " + V3DHelper.getUDLRFB(APIPlayer.getFootWithOffset()));
//							System.out.println(APIChunk.getBlock(APIPlayer.getFootWithOffset().addY(-1)).getUnlocalizedName());
							
							
						} else if (strInfo.matches("^.*(stop|停).*$")) {
							if (WallE.currentAct != null) {
								AIManager.doing = false;
								WallE.currentAct.doing = false;
								EventTickClass.doCheckTrees = false;
								WallE.runtime.isCuttingTree = false;
							}
							if (this.ai != null && ai.doing) {
								this.ai.setDoing(false);
							}
							if (!chatName.equals(myName)) {
								mc.player.sendChatMessage("Wall-E 远程控制已接受指令，停止AI." + WallE.acts.get(0).getActName());
							}
						} else if (strInfo.matches("^.*(ai).*$")) {
							this.ai = new AIManager();
							if (!chatName.equals(myName)) {
								mc.player.sendChatMessage("Wall-E 远程控制已接受指令，开启AI！！！！！");
							}
						} else if (strInfo.matches("^.*(fish|魚|鱼).*$")) {
							new Fishing();
							if (!chatName.equals(myName)) {
								mc.player.sendChatMessage("Wall-E 远程控制已接受指令，开始钓鱼！！！！！");
							}
						} else if (strInfo.matches("^.*(dig|挖|掘).*$")) {
							new DigChunk();
						} else if (strInfo.matches("^.*(cobble|石|stone).*$")) {
							new Digging();
						} else if (strInfo.matches("^.*(floor|铺地).*$")) {
							new Flooring();
						}  else if (strInfo.matches("^.*(cuttree|砍树).*$")) {
							EventTickClass.doCheckTrees = true;
						} else if (strInfo.matches("^.*(lighting|插火把).*$")) {
							new Lighting();
						} else if (strInfo.matches("^.*disconnect$")) {
							if (!chatName.equals(myName)) {
								mc.player.sendChatMessage("Wall-E 远程控制已接受指令，开始下线！！！！！");
								mc.world.sendQuittingDisconnectingPacket();
							}
						} else if (strInfo.matches("^.*eyeson$")) {
							mc.player.sendChatMessage(APIChunk.getBlockEyesOn().toString());
						} else if (strInfo.matches(".*(hold|切换).*")) {
							mc.player.inventory.changeCurrentItem(1);
						} else if (strInfo.matches(".*(walk|走).*")) {
							new WalkOneStep();
						} else if(strInfo.matches("^.*light.*off$")) {
							mc.gameSettings.gammaSetting = 1.0F;
						} else if(strInfo.matches("^.*light.*on$")) {
							mc.gameSettings.gammaSetting = 1000F;
						} else if (strInfo.matches("^.*(当前客户|chat\\s?guest).*$")) {
							String guestsName = "";
							for (int i = 0; i < EventChatClass.guests.size(); i++) {
								Date da = new Date(EventChatClass.guests.get(i).time);
								guestsName += EventChatClass.guests.get(i).name + "=" + da.getHours() + ":"
										+ da.getMinutes() + ":" + da.getSeconds() + "  ";
							}
							mc.player.sendChatMessage("当前客户： " + guestsName);
						} else if (!chatName.equals(this.myName)) { // 远程控制专用指令
							if (strInfo.matches("^r.*$")) {
								mc.player.sendChatMessage(strInfo.substring(1, strInfo.length()));
							} else if (strInfo.matches("^.*(cancel|取消)(power|关|shut).*(off|机|down).*$")) {
								try {
									Runtime.getRuntime().exec("shutdown -a");
								} catch (IOException e) {
									e.printStackTrace();
								}
								mc.player.sendChatMessage("Wall-E 远程控制已接受指令，取消关机计划！！！！！");
							} else if (!chatName.equals(this.myName)
									&& strInfo.matches("^.*(power|关|shut)(off|机|down).*$")) {
								mc.player.sendChatMessage("Wall-E 远程控制已接受指令，60秒后关闭计算机！！！！！");
								try {
									Runtime.getRuntime().exec("shutdown -s -t 60");
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
//			else if(chatName != null && strInfo.length() > myName.length() && strInfo.substring(0, this.myName.length()).equals(myName)) {
//				String msg = strInfo;
//				HttpRequest http = new HttpRequest(msg, chatName);
//				String str = http.answer.replace("<br>", "");
//				mc.player.sendChatMessage("@" + chatName + "，" + str);
//			}
		} 
	}

	private boolean isController(String name) {
		if (name.equals(this.myName)) {
			return true;
		}
		for (String maste : WallE.master) {
			if (name.equals(maste)) {
				return true;
			}
		}
		return false;
	}

	private boolean isPlayerChat() {
		// ❤Huier❤ <[Elite]dmc2002> 恩
		// <[居民]why100> @dmc2002 真的么？？
		// [[居民]foobar -> 我] walle woog shutdown
		// [world]<[云隙]foobar> sadsd
		// ❤3033877517❤ **Happy <[CEO]sxl> @jianb 你给我，我给你钱，我CEO，我骗你，你靠，我封号，我不值
		// ☪56 [lv.49 ding_d] ♂ : 还好我附魔台弄得早 不然要累死。
		// ^.*(<|\\[)\\W+\\w+(\\s\\W{2}\\s\\W)?(>|]) .*$
		if (this.message.matches("^.*[<\\[]\\S*\\s?\\w+[>\\]].*")) {
//			System.out.println("+++++++++++++++++++++++++++++匹配   |" + message);
			return true;
		}
		return false;
	}
	
	private String[] getNameChat2() {
		String[] b = new String[2];
		String msg = message;
		Pattern p1 = Pattern.compile("[<\\[](.*)[>\\]]");	//(.*)为贪婪模式,最长匹配； (.*?)为非贪婪模式，最短匹配
		Matcher m1 = p1.matcher(msg);
		String tName = "", tChat = "";
		if(m1.find()) {
			tName = m1.group(1);
			tName = tName.replaceAll("(<.*>)|(\\[.*\\])", "");
			String[] names = tName.split(" ");
			for(String nam : names) {
				if(nam.matches("^[a-z0-9_]+$")) {
					b[0] = nam;
				}
			}
			tChat = msg.substring(m1.end());
			tChat = tChat.replaceAll("[\\W\\u4e00-\\u9fa5]\\s", "");
			while(tChat.startsWith(" ")) {
				tChat = tChat.substring(1, tChat.length());
			}
			b[1] = tChat;
//				System.out.println(msg );
//				System.out.println("		【" + b[0] + "】	【" + tChat + "】");
		}
		return b;
	}

	private String[] getNameChat() {
		// [[居民]foobar -> 我] walle woog shutdown
		// [[居民]foobar -> 我] wewe
		String[] nc = new String[2];
		nc[0] = null;
		nc[1] = this.message;
		if(this.message.matches("^<\\w+>\\s.*$")) {
			String[] tem = this.message.split("<|>\\s", 3);
			nc[0] = tem[1];
			nc[1] = tem[2];
		}else if(this.message.matches("^.*\\[\\S*\\s\\w+\\]\\s\\S\\s.*$")) { 
			String[] tem = this.message.split("]|:|\\s", 7);
			nc[0] = tem[2];
			nc[1] = tem[6];
		}else if (this.message.matches("^\\[.*\\]?<\\[.*\\].*>.*$")) {
			String[] tem = this.message.split("]|>\\s", 4);
			nc[0] = tem[2];
			nc[1] = tem[3];
		} else if (this.message.matches("^(❤.*❤ )?<\\[.*\\]?\\w+>.*$")) {
			String[] tem = this.message.split(">\\s|\\]");
			nc[0] = tem[1];
			nc[1] = tem[2];
		} else if (this.message.matches("^(❤.*❤ )?(\\S+\\s)<\\[.*\\]?\\w+>.*$")) {
			String[] tem = this.message.split(">\\s|\\]");
			nc[0] = tem[1];
			nc[1] = tem[2];
		} else if (this.message.matches("^\\[(❤.*❤ )?\\[.*\\]\\w+ -> 我\\].*$")) {
			String[] tem = this.message.split("\\[\\[\\S+\\]| -> 我\\]\\s");
			nc[0] = tem[1];
			nc[1] = tem[2];
			// System.out.println(nc[0] + " 4 " + nc[1]);
		} else if (this.message.matches("^\\[(❤.*❤ )?(\\S+\\s)\\[\\S+\\]\\w+ -> 我\\].*$")) {
			String[] tem = this.message.split("\\[\\S+\\]| -> 我\\]\\s");
			nc[0] = tem[1];
			nc[1] = tem[2];
		}
//		System.out.println(nc[0] + "   5  " + nc[1]);
		return nc;
	}

	public static boolean isLogined() {
		if (EventChatClass.lastTenChat[0] != null) {
			for (int i = 0; i < EventChatClass.lastTenChat.length; i++) {
				if (EventChatClass.lastTenChat[i] != null
						&& EventChatClass.lastTenChat[i].matches("^.*(登陆成功|登录成功|logged).*$")) {
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

	public void setMessage(String arg) {
		this.message = arg.replaceAll("§\\S", "");
	}
}
