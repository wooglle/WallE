package com.woog.walle;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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
	 * 获取指定位置的方块状态
	 * @param blockV3D
	 * @return
	 */
	public static IBlockState getBlockState(V3D blockV3D) {
		return Minecraft.getMinecraft().world.getBlockState(blockV3D.toBlockPos());
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
	 * 下一格为水、火、岩浆、仙人掌、空，均返回false。
	 * @param pos
	 * @return
	 */
	public static boolean isSafeForStand(V3D pos) {
		Block upperBlock = getBlock(pos.addY(1));
		Block posBlock = getBlock(pos);
//		Block lowerBlock = getBlock(pos);
		
		Material lowerMaterial = getMaterial(pos.addY(-1));
		if(!(getBlock(pos).canSpawnInBlock() & getBlock(pos.addY(1)).canSpawnInBlock())) {
			return false;
		}
		if(!lowerMaterial.isSolid()) {
			return false;
		}
//		System.out.println(" SAFE 2    "  + Material.LEAVES + "   " + Material.LEAVES + "   "  + "   " + lowerMaterial);
		
//		if(!upperMaterial.equals(Material.AIR)) {
//			return false;
//		}
//		if(!(posMaterial.equals(Material.AIR) | posMaterial.equals(Material.PLANTS) | posMaterial.equals(Material.CIRCUITS)
//				| posMaterial.equals(Material.GRASS) | posMaterial.equals(Material.CARPET))) {
//			return false;
//		}
//		if(posMaterial.equals(Material.FIRE) | posMaterial.equals(Material.WATER) | posMaterial.equals(Material.LAVA)) {
//			return false;
//		}
//		if(lowerMaterial.equals(Material.AIR) | lowerMaterial.equals(Material.FIRE) | lowerMaterial.equals(Material.WATER) 
//				| lowerMaterial.equals(Material.LAVA) | lowerMaterial.equals(Material.CACTUS)) {
//			return false;
//		}
		return true;
	}
	
	/**
	 * 获取指定位置的立足点模型
	 * topView:     ■    +1     sideView:     ■    +1 
	 *            ■ □ ■   0                 ■ □ ■   0 
	 *              ■    -1                 ■ ■ ■  -1
	 *                                        ■    -2
	 *                                        ■    -3
	 *                                        ■    -4
	 *                                        ■    -5
	 *                                        ■    -6
	 * @param pos
	 * @return
	 */
	public static List<V3D> getStandModel(V3D pos) {
		List<V3D> list = new ArrayList<V3D>(16);
		list.add(pos);
		list.add(pos.addY(1));
		list.add(pos.add(1, 0, 0));
		list.add(pos.add(-1, 0, 0));
		list.add(pos.add(0, 0, 1));
		list.add(pos.add(0, 0, -1));
		list.add(pos.addY(-1));
		list.add(pos.add(1, -1, 0));
		list.add(pos.add(-1, -1, 0));
		list.add(pos.add(0, -1, 1));
		list.add(pos.add(0, -1, -1));
		list.add(pos.addY(-2));
		list.add(pos.addY(-3));
		list.add(pos.addY(-4));
		list.add(pos.addY(-5));
		list.add(pos.addY(-6));
//		V3D[] b = new V3D[list.size()];
//		list.toArray(b);
		return list;
	}
	
	public static V3D[] getPosByDistance(int dis) {
		List<V3D> list = new ArrayList<V3D>(dis * dis * dis);
		V3D[] b = new V3D[list.size()];
		list.toArray(b);
		return b;
	}
	
	public static Material getMaterial(V3D pos) {
		return getBlockState(pos).getMaterial();
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
