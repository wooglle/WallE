package com.woog.walle;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;

public class APIInventory {
	public static Minecraft mc = FMLClientHandler.instance().getClient();
//	public static ItemStack[] myInventory = getPlayersInventory();
//	public static List boxInventory = getBoxInventory();
	
	/***
	 * 获取主要的库存（快捷栏 + 背包）
	 * @return
	 */
	public static NonNullList<ItemStack> getMainInventory() {
		if(mc.player == null) {
			return NonNullList.create();
		}else{
			return mc.player.inventory.mainInventory;
		}
	}
	
	/***
	 * 获取当前库存界面的所有格槽内的物品
	 * @return 从上到下，自左至右
	 */
	public static NonNullList<ItemStack> getCurrentInventory() {
		if(mc.player.openContainer != null) {
			return mc.player.openContainer.getInventory();
		}else{
			return NonNullList.create();
		}
	}
	
	/***
	 * 获取当前打开的箱子内的物品
	 * @return
	 */
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
	public static int getItemIndex(String name) {
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
	public static int getItemIndexByKeyword(String keyword) {
		NonNullList<ItemStack> is = getPacketInventory();
		for(int i = 9; i < 45; i++) {
			if(is.get(i).getItem().getUnlocalizedName().matches("^.*" + keyword + ".*")) {
				return i;
			}
		}
		return -1;
	}
	
	/***
	 * 根据给定的关键字获取背包里名称符合关键字的item
	 * @param keyword item的关键字
	 * @return null： 没有名称符合keyword的item， 或背包里没有符合的item
	 */
	public static Item getItemByKeyword(String keyword) {
		NonNullList<ItemStack> is = getPacketInventory();
		for(int i = 9; i < 45; i++) {
			Item b = is.get(i).getItem();
			if(b.getUnlocalizedName().matches("^.*" + keyword + ".*")) {
				return b;
			}
		}
		return null;
	}
	
	/***
	 * 判断item是否可用于破坏pos位置的block
	 * @param item 工具item
	 * @param pos 指定block的坐标
	 * @return
	 */
	public static boolean canItemBrokeBlock(Item item, V3D pos) {
		return item.canHarvestBlock(Minecraft.getMinecraft().world.getBlockState(pos.toBlockPos()));
	}
	
	/**
	 * 服务器上玩家背包及hotbar物品排列
	 * @return 0: 合成台输出， 1-4： 合成台, 5-8：装备栏， 9-35：背包从上到下， 36-44：hotbar， 45：副手
	 */
	public static NonNullList<ItemStack> getPacketInventory() {
		if(FMLClientHandler.instance().getClient().player != null){
			NonNullList<ItemStack> out = NonNullList.<ItemStack>withSize(46, ItemStack.EMPTY);
			for(int i = 5; i < 9; i++) {
				out.set(i, mc.player.inventory.armorInventory.get(i - 5));
			}
			for(int i = 9; i < 36; i++) {
				out.set(i, APIInventory.getMainInventory().get(i));
			}
			for(int i = 36; i < 45; i++) {
				out.set(i, APIInventory.getMainInventory().get(i - 36));
			}
			out.set(45, mc.player.inventory.offHandInventory.get(0));
			return out;
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
	
	/**
	 * 获取当前手持的物品的ItemStack
	 * @return
	 */
	public static ItemStack getHeldItem() {
		if(mc.player == null) {
			return null;
		}else{
			return mc.player.getHeldItem(EnumHand.MAIN_HAND);
		}
	}
	
	/***
	 * 获取当前手持物品的名称
	 * @return
	 */
	public static String getHeldName() {
		if(mc.player == null) {
			return null;
		}else{
			return getHeldItem().getDisplayName();
		}
	}
	
	/***
	 * 获取当前手持物品的剩余耐久度
	 * @return
	 */
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
	
	/**
	 * 获取指定物品的剩余耐久度
	 * @param item
	 * @return
	 */
	public static int getItemDamage(ItemStack item) {
		if(mc.player == null | item == null) {
			return 0;
		}else{
			return item.getMaxDamage() - item.getItemDamage();
		}
	}
	
	/***
	 * 获取当前主手手持物品的ID
	 * @return
	 */
	public static int getHeldID() {
		if(mc.world != null && mc.player.getHeldItem(EnumHand.MAIN_HAND) != null) {
			return Item.getIdFromItem(mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem());
		}else{
			return 0;
		}
	}
	
	/***
	 * 把手持物品切换为快捷栏里的其他物品
	 * @param slotID 0-8
	 */
	public static void setHeldItem(int slotID) {
		if(0 < slotID & slotID < mc.player.inventory.getHotbarSize()) {
			mc.player.inventory.setInventorySlotContents(slotID, (ItemStack)null);
		}
	}
	
	/**
	 * 获取主库存内恢复饥饿值最大物品的索引号
	 * @return
	 */
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
	
	/**
	 * 从主库存内获取指定ID的物品的索引
	 * @param itemID 物品的ID
	 * @return
	 */
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
	
	/**
	 * 用于显示物品堆垛
	 * @param stack
	 */
	public static void printStacks(NonNullList<ItemStack> list){
		for(int i = 0; i < list.size(); i++){
			if(list.get(i) != ItemStack.EMPTY){
				System.out.printf(" %s: %s,", i, list.get(i));
			}
			if((i + 1) % 9 == 0) {
				System.out.printf(" \n");
			}
		}
	}
	
	/**
	 * 用于显示当前玩家的主库存及打开的箱子的库存
	 */
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
			System.out.println("[APIInventory]没有打开箱子货或箱子为空。");
		}
	}
}
