package com.woog.walle.ai;

import com.woog.walle.APIInventory;
import com.woog.walle.WallE;
import com.woog.walle.additional.IDebug;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//@SideOnly(Side.CLIENT)
public class ActionBase {
	Minecraft mc = FMLClientHandler.instance().getClient();
	public boolean doing, pause = false, running;
	private String start = getActName() + " has start!";
	private String stop = getActName() + " has stopped!";
	private String suspend = getActName() + " has suspended!";
	private String resume = getActName() + " has resume!";
//	private Item[] holds = this.getHolds(); 
	private String[] holds = this.getHolds();
	
	public static ActionUtil util;
	
	protected ActionBase() {
		if(!WallE.acts.isEmpty()) {
			if((!isRepeat()) & WallE.acts.get(0).getActName().equals(this.getActName())) {
				return;
			}else{
				WallE.acts.get(0).pause = true;
			}
		}else{
			this.pause = false;
		}
		
		util = new ActionUtil();
		doing = true;
		WallE.acts.add(0, this);
		new Monitor().start();
		if(showMsg()) {
			IDebug.PrintYellow(start);
//			mc.player.addChatComponentMessage(start);
		}
	}
	
	protected String[] getHolds() {
		return null;
	}
	
	/**
	 * 手持物重装
	 */
	protected void holdStuff() {
		while(this.condition() && this.canStuff()) {
//			System.out.println("  " + mc.gameSettings.keyBindAttack.isKeyDown());
			if(mc.gameSettings.keyBindAttack.isKeyDown()) {
				mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
				new Stuffing(this.holds);
				mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
			}else{
				new Stuffing(this.holds);
			}
			delay(500);
		}
//		delay(1000);
	}
	
	/**
	 * 是否需要重装
	 * @return true:重装,  false:不重装
	 */
	private boolean canStuff() {
		if(APIInventory.getHeldItem() == null) {
			return true;
		}else{
			boolean flag = false;
			for(int i = 0; i < this.holds.length; i++) {
				if(APIInventory.getHeldItem().getItem().getUnlocalizedName().equals(this.holds[i])) {
					flag = true;
				}
			}
			if(flag && APIInventory.getHeldItemDamage() < WallE.minItemDamage) {
				return true;
			}else if(!flag) {
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 设置是否显示默认动作消息
	 * @return
	 */
	protected boolean showMsg() {
		return true;
	}
	
	/**
	 * 设置动作是否可以连续重复添加
	 * @return
	 */
	protected boolean isRepeat() {
		return false;
	}
	
	/**
	 * 设置动作名称
	 * @return
	 */
	public String getActName() {
		return "[have not set name]";
	}

	/**
	 * 循环判断条件
	 * @return
	 */
	protected boolean condition() {
		return true;
	}
	
	/**
	 * 动作
	 */
	protected void action() {}
	
	/**
	 * 暂停
	 */
	protected void goOn() {
		util = new ActionUtil();
		doing = true;
		pause = false;
		Monitor mon = new Monitor();
		mon.setName("Action-" + getActName());
		mon.start();
//		mc.player.addChatComponentMessage(resume);
		IDebug.PrintYellow(resume);
	}
	
	/**
	 * 延时
	 * @param msec
	 */
	protected void delay(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected class Monitor extends Thread {
		public void run() {
			action();
//			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】 " + WallE.acts.get(0).getActName() + "  pause: " + WallE.acts.get(0).pause + "  " + WallE.acts.isEmpty() + "  " + WallE.acts.size()));
//			FMLClientHandler.instance().getClient().isGamePaused();
//			delay(1500);
//			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】 " + WallE.acts.get(0).getActName() + "  pause: " + WallE.acts.get(0).pause + "  " + WallE.acts.isEmpty() + "  " + WallE.acts.size()));
//			System.out.println("【infobase】 " + WallE.acts.get(0).getActName() + "  " + WallE.acts.get(0).pause);
			if(!WallE.acts.isEmpty()) {
				if(mc.world != null & !pause) {
//					mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】 " + WallE.acts.get(0).getActName() + "已被删除"));
					if(WallE.acts.get(0).getActName().equals(getActName())) {
						WallE.acts.remove(0);
						util.allDefault();
						if(showMsg()) {
//							mc.player.addChatComponentMessage(stop);
							IDebug.PrintRed(stop);
						}
						doing = false;
						if(WallE.acts != null && !WallE.acts.isEmpty()) {
							WallE.acts.get(0).goOn();
						}
					}
				}else if(WallE.acts.size() > 1){
					pause = true;
					util.allDefault();
//					mc.player.addChatComponentMessage(suspend);
					IDebug.PrintYellow(suspend);
					if(mc.gameSettings.keyBindAttack.isKeyDown()) {
						mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
					}
				}
			}else{
				System.out.println("ActionBase ERRO!!!!!!!!!");
			}
			
//			util.allDefault();
//			if(mc.thePlayer != null) {
//				if(showMsg()) {
//					System.out.println("【STOP】" + getActName() + "  " + this.toString());
//					mc.thePlayer.addChatComponentMessage(stop);
//				}
//				doing = false;
//				if(!WallE.acts.isEmpty()) {
//					WallE.acts.get(0).goOn();
//				}
//			}
		}
	}
}
