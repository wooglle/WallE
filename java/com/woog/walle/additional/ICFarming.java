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
			System.out.println("ICF 1  " + this.startPos + "   " + this.isAccordant);
		}
		this.set();
	}
	
	private void set() {
		int[] list = new int[4];
		V3D tem;
		boolean[][] boo = new boolean[4][10];
		for(int i = 0; i < 4; i++) {
			EnumFacing face  = EnumFacing.HORIZONTALS[i];
			for(int j = 1; j < 10; j++) {
				tem = this.startPos.add(face);
				if(APIChunk.isEmpty(tem)) {
					break;
				}else if(this.isPlantBlock(tem) ) {
					boo[i][j] = true;
				}
			}
		}
		boolean[] tempArray = new boolean[10];
		int temp = 0;
		for(int i = 0; i < 4; i++) {
			for(int j = i + 1; j < 3; j++) {
				int l1 = 0, l2 = 0;
				for(int b1 = 0; b1 < boo[i].length; b1++) {
					if(boo[i][b1]) {
						l1 = b1;
					}
				}
				for(int b2 = 0; b2 < boo[j].length; b2++) {
					if(boo[j][b2]) {
						l1 = b2;
					}
				}
				if(l1 < l2) {
					tempArray = boo[i];
					boo[i] = boo[j];
					boo[j] = tempArray;
					list[i] = j;
					list[j] = i;
				}
			}
		}
		this.facing = EnumFacing.HORIZONTALS[list[0]];
		this.facingShift = EnumFacing.HORIZONTALS[list[1]];
		
	}
	
	private boolean isPlantBlock(V3D pos) {
		String name = APIChunk.getBlock(pos).getRegistryName().toString();
		return (name.equals("minecraft:farmland") | name.equals("minecraft:sand") | name.equals("minecraft:soul_sand"));
	}
}
