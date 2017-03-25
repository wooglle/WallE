package com.woog.walle;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.client.FMLClientHandler;

public class APIInventory {
	public static Minecraft mc = FMLClientHandler.instance().getClient();
//	public static ItemStack[] myInventory = getPlayersInventory();
//	public static List boxInventory = getBoxInventory();
	
	public static NonNullList<ItemStack> getMainInventory() {
		if(mc.player == null) {
			return NonNullList.create();
		}else{
			return mc.player.inventory.mainInventory;
		}
	}
	
	public static List getCurrentInventory(){
		if(mc.player.openContainer != null) {
			return mc.player.openContainer.getInventory();
		}else{
			return null;
		}
	}
	
	public static IInventory getBoxInventory() {
		if(mc.player.openContainer != null && mc.player.openContainer instanceof ContainerChest) {
			ContainerChest con = (ContainerChest)mc.player.openContainer;
			return con.getLowerChestInventory();
		}else{
			return null;
		}
//		return mc.player.inventoryContainer.getInventory();
	}
	
	/**
	 * 通过item的全名查找背包中指定item的序号
	 * @param name Ex：item.fishingRod
	 * @return
	 */
	public static int getItem(String name) {
		NonNullList<ItemStack> is = getPacketInventory();
		for(int i = 0; i < is.size(); i++) {
			if(is.get(i) != ItemStack.EMPTY && is.get(i).getItem().getUnlocalizedName().equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 通过关键字查找背包中指定item的序号
	 * @param name Ex：item.fishingRod
	 * @return
	 */
	public static int getItemByKeyword(String keyword) {
		NonNullList<ItemStack> is = getPacketInventory();
		for(int i = 0; i < is.size(); i++) {
			if(is.get(i) != ItemStack.EMPTY && is.get(i).getItem().getUnlocalizedName().matches("^.*" + keyword + ".*")) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 服务器上玩家背包及hotbar物品排列
	 * @return 0-8： hotbar, 9-36：背包从上到下
	 */
	public static NonNullList<ItemStack> getPacketInventory() {
		if(FMLClientHandler.instance().getClient().player != null){
//			NonNullList<ItemStack> out = NonNullList.<ItemStack>withSize(45, ItemStack.EMPTY);
//			for(int i = 5; i < 9; i++){
//				out.set(i, mc.player.inventory.armorInventory.get(i - 5));
//			}
//			for(int i = 9; i < 45; i++){
//				out.set(i, mc.player.inventory.mainInventory.get(i - 9));
//			}
			return mc.player.inventory.mainInventory;
		}else{
			return null;
		}
	}
	
	/**
	 * 获取玩家全身物品
	 * 0-4：合成台， 5-8：脚-头， 9-45: 9*4从上到下
	 * @return
	 */
	public static NonNullList<ItemStack> getAllInventory() {
		if(FMLClientHandler.instance().getClient().player != null){
			NonNullList<ItemStack> out = NonNullList.<ItemStack>withSize(45, ItemStack.EMPTY);
			for(int i = 0; i < 5; i++){
				out.set(i, ItemStack.EMPTY);
			}
			for(int i = 5; i < 9; i++){
				out.set(i, mc.player.inventory.armorInventory.get(i - 5));
			}
			for(int i = 9; i < 45; i++){
				out.set(i, mc.player.inventory.mainInventory.get(i - 9));
			}
//			for(int i = 9; i < 36; i++){
//				out.set(i, mc.player.inventory.mainInventory.get(i));
//			}
//			for(int i = 36; i < 45; i++){
//				out.set(i, mc.player.inventory.mainInventory.get(i - 36));
//			}
			return out;
		}else{
			return null;
		}
	}
	
	public static ItemStack getHeldItem() {
		if(mc.player == null) {
			return null;
		}else{
			return mc.player.getHeldItem(EnumHand.MAIN_HAND);
		}
	}
	
	public static String getHeldName() {
		if(mc.player == null) {
			return null;
		}else{
			return getHeldItem().getDisplayName();
		}
	}
	
	public static int getHeldItemDamage() {
		if(mc.player == null) {
			return 0;
		}else{
			if(getHeldItem() == null) {
				return 0;
			}else{
				return APIInventory.getHeldItem().getMaxDamage() - APIInventory.getHeldItem().getItemDamage();
			}
		}
	}
	
	public static int getItemDamage(ItemStack item) {
		if(mc.player == null | item == null) {
			return 0;
		}else{
			return item.getMaxDamage() - item.getItemDamage();
		}
	}
	
	public static int getHeldID() {
		if(mc.world != null && mc.player.getHeldItem(EnumHand.MAIN_HAND) != null) {
			return Item.getIdFromItem(mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem());
		}else{
			return 0;
		}
	}
	
	public static void setHeldItem(int slotID) {
		if(0 < slotID & slotID < mc.player.inventory.getHotbarSize()) {
			mc.player.inventory.setInventorySlotContents(slotID, (ItemStack)null);
		}
	}
	
	public static int getFoodMax() {
		if(mc.player == null) {
			return -1;
		}
		float max = 0.0F, buff = 0.0F;
		int get = -1;
		for(int i = 0; i < 9; i++) {
			if(getMainInventory().get(i) != null && getMainInventory().get(i).getItemUseAction().equals(EnumAction.EAT)) {
				ItemFood f = (ItemFood) getMainInventory().get(i).getItem();
				buff = (float)f.getHealAmount(getMainInventory().get(i)) * f.getHealAmount(getMainInventory().get(i)) * 2.0F;
				if(buff > max & !getMainInventory().get(i).getUnlocalizedName().equals("item.fish.pufferfish.raw")) {
					max = buff;
					get = i;
				}
			}
		}
		return get;
	}
	
	public static int getItem(int itemID) {
		int x;
		for(int i = 0; i < getMainInventory().size(); i++) {
			if(i < 27) {
				x = i + 9;
			}else{
				x = i - 27;
			}
			if(APIInventory.getMainInventory().get(x) != null) {
				if(Item.getIdFromItem(getMainInventory().get(x).getItem()) == itemID && getMainInventory().get(x).getItemDamage() < 20) {
					return x;
				}
			}
		}
		return -1;
	}
	
	public static void printStacks(ItemStack[] stack){
		for(int i = 0; i < stack.length; i++){
			if(stack[i] != null){
				System.out.printf(" %s: %s,", i, stack[i]);
			}
			if((i + 1) % 9 == 0) {
				System.out.printf(" \n");
			}
		}
	}
	
	public static void printInventory() {
		System.out.printf("玩家库存 \n");
		for(int i = 0; i < getMainInventory().size(); i++) {
			if(getMainInventory().get(i) != null) {
				System.out.printf(" %s: %s,", i, getMainInventory().get(i));
			}
			if((i + 1) % 9 == 0) {
				System.out.printf(" \n");
			}
		}
		System.out.printf(" \n");
		IInventory box = APIInventory.getBoxInventory();
		if(box != null) {
			System.out.printf("箱子库存 \n");
			for(int i = 0; i < box.getSizeInventory(); i++) {
				ItemStack item = box.getStackInSlot(i);
				if(item != null) {
					System.out.printf(" %s: %s,", i, item.toString());
				}
				if((i + 1) % 9 == 0) {
					System.out.printf(" \n");
				}
			}
			System.out.printf(" \n");
		}else{
			System.out.println("没有打开箱子货或箱子为空。");
		}
	}
}
