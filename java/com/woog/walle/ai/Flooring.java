package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.RayTraceTarget;
import com.woog.walle.V3D;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class Flooring extends ActionBase{
	private V3D[] lastTen = new V3D[10];
	private V3D startPos = getStartPos();
	private V3D fistPos;
	private EnumFacing strtFace;
	@Override
	public String getActName() {
		return "Flooring";
	}
	
	@Override
	public String getToolsKeyword() {
		
		return "hatchet";
	}
	
	/**
	 * 自动获取铺地起始点
	 * @return
	 */
	private V3D getStartPos() {
		
		return null;
	}
	
	/**
	 * 在起点位置做好姿势
	 */
	private void getReady() {
		Vec3d pos = APIPlayer.getFoot();
		Vec3d deriction = APIPlayer.getDirectionDiff();
		V3D posInt = APIPlayer.getFootWithOffset();
		V3D startFirstDiff = startPos.minus(fistPos);
		new FaceTo(startPos.add(startFirstDiff), 1);
		
		Vec3d posBack = new Vec3d(posInt.x + 0.5 + deriction.xCoord * 0.7, posInt.y, posInt.z + 0.5 + deriction.zCoord * 0.7);
		while(this.isGetReady(startPos)) {
			this.util.setMovement(-0.1F, false, true);
			delay(10);
		}
		this.util.setMovement(0, false, false);
	}
	
	private void go() {
		
	}
	
	private boolean isGetReady(V3D pos) {
		EnumFacing face = APIPlayer.getFacing();
		Vec3d foot = APIPlayer.getFoot();
		if(face.getFrontOffsetX() == 0) {
			return foot.zCoord > pos.z  - face.getFrontOffsetZ() * 0.15 ? true : false; 
		}else if(face.getFrontOffsetZ() == 0) {
			return foot.xCoord > pos.x  - face.getFrontOffsetX() * 0.15 ? true : false; 
		}
		return false;
	}
	
	@Override
	public void action() {
		if(!this.startPos.isEqual(getStartPos())) {
			new RayTraceTarget(this.startPos);
			new Walk2There();
		}else{
			this.getReady();
			this.go();
		}
	}
}
