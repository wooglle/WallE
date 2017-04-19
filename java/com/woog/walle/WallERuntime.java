package com.woog.walle;

import java.util.List;

import com.woog.walle.additional.RebuildTree;

public class WallERuntime {
	//行走的路径
	public static List<V3D> way = null;
	
	//当前正在砍的树
	public static RebuildTree currentTree = null;
	//砍树前的起始坐标
	public static V3D originPos;
	//当前正在砍的树已经砍掉木块的数量
	public static int hasCutted;
	//是否正在砍树
	public static boolean isCuttingTree;
}
