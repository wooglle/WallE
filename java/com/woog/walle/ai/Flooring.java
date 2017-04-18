package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;

import net.minecraft.util.math.Vec3d;

public class Flooring extends ActionBase{
	private V3D[] lastTen = new V3D[10];
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
	public void getReady() {
		Vec3d pos = APIPlayer.getFoot();
		Vec3d deriction = APIPlayer.getDirectionDiff();
		V3D posInt = APIPlayer.getFootWithOffset();
		
		Vec3d posBack = new Vec3d(posInt.x + 0.5 + deriction.xCoord * 0.7, posInt.y, posInt.z + 0.5 + deriction.zCoord * 0.7);
	}
	
	@Override
	public void action() {
		
	}
}
