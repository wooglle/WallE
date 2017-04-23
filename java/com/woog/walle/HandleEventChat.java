package com.woog.walle;

import java.io.IOException;
import java.util.Date;

import com.woog.walle.additional.GLDraw;
import com.woog.walle.additional.IChunkFlooring;
import com.woog.walle.additional.IDebug;
import com.woog.walle.additional.LoginPassword;
import com.woog.walle.additional.RebuildTree;
import com.woog.walle.ai.AIManager;
import com.woog.walle.ai.DigChunk;
import com.woog.walle.ai.Digging;
import com.woog.walle.ai.Fishing;
import com.woog.walle.ai.LogInIsland;
import com.woog.walle.ai.Stuffing;
import com.woog.walle.ai.Walk2There;
import com.woog.walle.ai.WalkOneStep;

import net.minecraft.client.Minecraft;
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
		// TODO Auto-generated method stub
		
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
						System.out.println("     " + WallE.acts.size());
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
		} else if (this.isPlayerChat()) {
			String[] buff = this.getNameChat();
			String chatName = buff[0];
			String chatInfo = buff[1];
//			System.out.println(chatName + "     +++++       " + chatInfo + "  " + chatInfo.matches("^\\W+$"));
//			System.out.println("-----" + chatInfo.substring(0, this.myName.length()));
			if(chatName != null && this.isController(chatName)) { 	// 控制指令
				if (chatInfo.length() > myName.length() && chatInfo.substring(0, this.myName.length()).equals(myName)) { // 指令格式：name + 指令
					if (chatInfo.matches("^.*test.*$")) {
//						new Stuffing();
//						APIInventory.printInventory();
						
//						for(int i = 0; i < mc.player.inventory.mainInventory.size(); i++) {
//							System.out.println( i + "       " + mc.player.inventory.mainInventory.get(i));
//						}
						
//						short short1 = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
//						int slot = 44;
//						ItemStack itemstack = mc.player.inventoryContainer.slotClick(slot, 6, ClickType.SWAP, mc.player);
//						System.out.println("ssssssssss  " + itemstack);
//						FMLClientHandler.instance().getClient().getConnection().sendPacket(new CPacketClickWindow(
//								mc.player.inventoryContainer.windowId, slot, 6, ClickType.SWAP, 
//								itemstack, short1));
						
//						System.out.println("CHAT    " + new V3D(0, 0, 0).getCrossCube(1));
						
//						new RayTraceTarget(new V3D(-991, 27, -1051), false);
//						new AstarFindWay(APIPlayer.getFootWithOffset(), new V3D(-1005, 26, -1053), false);
//						new RayTraceTarget(new V3D(-1005, 26, -1053), false); 
//						new RayTraceTarget(new V3D(873, 22, 252), false); 
//						System.out.println("CHAT   " + APIChunk.isSafeForStand(new V3D(-991, 26, -1053)));
//						new Walk2There();
						
						
//						System.out.println("       " + APIChunk.isSafeForStand(new V3D(872, 21, 247)));
						
//						EventTickClass.doCheckTrees = false;
//						WallE.runtime.isCuttingTree = false;
//						EventTickClass.doCheckTrees = true;
//						new Stuffing("hatchet");
						
						GLDraw.drawLine(200, 100, 20, 20, 1342177280);
						
//						System.out.println("       " + new V3D(0, 0, 0).getSquareEdge(10));
//						System.out.println("       " + new IChunkFlooring().square);
						
//						System.out.println("【T】" + APIInventory.getHeldItem());
//						System.out.println("【T】" + APIInventory.getHeldItem().getItem().getRegistryName().getResourceDomain());
//						System.out.println("【T】" + APIInventory.getHeldItem().getItem().getRegistryName().getResourcePath());
						
//						System.out.println("【T】" + APIPlayer.getFacing().getDirectionVec());
//						new Stuffing("pickaxe");
//						V3D pos = new V3D(-990, 26, -1050);
//						System.out.println("          " + mc.player.openContainer.getInventory());
						
//						WallE.way.clear();
//						new RayTraceTarget(new V3D(100, 4, -1837), false);
//						new RayTraceTarget(new V3D(-990, 28, -1048), false);
//						Walk2There w2t = new Walk2There();
//						new Walk2There();
//						WallE.acts.add(w2t);
						
//						mc.getBlockRendererDispatcher().getBlockModelShapes();
						
//						new KeepOnWatch(new V3D(-940, 26, -1015), 1);
						
					} else if (chatInfo.matches("^.*(stop|停).*$")) {
						if (WallE.currentAct != null) {
							AIManager.doing = false;
							WallE.currentAct.doing = false;
						}
						if (this.ai != null && ai.doing) {
							this.ai.setDoing(false);
						}
						if (!chatName.equals(myName)) {
							mc.player.sendChatMessage("Wall-E 远程控制已接受指令，停止AI." + WallE.acts.get(0).getActName());
						}
					} else if (chatInfo.matches("^.*(ai).*$")) {
						this.ai = new AIManager();
						if (!chatName.equals(myName)) {
							mc.player.sendChatMessage("Wall-E 远程控制已接受指令，开启AI！！！！！");
						}
					} else if (chatInfo.matches("^.*(fish|魚|鱼).*$")) {
						new Fishing();
						if (!chatName.equals(myName)) {
							mc.player.sendChatMessage("Wall-E 远程控制已接受指令，开始钓鱼！！！！！");
						}
					} else if (chatInfo.matches("^.*(dig|挖|掘).*$")) {
						new DigChunk();
					} else if (chatInfo.matches("^.*(cobble|石|stone).*$")) {
						new Digging();
					} else if (chatInfo.matches("^.*disconnect$")) {
						if (!chatName.equals(myName)) {
							mc.player.sendChatMessage("Wall-E 远程控制已接受指令，开始下线！！！！！");
						}
						mc.world.sendQuittingDisconnectingPacket();
					} else if (chatInfo.matches("^.*eyeson$")) {
						mc.player.sendChatMessage(APIChunk.getBlockEyesOn().toString());
					} else if (chatInfo.matches(".*(hold|切换).*")) {
						mc.player.inventory.changeCurrentItem(1);
					} else if (chatInfo.matches(".*(walk|走).*")) {
						new WalkOneStep();
					} else if(chatInfo.matches("^.*light.*off$")) {
						mc.gameSettings.gammaSetting = 1.0F;
					} else if(chatInfo.matches("^.*light.*on$")) {
						mc.gameSettings.gammaSetting = 100F;
					} else if (chatInfo.matches("^.*(当前客户|chat\\s?guest).*$")) {
						String guestsName = "";
						for (int i = 0; i < EventChatClass.guests.size(); i++) {
							Date da = new Date(EventChatClass.guests.get(i).time);
							guestsName += EventChatClass.guests.get(i).name + "=" + da.getHours() + ":"
									+ da.getMinutes() + ":" + da.getSeconds() + "  ";
						}
						mc.player.sendChatMessage("当前客户： " + guestsName);
					} else if (!chatName.equals(this.myName)) { // 远程控制专用指令
						if (chatInfo.matches("^r.*$")) {
							mc.player.sendChatMessage(chatInfo.substring(1, chatInfo.length()));
						} else if (chatInfo.matches("^.*(cancel|取消)(power|关|shut).*(off|机|down).*$")) {
							try {
								Runtime.getRuntime().exec("shutdown -a");
							} catch (IOException e) {
								e.printStackTrace();
							}
							mc.player.sendChatMessage("Wall-E 远程控制已接受指令，取消关机计划！！！！！");
						} else if (!chatName.equals(this.myName)
								&& chatInfo.matches("^.*(power|关|shut)(off|机|down).*$")) {
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
//			else if(chatName != null && chatInfo.length() > myName.length() && chatInfo.substring(0, this.myName.length()).equals(myName)) {
//				String msg = chatInfo;
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

	private String[] getNameChat() {
		// [[居民]foobar -> 我] walle woog shutdown
		// [[居民]foobar -> 我] wewe
		String[] nc = new String[2];
		nc[0] = null;
		nc[1] = this.message;
		if(this.message.matches("^<\\w+>\\s.*$")) {
			String[] tem = this.message.split("<|>\\s", 3);
//			for(String s : tem) {
//				System.out.println("      1111       " + s);
//			}
			nc[0] = tem[1];
			nc[1] = tem[2];
		}else if(this.message.matches("^.*\\[\\S*\\s\\w+\\]\\s\\S\\s.*$")) { 
			String[] tem = this.message.split("]|:|\\s", 7);
//			for(String s : tem) {
//				System.out.println("      222       " + s);
//			}
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
