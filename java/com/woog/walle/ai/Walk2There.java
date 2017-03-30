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
		return true;
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
		V3D current = null;
		
		for(int i = 0; i < n; i++) {
//			System.out.println("   " + i + "           " + WallE.way.get(0));
			KeepOnWatch watch = new KeepOnWatch(WallE.way.get(0), 0);
//			watch.pos = WallE.way.get(0);
			forward();
			
			while(true) {
				if(mc.player.getEntityBoundingBox().isVecInside(WallE.way.get(0).toVec3())) {
					System.out.println("bounding====================");
				}
				if(WallE.way.get(0).isEqual(new V3D(APIPlayer.getFoot()))) {
					forwardstop();
					WallE.way.remove(0);
					break;
				}
			}
			watch.isKeep = false;
		}
		util.allDefault();
	}
}
