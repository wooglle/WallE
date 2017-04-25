package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.RayTraceTarget;
import com.woog.walle.V3D;
import com.woog.walle.additional.IChunkFlooring;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class Flooring extends ActionBase{
	private V3D[] lastTen = new V3D[10];
	private EnumFacing startFace;
	private EnumFacing transverseFace;
	private boolean pressRight;
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
	
	/**
	 * 在起点位置做好姿势
	 */
	private void stepBackward() {
		delay(200);
		V3D posInt = APIPlayer.getFootWithOffset();
		new FaceTo(this.ichunk.facing, 80);
		delay(200);
		this.util.setMovement(-0.1F, false, true);
		for(int i = 0; i < 500; i++) {
			if(this.isGetReady(this.ichunk.firstEmpty)) {
				break;
			}
			delay(5);
		}
		
		this.util.setMovement(0, false, true);
		delay(200);
		this.util.setMovement(0, false, false);
		delay(200);
		new FaceTo(this.ichunk.firstEmpty, this.ichunk.facing, 2);
		
	}
	
	private void go() {
		this.util.rightDown();
		delay(500);
		if(this.pressRight) {
			this.util.moveRight();
		}else{
			this.util.moveLeft();
		}
		while(this.condition() && this.ichunk.isPosInside(APIPlayer.getFootWithOffset().addY(-1).add(new V3D(this.transverseFace)))) {
			delay(20);
		}
		this.util.rightUp();
		if(this.pressRight) {
			this.util.moveRightStop();
		}else{
			this.util.moveLeftStop();
		}
		this.leftRightSwitch();
//		while(this.ichunk.isPosInside(APIPlayer.getFootWithOffset().addY(-1).add(new V3D(this.ichunk.facing)))) {
//		}
	}
	
	private boolean isGetReady(V3D pos) {
		Vec3d foot = APIPlayer.getFoot();
		double next = 0;
		if(this.ichunk.facing.equals(EnumFacing.SOUTH)) {
			next = (double)pos.z - 0.12;
			return foot.zCoord < next;
		}else if(this.ichunk.facing.equals(EnumFacing.NORTH)){
			next = (double)pos.z + 0.12;
			return foot.zCoord > next;
		}else if(this.ichunk.facing.equals(EnumFacing.EAST)) {
			next = (double)pos.x - 0.12;
			return foot.xCoord < next;
		}else{
			next = (double)pos.x + 0.12;
			return foot.xCoord > next;
		}
	}
	
	private void leftRightSwitch() {
		if(this.pressRight) {
			this.pressRight = false;
		}else{
			this.pressRight = true;
		}
		this.transverseFace = this.transverseFace.getOpposite();
	}
	
	@Override
	public void action() {
		this.util.moveRight();
//		this.ichunk = new IChunkFlooring();
//		if(!APIPlayer.getFootWithOffset().equals(this.ichunk.firstStandPos)) {
//			new RayTraceTarget(this.ichunk.firstStandPos, false);
//			new Walk2There();
//			return;
//		}else{
//			this.stepBackward();
//			this.transverseFace = this.ichunk.facingTransverse;
//			int rightIndex = (this.ichunk.facing.getHorizontalIndex() + 1) % 4;
//			if(this.ichunk.facingTransverse.equals(EnumFacing.getHorizontal(rightIndex))) {
//				this.pressRight = true;
//			}else{
//				this.pressRight = false;
//			}
//			this.go();
//		}
	}
}
