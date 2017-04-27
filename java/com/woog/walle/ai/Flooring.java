package com.woog.walle.ai;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.RayTraceTarget;
import com.woog.walle.V3D;
import com.woog.walle.WallE;
import com.woog.walle.additional.IChunkFlooring;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class Flooring extends ActionBase{
	private V3D[] lastTen = new V3D[10];
	private EnumFacing transverseFace;
	private boolean pressRight;
	private IChunkFlooring ichunk;
	private V3D posBackOne;
	private V3D nextRenference;
	private V3D footNextDiff;
	
	@Override
	public String getActName() {
		return "Flooring";
	}
	
	@Override
	public String getToolsKeyword() {
		
		return "block";
	}
	
	/**
	 * 在起点位置做好姿势
	 */
	private void stepBackward() {
		delay(200);
		this.util.setMovement(-0.9F, 0.0F, false, true);
		for(int i = 0; i < 500; i++) {
			if(this.isGetReady(this.nextRenference)) {
				break;
			}
			delay(5);
		}
		this.util.setMovement(0.0F, 0.0F, false, true);
		delay(60);
		this.util.setMovement(0.0F, 0.0F, false, false);
		delay(200);
		new FaceTo(this.nextRenference.addY(-1), this.ichunk.facing.getOpposite(), 2);
		delay(100);
	}
	
	private void go() {
//		while(this.condition() && this.isLongitudinal()) {
			this.stepBackward();
			this.util.rightDown();
			delay(200);
			if(this.pressRight) {
				this.util.setMovement(0.0F, -0.8F, false, false);
			}else{
				this.util.setMovement(0.0F, 0.8F, false, false);
			}
			while(this.condition() && this.isTransverseInside()) {
				this.holdStuff();
				delay(20);
			}
			this.util.setMovement(0.0F, 0.0F, false, false);
			delay(60);
			this.nextRenference = APIPlayer.getFootWithOffset();
			this.leftRightSwitch();
			this.isLongitudinal();
//		}
//		delay(500);
		this.util.rightUp();
	}
	
	private boolean isTransverseInside() {
//		this.ichunk.isPosInside(APIPlayer.getFootWithOffset().addY(-1).add(new V3D(this.ichunk.facing).add(new V3D(this.transverseFace))));
		V3D temp = APIPlayer.getFootWithOffset().add(new V3D(this.transverseFace).addY(-1));
		return APIChunk.isEmpty(temp);
	}
	
	private boolean isLongitudinal() {
		int difX = this.ichunk.facing.getOpposite().getFrontOffsetX();
		int difZ = this.ichunk.facing.getOpposite().getFrontOffsetZ();
		V3D temp  = APIPlayer.getFootWithOffset().add(difX, -1, difZ);
//		System.out.println("FLOOR   " + APIPlayer.getFootWithOffset() + temp);
		return APIChunk.isEmpty(temp);
	}
	
	/**
	 * 坐标越过边界0.12（最大可达0.3）
	 * @param pos
	 * @return
	 */
	private boolean isGetReady(V3D pos) {
		Vec3d foot = APIPlayer.getFoot();
		double next = 0;
		if(this.ichunk.facing.equals(EnumFacing.SOUTH)) {
			next = (double)pos.z - 0.12;
//			System.out.println("FLOOR 1  " + pos + foot + next);
			return foot.zCoord < next;
		}else if(this.ichunk.facing.equals(EnumFacing.NORTH)){
			next = (double)pos.z + 1.12;
//			System.out.println("FLOOR 2  " + pos + foot + next);
			return foot.zCoord > next;
		}else if(this.ichunk.facing.equals(EnumFacing.EAST)) {
			next = (double)pos.x - 0.12;
//			System.out.println("FLOOR 3  " + pos + foot + next);
			return foot.xCoord < next;
		}else{
			next = (double)pos.x + 1.12;
//			System.out.println("FLOOR 4  " + pos + foot + next);
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
		if(WallE.runtime.icFlooring == null) {
			WallE.runtime.icFlooring = new IChunkFlooring();
			this.ichunk = WallE.runtime.icFlooring;
		}
		if(!APIPlayer.getFootWithOffset().equals(this.ichunk.firstStandPos)) {
			new RayTraceTarget(this.ichunk.firstStandPos, false);
			new Walk2There();
			return;
		}else{
			this.footNextDiff = new V3D(this.ichunk.facing.getOpposite().getFrontOffsetX(), -1, this.ichunk.facing.getOpposite().getFrontOffsetZ());
			this.nextRenference = this.ichunk.firstStandPos;
			delay(200);
			new FaceTo(this.ichunk.facing, 80);
			this.transverseFace = this.ichunk.facingTransverse;
			int rightIndex = (this.ichunk.facing.getHorizontalIndex() + 1) % 4;
			if(this.ichunk.facingTransverse.equals(EnumFacing.getHorizontal(rightIndex))) {
				this.pressRight = true;
			}else{
				this.pressRight = false;
			}
			this.go();
			WallE.runtime.icFlooring = null;
		}
	}
}
