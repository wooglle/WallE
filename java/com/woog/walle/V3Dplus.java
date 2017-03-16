package com.woog.walle;

public class V3Dplus {
	V3D position;
	int count;
	boolean isTree;
	
	/**
	 * 
	 * @param position 路点坐标
	 * @param count 总计数
	 * @param isTree 是否分叉
	 */
	public V3Dplus(V3D position, int step, boolean isTree) {
		this.position = position;
		this.count = step;
		this.isTree = isTree;
	}
}
