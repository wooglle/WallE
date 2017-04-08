package com.woog.walle.additional;

import com.woog.walle.V3D;

/**
 * 自定义方向枚举类
 * @author WooG
 *
 */
public enum IDirection {
	south(new V3D(0, 0, 0), 0),
	north(new V3D(0, 0, 0), 1);
	private V3D diff;
	private int index;
	private IDirection(V3D diff, int index) {
		this.diff = diff;
		this.index = index;
	}
}
