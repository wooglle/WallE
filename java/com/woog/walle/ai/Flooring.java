package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.RayTraceTarget;
import com.woog.walle.V3D;
import com.woog.walle.additional.IChunkFlooring;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class Flooring extends ActionBase{
	private V3D[] lastTen = new V3D[10];
	private EnumFacing strtFace;
	private IChunkFlooring ichunk;
	private V3D posBackOne;
	
	@Override
	public String getActName() {
		return "Flooring";
	}
	
	@Override
	public String getToolsKeyword() {
		
		return "hatchet";
	}
	
	private V3D RayTrace(V3D pos) {
		for(int i = 0; i < 48; i++) {
			
		}
		return null;
	}
	
	/**
	 * 在起点位置做好姿势
	 */
	private void getReady() {
		delay(100);
		V3D posInt = APIPlayer.getFootWithOffset();
		System.out.println("[Flooring]   " + this.ichunk.facing.getHorizontalAngle());
		new FaceTo(this.ichunk.facing, 80);
		this.util.setMovement(-0.1F, false, true);
		for(int i = 0; i < 100; i++) {
			if(this.isGetReady(this.ichunk.firstEmpty)) {
				break;
			}
			delay(10);
		}
		
//		while(this.isGetReady(this.ichunk.firstStandPos.addY(-1))) {
//			this.util.setMovement(-0.1F, false, true);
//			delay(10);
//		}
		this.util.setMovement(0, false, false);
	}
	
	private void go() {
		
	}
	
	private boolean isGetReady(V3D pos) {
		EnumFacing face = this.ichunk.facing.getOpposite();
		Vec3d foot = APIPlayer.getFoot();
		if(face.getFrontOffsetX() == 0) {
			System.out.println("[Flooring x]   " + foot.zCoord + "   " + (double)(pos.z  + face.getFrontOffsetZ() * 0.2D));
			return foot.zCoord > (double)pos.z  + face.getFrontOffsetZ() * 0.2 ? true : false; 
		}else if(face.getFrontOffsetZ() == 0) {
			System.out.println("[Flooring z]   " + foot.xCoord + "   " + (pos.x - face.getFrontOffsetX() * 0.2));
			return foot.xCoord > (double)pos.x  + (double)face.getFrontOffsetX() * 0.2 ? true : false; 
		}
		return false;
	}
	
	@Override
	public void action() {
		this.ichunk = new IChunkFlooring();
		if(!APIPlayer.getFootWithOffset().equals(this.ichunk.firstStandPos)) {
			new RayTraceTarget(this.ichunk.firstStandPos, false);
			new Walk2There();
			return;
		}else{
			this.getReady();
			this.go();
		}
	}
}
