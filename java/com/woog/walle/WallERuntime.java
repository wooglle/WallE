package com.woog.walle;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.additional.ICFarming;
import com.woog.walle.additional.ICLighting;
import com.woog.walle.additional.IChunkFlooring;
import com.woog.walle.additional.IChunkFlooring2;
import com.woog.walle.additional.RebuildTree;

import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

public class WallERuntime {
	//GuiOverlayDebug
	
	/**
	 * 行走的路径
	 */
	public static List<V3D> way;
	
	/**
	 * 当前正在砍的树
	 */
	public static RebuildTree currentTree;
	/**
	 * 砍树前的起始坐标
	 */
	public static V3D originPos;
	/**
	 * 当前正在砍的树已经砍掉木块的数量
	 */
	public static int hasCutted;
	/**
	 * 是否正在砍树
	 */
	public static boolean isCuttingTree;
	
	/**
	 * 铺地时的地形
	 */
	public static IChunkFlooring2 icFlooring;
	/**
	 * 铺地时 玩家的手持物
	 */
	public static String flooringBlock;
	
	/**
	 * 插火把时的地形
	 */
	public static ICLighting icLighting;
	/**
	 * 插火把时的上一个位置
	 */
	public static V3D lightingPrevious;
	/**
	 * 插火把时的下一个位置
	 */
	public static V3D lightingNext;
	/**
	 * /插火把时下一位置周边多余的火把
	public static List<V3D> lightingSuperfluous = new ArrayList<V3D>(20);
	/**
	 * 插火把时的横向方向
	 */
	public static EnumFacing longitude;
	/**
	 * 插火把时的平面高度
	 */
	public static int lightingHeight;
	
	/**
	 * 种植时的地形
	 */
	public static ICFarming icFarming;
	/**
	 * 种植时下一个需要AI行走的点
	 */
	public static V3D farmingNextPos;
	/**
	 * 种植换行时下一个方块坐标
	 */
	public static V3D farmingNextBlock;
	/**
	 * 种植时单行运动方向
	 */
	public static EnumFacing farmingFace;
	/**
	 * 种植时玩家操作目标时的视线方向
	 */
	public static EnumFacing farmingFaceBlock;
}
