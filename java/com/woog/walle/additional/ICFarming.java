package com.woog.walle.additional;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.V3DHelper;

import net.minecraft.util.EnumFacing;

public class ICFarming {
	/*主要运动方向，单行的方向*/
	public EnumFacing facing;
	/*次要运动方向， 换行方向*/
	public EnumFacing facingShift;
	/*初始位置*/
	private V3D startPos;
	/*是否与脚步高度相等*/
	public boolean isAccordant;
	public ICFarming() {
		for(V3D pos : V3DHelper.getUDLRFB(APIPlayer.getFootWithOffset())) {
			if(this.isPlantBlock(pos)) {
				this.startPos = pos;
				break;
			}
		}
		if(this.startPos != null) {
			this.isAccordant = this.startPos.y - APIPlayer.getFootWithOffset().y == 0;
		}
	}
	
	private void set() {
		int[][] list = new int[4][10];
		
		Boolean[][] boo = new Boolean[4][10];
		for(int i = 0; i < 4; i++) {
			EnumFacing face = EnumFacing.HORIZONTALS[i];
			for(int j = 1; j < 11; j++) {
				if(this.isPlantBlock(startPos.add(face))) {
					boo[i][j] = true;
				}
			}
		}
		int[] temp = new int[4];
		int maxLenght, max = 0, maxIndex;
		for(int i = 0; i < 4; i++) {
			maxLenght = 0;
			for(int j = 0; j < 11; j++) {
				if(boo[i][j] != null && boo[i][j]) {
					maxLenght = j;
				}
			}
			if(max < maxLenght) {
				max = maxLenght;
				maxIndex = i;
			}
			temp[i] = maxLenght;
		}
		
	}
	
	private boolean isPlantBlock(V3D pos) {
		String name = APIChunk.getBlock(pos).getRegistryName().toString();
		return (name.equals("minecraft:dirt") | name.equals("minecraft:sand") | name.equals("minecraft:soul_sand"));
	}
}
