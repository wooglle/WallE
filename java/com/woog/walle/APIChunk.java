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
		System.out.println("          " + getBlock(pos).getDefaultState().getMaterial().equals(Material.AIR));
		return getBlock(pos).getDefaultState().getMaterial().equals(Material.AIR);
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
