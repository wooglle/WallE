package com.woog.walle.ai;

import com.woog.walle.EventChatClass;
import com.woog.walle.additional.IDebug;

import net.minecraft.client.gui.inventory.GuiChest;

public class WalkToIsland extends ActionBase{
	public static boolean walking = false;
	@Override
	public String getActName() {
		return "Automatic Walking to The Island";
	}
	
	@Override
	public boolean condition() {
		return (mc.world != null) && (doing & !isGetin());
	}
	
	public static boolean isWarp() {
		String[] list = EventChatClass.lastTenChat;
		for(int i = 0; i < list.length; i++) {
			if(list[i] != null) {
				if(list[i].matches("^.*传送到地标\\s\\S+$")) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isGetin() {
		return mc.world.getSpawnPoint().distanceSq(0.0D, 117.0D, 0.0D) != 0.0D;
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
	
	private void a1() {
		delay(3000);
		mc.player.inventory.currentItem = 0;
		delay(500);
		mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
		delay(50);
		mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
		
		if(mc.currentScreen instanceof GuiChest) {
			GuiChest container = (GuiChest)mc.currentScreen;
			short short1 = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
//			mc.thePlayer.sendChatMessage("its GuiChest!!!!!");
			delay(3000);
//			ItemStack itemstack = mc.thePlayer.openContainer.slotClick(p_78753_2_, p_78753_3_, p_78753_4_, p_78753_5_);
			container.inventorySlots.inventoryItemStacks.get(0);
//			mc.getNetHandler().addToSendQueue(new C0EPacketClickWindow(p_78753_1_, p_78753_2_, p_78753_3_, p_78753_4_, itemstack, short1));;
		}
		
		mc.player.sendChatMessage(mc.player.inventoryContainer.getInventory().toString());
		
//		mc.player.sendChatMessage(mc.getNetHandler().toString());
		
//		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】 " + this.isWarp()));
//		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】 " + isGetin() + "  " + doing + " C: " + this.condition()));
		IDebug.PrintYellow(this.isWarp() + "");
		IDebug.PrintYellow(isGetin() + "  " + doing + " C: " + this.condition());
	}
	
	private void a2() {
		while(this.condition() & !this.isWarp()) {
			delay(2000);
			if(this.condition()) {
				mc.player.sendChatMessage("/warp l");
			}
			delay(1000);
		}
		int n = 0;
		while(this.condition() & n < 20) {
			n++;
			delay(120);
			util.setMovement(1.0F, true, false);
			delay(150);
			util.setMovement(1.0F, false, false);
		}
		while(this.condition()) {
			delay(200);
			util.setMovement(-1.0F, false, false);
			delay(500);
			util.setMovement(1.0F, false, false);
			delay(600);
		}
		util.setMovement(0.0F, false, false);
	}
	
	@Override
	public void action() {
		this.walking = true;
		a2();
	}
}
