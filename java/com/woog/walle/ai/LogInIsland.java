package com.woog.walle.ai;

import java.util.Date;

import com.woog.walle.APIInventory;
import com.woog.walle.EventChatClass;
import com.woog.walle.WallE;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;

public class LogInIsland extends ActionBase{
	
	@Override
	public String getActName() {
		return "Automatic Log In Island";
	}
	
	@Override
	public boolean condition() {
		return (mc.world != null) && doing && (!pause) &&(!isGetin());
	}
	
	public boolean isGetin() {
//		System.out.println("            " + mc.theWorld.getSpawnPoint());
		return mc.world.getSpawnPoint().distanceSq(0.0D, 143.0D, 70.0D) != 0.0F;
//		return mc.theWorld.getSpawnPoint().distanceSq(0.0D, 117.0D, 0.0D) != 0.0F;
	}
	
	public boolean isGetin2() {
		String[] list = EventChatClass.lastTenChat;
		for(int i = 0; i < list.length; i++) {
			if(list[i] != null) {
				if(list[i].matches("^.*愉快的加入了岛屿生活.*$")) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void action() {
		long t0 = new Date().getTime();
		long t1 = t0;
		int getID = -1;
		int n = 0;
		while(condition() && !(mc.currentScreen instanceof GuiChest)) {
			n++;
			delay(500);
			if(getID == -1) {
				for(int i = 0; i < APIInventory.getMainInventory().size(); i++) {
					if(APIInventory.getMainInventory().get(i) != null) {
						getID = i;
					}
				}
				if(n > 10) {
					WallE.acts.add(new WalkToIsland());
					return;
				}
			}else{
				delay(500);
				mc.player.inventory.currentItem = getID;
				mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
				delay(50);
				mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
			}
		}
		while(condition() & t1 - t0 < 60000) {
			delay(200);
			if(condition()) {
				if(!(mc.currentScreen instanceof GuiChest)) {
					mc.player.inventory.currentItem = getID;
					mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
					delay(50);
					mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
					delay(500);
				}else{
//					mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(1, 15, 1, 1, is, short1));
//					ContainerChest con = (ContainerChest)mc.thePlayer.openContainer;
//					String lowername = con.getLowerChestInventory().getName();
					if(APIInventory.getBoxInventory().getName().matches("^.*服务器快捷切换.*$")){
						short short1 = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
						ItemStack is = APIInventory.getBoxInventory().getStackInSlot(20);
						mc.getConnection().sendPacket(new CPacketClickWindow(mc.player.openContainer.windowId, 20, 1, ClickType.PICKUP, is, short1));
						delay(100);
					}
				}
			}
			t1 = new Date().getTime();
		}
//		if(!isGetin()) {
//			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】点击方块失败， 将走进海岛。。。"));
//			if(mc.currentScreen instanceof GuiChest) {
//				mc.displayGuiScreen(null);
//				delay(800);
//			}
//			WallE.acts.add(new WalkToIsland());
//		}
	}
}
