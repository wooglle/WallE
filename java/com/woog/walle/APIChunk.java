package com.woog.walle;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class APIChunk {
	private static Minecraft mc = Minecraft.getMinecraft();

	/**
	 * 获取晚间视线落点的方块
	 * @return
	 */
	public static Block getBlockEyesOn() {
		return Minecraft.getMinecraft().world
				.getBlockState(new BlockPos(APIPlayer.viewX(), APIPlayer.viewY(), APIPlayer.viewZ())).getBlock();
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
	 * 获取一个玩家头部高度平面内指定距离范围内的木头方块
	 * @return
	 */
	public static V3D getWood() {
		V3D[] neighbor = APIPlayer.getHeadPos().getNeighborByDistance(5);
		for(V3D pos: neighbor) {
			if(getBlock(pos).getRegistryName().toString().matches("^.*minecraft:log.*")) {
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
