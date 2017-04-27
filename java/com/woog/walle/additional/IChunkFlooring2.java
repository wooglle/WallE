package com.woog.walle.additional;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.V3DHelper;

import net.minecraft.util.EnumFacing;

public class IChunkFlooring2 {
	//第一块空坐标
	public V3D firstEmpty;
	//第一块站立的坐标
	public V3D firstStandPos;
	//铺地时视线水平朝向
	public EnumFacing facing;
	//开始铺地时的横向移动的方向
	public EnumFacing facingTransverse;
	
	public IChunkFlooring2() {
		set();
		setTransverse();
	}
	
	private void set() {
		this.firstStandPos = APIPlayer.getFootWithOffset();
		this.facing = APIPlayer.getFacing().getOpposite();
		this.firstEmpty = this.firstStandPos.add(this.facing.getOpposite().getFrontOffsetX(), -1, this.facing.getOpposite().getFrontOffsetZ());
		
		
	}
	
	private void setTransverse() {
		List<V3D> list = V3DHelper.getCrossPlane(firstEmpty, 1);
		for(V3D temp : list) {
			if(APIChunk.isEmpty(temp)) {
				V3D direct = V3DHelper.getDirection(this.firstEmpty, temp);
				if(!direct.equals(new V3D(this.facing.getOpposite()))) {
					this.facingTransverse = V3DHelper.toEnumFacing(direct);
				}
			}
		}
	}
}
