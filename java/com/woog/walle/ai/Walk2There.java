package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.RayTraceTarget;
import com.woog.walle.V3D;
import com.woog.walle.WallE;
import com.woog.walle.additional.IChunk;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public class Walk2There extends ActionBase{
	@Override
	public String getActName() {
		return "Walk to There";
	}
	
	@Override
	public boolean showMsg() {
		return false;
	}
	
	@Override
	public boolean condition() {
		return (mc.world != null) && doing && (!pause);
	}
	
	private void forward() {
		util.setMovement(1, false, false);
	}
	
	private void forwardstop() {
		util.setMovement(0, false, false);
	}
	
	@Override
	public void action() {
		int n = WallE.way.size();
		if(n == 0) {
			return;
		}
		V3D current = null;
		
		KeepOnWatch watch = new KeepOnWatch(WallE.way.get(0), 0);
		while(this.condition() && !WallE.way.isEmpty()) {
			watch.SetPos(WallE.way.get(0));
			forward();
			while(this.condition()) {
				if(mc.player.getEntityBoundingBox().isVecInside(WallE.way.get(0).toVec3d())) {
					System.out.println("bounding====================");
				}
				delay(5);
				if(WallE.way.get(0).equals(new V3D(APIPlayer.getFoot()))) {
					forwardstop();
					WallE.way.remove(0);
					break;
				}
			}
		}
//		for(int i = 0; i < n; i++) {
//			watch.SetPos(WallE.way.get(0));
//			forward();
//			while(this.condition()) {
//				if(mc.player.getEntityBoundingBox().isVecInside(WallE.way.get(0).toVec3())) {
//					System.out.println("bounding====================");
//				}
//				if(WallE.way.get(0).isEqual(new V3D(APIPlayer.getFoot()))) {
//					forwardstop();
//					WallE.way.remove(0);
//					break;
//				}
//			}
//			if(!this.condition()) {
//				return;
//			}
//		}
		watch.isKeep = false;
//		util.allDefault();
	}
}
