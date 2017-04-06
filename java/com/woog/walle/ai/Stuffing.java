package com.woog.walle.ai;

import com.woog.walle.APIInventory;
import com.woog.walle.APIPlayer;
import com.woog.walle.WallE;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.client.FMLClientHandler;

public class Stuffing{
	Minecraft mc = FMLClientHandler.instance().getClient();
	
	public String getActName() {
		return "Automatic Stuffing";
	}
	
	public Stuffing() {
//		swapSlot2Hotbar(this.getItemInBackbag(APIInventory.getHeldItem().getItem().getUnlocalizedName()), mc.player.inventory.currentItem);
	}
	
	public Stuffing(String itemKeyword){
		boolean isStuffed = false;
		int n = APIInventory.getItemIndexByKeyword(itemKeyword);
		if(n >= 0) {
//			swapSlot2Hotbar(n, mc.player.inventory.currentItem);
			swapSlot2Hotbar(this.getItemInBackbagByKeyword(itemKeyword), mc.player.inventory.currentItem);
//			if(n < 9) {
//				mc.player.inventory.currentItem = n;
//			}else{
//				swapSlot2Hotbar(this.getItemInBackbagByKeyword(itemKeyword), mc.player.inventory.currentItem);
//			}
			isStuffed = true;
		}
		if(!isStuffed) {
			if(!WallE.acts.isEmpty()) {
				WallE.acts.get(0).doing = false;
				AIManager.doing = false;
			}
//			mc.player.sendChatMessage("▁▂▃▄▅▆▇"+  "背包没有" + itemKeyword + "， Wall-E已停止工作▇▆▅▄▃▂▁"); 
		}
	}
	
	public void action() {
//		【纯受】虾米Mickey 2016/4/27 14:33:33
//		发包提交换位请求
//		【纯受】虾米Mickey 2016/4/27 14:33:50 
//		直接换造成不同步
//		swapSlot2Hotbar(this.getSameItemInBackbag(), mc.thePlayer.inventory.currentItem);
	}
	
	/**
	 * 获取背包中具有相同关键字的物品
	 * @return 物品的在背包中的序号，-1表示没有可用的工具
	 */
	private int getItemInBackbagByKeyword(String keyword){
		String currentItemName = keyword;
//		System.out.println("     " + APIInventory.getPacketInventory().size());
		NonNullList<ItemStack> packet = APIInventory.getPacketInventory();
		for(int i = 9; i < 45; i++){
			ItemStack item = packet.get(i);
			if(i != mc.player.inventory.currentItem && item.getItem().getUnlocalizedName().matches("^.*" + keyword + ".*")){
				if(item.getUnlocalizedName().matches("^item.*$")){
					if(APIInventory.getItemDamage(item) > WallE.minItemDamage){
						System.out.println("[Shuffing]切换工具：  " + keyword + "   " + i);
						return i;
					}
				}else{
					return i;
				}
			}
		}
		if(!WallE.acts.isEmpty()) {
			WallE.acts.get(0).doing = false;
			AIManager.doing = false;
		}
		mc.player.sendChatMessage("▁▂▃▄▅▆▇"+ keyword + "已耗尽， Wall-E已停止工作▇▆▅▄▃▂▁"); 
		return -1;
	}
	
	/**
	 * 获取背包中相同名字的物品
	 * @return 物品的在背包中的序号，-1表示没有可用的相同物品 
	 */
	private int getItemInBackbag(String hold){
		String currentItemName = hold;
		NonNullList<ItemStack> packet = APIInventory.getPacketInventory();
		for(int i = 0; i < packet.size(); i++){
			ItemStack item = packet.get(i);
			if(i != mc.player.inventory.currentItem && currentItemName.equals(item.getItem().getUnlocalizedName())){
				if(item.getUnlocalizedName().matches("^item.*$")){
					if(APIInventory.getItemDamage(item) > WallE.minItemDamage){
						System.out.println("▄▅▆▇  " + hold + "                " + i);
						return i;
					}
				}else{
					return i;
				}
			}
		}
		if(!WallE.acts.isEmpty()) {
			WallE.acts.get(0).doing = false;
			AIManager.doing = false;
		}
		mc.player.sendChatMessage("▁▂▃▄▅▆▇"+ hold + "已耗尽， Wall-E已停止工作▇▆▅▄▃▂▁"); 
		return -1;
	}
			
	private String getNameKeyword(String str){
		str.toUpperCase();
		return "";
	}
	
	private boolean isSameItem(String Keyword, String targe){
		
		return false;
	}
	
	private void swapSlot2Hotbar(int iSlot, int iHotbar){
		if(iSlot < 0 | iSlot > 45 | iHotbar < 0 | iHotbar > 9){
			return;
		}else{
			short short1 = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
			ItemStack itemstack = mc.player.inventoryContainer.slotClick(iSlot, iHotbar, ClickType.SWAP, mc.player);
			FMLClientHandler.instance().getClient().getConnection().sendPacket(new CPacketClickWindow(
					mc.player.inventoryContainer.windowId, iSlot, iHotbar, ClickType.SWAP, 
					itemstack, short1));
		}
	}
}
