package com.woog.walle;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class APIChunk {
	private static Minecraft mc = Minecraft.getMinecraft();
//	获取最近的方块（高于地面1-5格）
//	判断方块是否可挖（相邻是否岩浆，下方是否为空（当方块不高于player时））
//	方块是否挖得到（true则next，flase则move）
//	获取指定方块
//	获取目标方块周围的指定方块
//	取得落脚点
//	move（定向导航，自动避障，路径回归）
	public static void chunkForDig(){
//		规格：下1，上2+3+1，前1+1，左1+3+1，右1+3+1
//		竖直方向×左右×前后；挖掘体积：5×7×1；危险探测体积：7×9×2
		Chunk chunk = mc.world.getChunkFromChunkCoords(mc.player.chunkCoordX, mc.player.chunkCoordZ);
	}
	
	public static V3D nearestBlock(){
		
		return null;
	}
	
	public static Block getBlockEyesOn(){
		return Minecraft.getMinecraft().world.getBlockState(
				new BlockPos(APIPlayer.viewX(), APIPlayer.viewY(), APIPlayer.viewZ())).getBlock();
	}
	
	public static Block getBlock(V3D blockV3D){
		return Minecraft.getMinecraft().world.getBlockState(blockV3D.toBlockPos()).getBlock();
	}
}
