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
	private String toolsKeyword = this.getToolsKeyword();
	
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
		WallE.currentAct = this;
//		new Monitor().start();
		Monitor mon = new Monitor();
		mon.setName("Action-" + getActName());
		mon.start();
		if(showMsg()) {
			IDebug.PrintYellow(start);
//			mc.player.addChatComponentMessage(start);
		}
	}
	
	/**
	 * 动作所使用工具的关键字
	 * @return
	 */
	protected String getToolsKeyword() {
		return null;
	}

	/**
	 * 手持物重装
	 */
	protected void holdStuff() {
		if(this.canStuff()) {
			boolean change = false;
			if(mc.gameSettings.keyBindAttack.isKeyDown()) {
				change = true;
				mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
			}
			while(this.condition() && this.canStuff()) {
//				new Stuffing(this.holds);
				new Stuffing(this.toolsKeyword);
				delay(100);
			}
			if(change && !mc.gameSettings.keyBindAttack.isKeyDown()) {
				mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
			}
		}
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
			if(APIInventory.getHeldItem().getItem().getUnlocalizedName().matches("^.*" + toolsKeyword + ".*")) {
				flag = true;
			}
			if(flag && APIInventory.getHeldItem().isItemDamaged() && APIInventory.getHeldItemDamage() < WallE.minItemDamage) {
				return true;
			} else if(!flag) {
				return true;
			} else {
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
		return this.doing && !this.pause;
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
		new Monitor().start();
//		mc.player.addChatComponentMessage(resume);
		if(this.showMsg()) {
			IDebug.PrintYellow(resume);
		}
	}
	
	/**
	 * 延时
	 * @param msec
	 */
	protected void delay(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected class Monitor extends Thread {
		public void run() {
			this.setName("Action-" + getActName());
			action();
			if(!WallE.acts.isEmpty()) {
				if(mc.world != null & !pause) {
//					mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】 " + WallE.acts.get(0).getActName() + "已被删除"));
					if(WallE.acts.get(0).getActName().equals(getActName())) {
						WallE.acts.remove(0);
						util.allDefault();
						if(showMsg()) {
							IDebug.PrintRed(stop);
						}
						doing = false;
						if(WallE.acts != null && !WallE.acts.isEmpty()) {
							WallE.acts.get(0).goOn();
						}
					}
				}else if(WallE.acts.size() > 1 | pause) {
					pause = true;
					util.allDefault();
					if(showMsg()) {
						IDebug.PrintYellow(suspend);
					}
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
