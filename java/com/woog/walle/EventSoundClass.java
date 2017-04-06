package com.woog.walle;

import com.woog.walle.ai.Feed;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventSoundClass {
	private String ename;
	private static Minecraft mc = WallE.mc;
	private static String oldname = null;
	
	@SubscribeEvent
	public void PlaySound(PlaySoundEvent e){
		ename = e.getName();
		if(!ename.equals(oldname)) {
//			System.out.println("【Sound】   " + ename);
			oldname = ename;
		}
		if(ename.matches("^.*player.*hurt.*$")){
//			System.out.println("【hurt】 ===" + FMLClientHandler.instance().getClient().thePlayer.getFoodStats().needFood());
//			System.out.println("【hurt】 ===" + APIPlayer.getHealth() + "   " + mc.player.getMaxHealth());
			if(mc.player.getFoodStats().needFood() | APIPlayer.getHealth() < mc.player.getMaxHealth()) {
				if(!WallE.acts.isEmpty()) {
//					System.out.println("【hurt】 +++++++++++++++++++ ");
					new Feed();
				}
			}
		}else if(ename.matches("^random.splash$")){
			new RightClick().start();
		}else if(ename.matches("^block.grass.break$")){
//			System.out.println("【收了一个艹艹】 +++++++++++++++++++ ");
		}
	}
	
	class Hurt extends Thread {
		@Override
		public void run() {
			System.out.println("【hurt】  " +this.hashCode());
			if(Minecraft.getMinecraft().player.getFoodStats().needFood() | APIPlayer.getHealth() < Minecraft.getMinecraft().player.getMaxHealth()) {
				if(!WallE.acts.isEmpty()) {
					new Feed();
				}
			}
		}
	}
	
	class RightClick extends Thread {
		@Override
		public void run(){
			try {
				this.sleep(50);
				mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
				this.sleep(50);
				mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
