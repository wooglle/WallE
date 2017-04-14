package com.woog.walle;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class APIChunk {
	private static Minecraft mc = Minecraft.getMinecraft();

	/**
	 * 获取玩家视线落点的方块
	 * @return
	 */
	public static Block getBlockEyesOn() {
		return getBlock(new V3D(APIPlayer.viewX(), APIPlayer.viewY(), APIPlayer.viewZ()));
	}
	
	/**
	 * 获取指定位置的方块
	 * @param blockV3D
	 * @return
	 */
	public static Block getBlock(V3D blockV3D) {
		return Minecraft.getMinecraft().world.getBlockState(blockV3D.toBlockPos()).getBlock();
	}
	
	/**
	 * 判断指定位置是否原木
	 * @param pos
	 * @return
	 */
	public static boolean isLog(V3D pos) {
		return getBlock(pos).getRegistryName().toString().equals("minecraft:log");
	}
	
	/**
	 * 判断指定位置是否空的
	 * @param pos
	 * @return
	 */
	public static boolean isEmpty(V3D pos) {
		return getMaterial(pos).equals(Material.AIR);
	}
	
	/**
	 * 判断指定位置是否可以安全站立
	 * 判断条件， 指定位置非空、非植物、非电路、非草、非地毯、非雪， 上一格非空， 指定位置为火、水、岩浆，
	 * @param pos
	 * @return
	 */
	public static boolean isSafeForStand(V3D pos) {
		Material posMaterial = getMaterial(pos);
		if(!(posMaterial.equals(Material.AIR) | posMaterial.equals(Material.PLANTS) | posMaterial.equals(Material.CIRCUITS)
				| posMaterial.equals(Material.GRASS) | posMaterial.equals(Material.CARPET))) {
			return false;
		}
		Material upperMaterial = getMaterial(pos.addY(1));
		if(!upperMaterial.equals(Material.AIR)) {
			return false;
		}
		if(posMaterial.equals(Material.FIRE) | posMaterial.equals(Material.WATER) | posMaterial.equals(Material.LAVA)) {
			return false;
		}
		Material lowerMaterial = getMaterial(pos.addY(-1));
		if(lowerMaterial.equals(Material.FIRE) | lowerMaterial.equals(Material.WATER) | lowerMaterial.equals(Material.LAVA)
				| lowerMaterial.equals(Material.AIR)) {
			return false;
		}
		return true;
	}
	
	public static Material getMaterial(V3D pos) {
		return getBlock(pos).getDefaultState().getMaterial();
	}
	
	public static boolean canStand(V3D pos) {
		if(isEmpty(pos)) {
			if(isEmpty(pos.addY(1))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取一个玩家头部高度平面内指定距离范围内的木头方块
	 * @return
	 */
	public static V3D getWood() {
		V3D[] neighbor = APIPlayer.getHeadPos().getNeighborByDistance(5);
		for(V3D pos: neighbor) {
			if(isLog(pos)) {
				return pos;
			}
		}
		return null;
	}
	
	/**
	 * 判断指定位置的方块是否被挖掉
	 * @param blockPos
	 * @return
	 */
	public static boolean isBlockBroke(V3D blockPos) {
		return false;
	}
}
