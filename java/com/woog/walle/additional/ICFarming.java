package com.woog.walle.additional;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.V3DHelper;

import net.minecraft.util.EnumFacing;

public class ICFarming {
	/**主要运动方向，单行的方向*/
	public EnumFacing facing;
	/**次要运动方向， 换行方向*/
	public EnumFacing facingShift;
	/**第一块土地的位置*/
	public V3D firstPlantPos;
	/**操作第一块土地时站立的位置*/
	public V3D firstStandPos;
	/**是否与脚步高度相等, 即比头部低一格*/
	public boolean isAccordant;
	public ICFarming() {
		for(V3D pos : V3DHelper.getUDLRFB(APIPlayer.getFootWithOffset())) {
			if(this.isPlantBlock(pos)) {
				this.firstPlantPos = pos;
				break;
			}
		}
		if(this.firstPlantPos != null) {
			this.isAccordant = this.firstPlantPos.y - APIPlayer.getFootWithOffset().y == 0;
//			System.out.println("ICF 1  " + this.startPos + "   " + this.isAccordant);
		}else{
			System.out.println("ICFarming   Errro: firstPlantPos is null!!!");
		}
		this.set();
//		System.out.printf("\t\tICF  2:  %s, %s, %s, %s, %s\n" , this.facing , this.facingShift, this.startPos, this.firstStandPos, this.isAccordant);
	}
	
	private void set() {
		V3D tem;
		boolean[][] boo = new boolean[4][10];
		for(int i = 0; i < 4; i++) {
			EnumFacing face  = EnumFacing.HORIZONTALS[i];
			for(int j = 0; j < 10; j++) {
				tem = this.firstPlantPos.add(face.getFrontOffsetX() * (j + 1), 0, face.getFrontOffsetZ() * (j + 1));
				if(APIChunk.isEmpty(tem)) {
					break;
				}else if(this.isPlantBlock(tem) ) {
					boo[i][j] = true;
				}
			}
		}
		int[][] iLenght = new int[4][2];
		for(int i = 0; i < 4; i++) {
			iLenght[i][0] = i;
			int j = 0;
			while(j < 10 && boo[i][j]) {
				iLenght[i][1]++;
				j++;
			}
		}
		int t = 0;
		for(int i = 0; i < 4; i++) {
			for(int j = i + 1; j < 4; j++) {
				if(iLenght[i][1] < iLenght[j][1]) {
					t = iLenght[i][0];
					iLenght[i][0] = iLenght[j][0];
					iLenght[j][0] = t;
					t = iLenght[i][1];
					iLenght[i][1] = iLenght[j][1];
					iLenght[j][1] = t;
				}
			}
		}
		this.facing = EnumFacing.HORIZONTALS[iLenght[0][0]];
		this.facingShift = EnumFacing.HORIZONTALS[iLenght[1][0]];
		if(iLenght[1][1] > 2) {
			this.isAccordant = false;
			this.firstStandPos = this.firstPlantPos.addY(1);
		}else{
			this.isAccordant = true;
			this.firstStandPos = this.firstPlantPos.add(EnumFacing.HORIZONTALS[iLenght[1][0]].getOpposite());
		}
	}
	
	public boolean isPlantBlock(V3D pos) {
		String name = APIChunk.getBlock(pos).getRegistryName().toString();
		return (name.equals("minecraft:farmland") | name.equals("minecraft:sand") | name.equals("minecraft:soul_sand"));
	}
}
