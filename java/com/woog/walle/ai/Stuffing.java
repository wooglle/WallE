package com.woog.walle.ai;

import com.woog.walle.APIInventory;
import com.woog.walle.WallE;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraftforge.fml.client.FMLClientHandler;

public class Stuffing{
	Minecraft mc = FMLClientHandler.instance().getClient();
	
	public String getActName() {
		return "Automatic Stuffing";
	}
	
	public Stuffing() {
		swapSlot2Hotbar(this.getItemInBackbag(APIInventory.getHeldItem().getItem().getUnlocalizedName()), mc.player.inventory.currentItem);
	}
	
	public Stuffing(String[] items){
		boolean isStuffed = false;
		for(int i = 0; i < items.length; i++){
			if(APIInventory.getItem(items[i]) > 0) {
				swapSlot2Hotbar(this.getItemInBackbag(items[i]), mc.player.inventory.currentItem);
				isStuffed = true;
			}
		}
		if(!isStuffed) {
			if(!WallE.acts.isEmpty()) {
				WallE.acts.get(0).doing = false;
				AIManager.doing = false;
			}
			mc.player.sendChatMessage("▁▂▃▄▅▆▇"+  "没有工具， Wall-E已停止工作▇▆▅▄▃▂▁"); 
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
	 * 获取背包中相同名字的物品
	 * @return 物品的在背包中的序号，-1表示没有可用的相同物品 
	 */
	private int getItemInBackbag(String hold){
		String currentItemName = hold;
		System.out.println("▄▅▆▇  " + hold);
		for(int i = 0; i < APIInventory.getPacketInventory().size(); i++){
			ItemStack item = APIInventory.getPacketInventory().get(i);
			if((item != null) && (i != mc.player.inventory.currentItem) && currentItemName.equals(item.getItem().getUnlocalizedName())){
				if(item.getUnlocalizedName().matches("^item.*$")){
					if(APIInventory.getItemDamage(item) > WallE.minItemDamage){
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
		if(iSlot < 0 | iSlot > APIInventory.getPacketInventory().size() - 1 | iHotbar < 0 | iHotbar > 9){
			return;
		}else{
//			System.out.println("【STUFF】 2222222222:" + iSlot + "  " + iHotbar);
			short short1 = mc.player.openContainer.getNextTransactionID(mc.player.inventory);
			FMLClientHandler.instance().getClient().getConnection().sendPacket(new CPacketClickWindow(
					mc.player.inventoryContainer.windowId, iSlot, iHotbar, ClickType.PICKUP, APIInventory.getMainInventory().get(9), short1));
		}
	}
}
