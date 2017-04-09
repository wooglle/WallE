package com.woog.walle.additional;

import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class IDirection {
	public final V3D front = getFront();
	public final V3D back = getBack();
	public final V3D left = getLeft();
	public final V3D right = getRight();
	public IDirection() {}
	
	/**
	 * 获取顺时针方向排列
	 * @return 前、左、后、右
	 */
	public V3D[] getClockwise() {
		V3D[] b = new V3D[4];
		b[0] = front;
		b[1] = right;
		b[2] = back;
		b[3] = left;
		return b;
	}
	/**
	 * 获取逆时针方向排列
	 * @return 前、右、后、左
	 */
	public V3D[] getAntiClockwise() {
		V3D[] b = new V3D[4];
		b[0] = front;
		b[1] = left;
		b[2] = back;
		b[3] = right;
		return b;
	}
	/**
	 * 获取方向排列
	 * @return 前、左、右、后
	 */
	public V3D[] getWADS() {
		V3D[] b = new V3D[4];
		b[0] = front;
		b[1] = left;
		b[2] = right;
		b[3] = back;
		return b;
	}
	/**
	 * 获取斜方向的数组
	 * @return 左前、右前、左后、右后
	 */
	public V3D[] getIncline() {
		V3D[] b = new V3D[4];
		b[0] = front.add(left);
		b[1] = front.add(right);
		b[2] = back.add(left);
		b[3] = back.add(right);
		return b;
	}
	
	private static V3D getFront() {
		return new V3D(APIPlayer.getFacing().getDirectionVec());
	}
	private static V3D getBack() {
		return new V3D(APIPlayer.getFacing().getDirectionVec()).getOpposite();
	}
	private static V3D getLeft() {
		Vec3i t = APIPlayer.getFacing().getDirectionVec();
		if(t.equals(EnumFacing.EAST.getDirectionVec())) {
			return new V3D(EnumFacing.NORTH.getDirectionVec());
		}else if(t.equals(EnumFacing.WEST.getDirectionVec())) {
			return new V3D(EnumFacing.SOUTH.getDirectionVec());
		}else if(t.equals(EnumFacing.SOUTH.getDirectionVec())) {
			return new V3D(EnumFacing.EAST.getDirectionVec());
		}else{
			return new V3D(EnumFacing.WEST.getDirectionVec());
		}
	}
	private static V3D getRight() {
		Vec3i t = APIPlayer.getFacing().getDirectionVec();
		if(t.equals(EnumFacing.EAST.getDirectionVec())) {
			return new V3D(EnumFacing.SOUTH.getDirectionVec());
		}else if(t.equals(EnumFacing.WEST.getDirectionVec())) {
			return new V3D(EnumFacing.NORTH.getDirectionVec());
		}else if(t.equals(EnumFacing.SOUTH.getDirectionVec())) {
			return new V3D(EnumFacing.WEST.getDirectionVec());
		}else{
			return new V3D(EnumFacing.EAST.getDirectionVec());
		}
	}
}
