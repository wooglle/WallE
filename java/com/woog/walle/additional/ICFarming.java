package com.woog.walle.additional;

import com.woog.walle.APIChunk;
import com.woog.walle.V3D;

import net.minecraft.util.EnumFacing;

public class ICFarming {
	/*主要运动方向，单行的方向*/
	public EnumFacing facing;
	/*次要运动方向， 换行方向*/
	public EnumFacing facingShift;
	/*初始位置*/
	private V3D startPos;
	public ICFarming() {
		
	}
	
	private void set() {
		int[][] list = new int[4][10];
		int n = 0;
		for(EnumFacing face: EnumFacing.HORIZONTALS) {
			for(int i = 1; i < 11; i++) {
//				APIChunk.getBlockState(startPos.add(face))
			}
			n++;
		}
	}
	
	
}
