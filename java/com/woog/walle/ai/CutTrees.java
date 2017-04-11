package com.woog.walle.ai;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.EventTickClass;
import com.woog.walle.RayTraceTarget;
import com.woog.walle.V3D;
import com.woog.walle.WallE;
import com.woog.walle.additional.RebuildTree;

public class CutTrees extends ActionBase {
	@Override
	public String getToolsKeyword() {
		return "_axe";
	}
	
	@Override
	public String getActName() {
		return "Cut Trees";
	}
	
	@Override
	public boolean condition() {
		return (mc.world != null) && doing && (!pause);
	}
	
	private void cutATree(RebuildTree tree) {
		if(tree.isStraight) {
			for(V3D pos : tree.getLogs()) {
				this.cutOneLog(pos);
				
			}
		}
	}
	
	private void cutOneLog(V3D pos) {
		this.holdStuff();
		mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
		while(!APIChunk.isEmpty(pos)) {
			new FaceTo(pos, 1);
			delay(20);
		}
		mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
	}
	
	@Override
	public void action() {
		WallE.isCuttingTrees = true;
		while(this.condition() && !WallE.TreePos.isEmpty()) {
			RebuildTree tree = new RebuildTree(WallE.TreePos.get(0));
			System.out.println("      " + APIPlayer.getHeadPos().distance(tree.getRoot()));
			if(APIPlayer.getHeadPos().distance(tree.getRoot()) > 2.1D) {
//				this.pause = true;
				new RayTraceTarget(tree.getRoot(), false);
				new Walk2There();
				this.cutATree(tree);
//				delay(100);
				return;
			}else{
				if(!APIChunk.isLog(WallE.TreePos.get(0))) {
					WallE.TreePos.remove(0);
					WallE.isCuttingTrees = false;
				}
			}
		}
	}
}
