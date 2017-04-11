package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.EventTickClass;
import com.woog.walle.RayTraceTarget;
import com.woog.walle.WallE;
import com.woog.walle.additional.RebuildTree;

public class CutTrees extends ActionBase {
	@Override
	public String getToolsKeyword() {
		return "";
	}
	
	@Override
	public String getActName() {
		return "Cut Trees";
	}
	
	@Override
	public boolean condition() {
		return (mc.world != null) && doing && (!pause);
	}
	
	@Override
	public void action() {
		WallE.isCuttingTrees = true;
		while(!WallE.TreePos.isEmpty()) {
			RebuildTree tree = new RebuildTree(WallE.TreePos.get(0));
			if(APIPlayer.getFootWithOffset().distance(tree.getRoot()) < 2.1D) {
//				this.pause = true;
				new RayTraceTarget(tree.getRoot(), false);
//				new Walk2There();
//				delay(100);
				return;
			}else{
				WallE.TreePos.remove(0);
//				WallE.isCuttingTrees = false;
			}
		}
	}
}
