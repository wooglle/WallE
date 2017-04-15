package com.woog.walle.ai;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.EventTickClass;
import com.woog.walle.RayTraceTarget;
import com.woog.walle.V3D;
import com.woog.walle.WallE;
import com.woog.walle.additional.RebuildTree;

public class CutTrees extends ActionBase {
	private static RebuildTree currentTree = null;
	private static V3D originPos = null;
	private static int hasCutted = 0;
	
	@Override
	public String getToolsKeyword() {
		return "hatchet";
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
		System.out.println("  tree.isStraight    " + tree.isStraight);
		if(tree.isStraight) {
			for(V3D pos : tree.getLogs()) {
				this.cutOneLog(pos);
				V3D foothold = getBetterFoothold(tree.getRoot(), pos);
				if(foothold != null) {
					new RayTraceTarget(foothold, false);
					new Walk2There();
					return;
				}
			}
		}
	}
	
	private V3D getBetterFoothold(V3D treeRoot, V3D pos) {
		V3D foot = APIPlayer.getFootWithOffset();
		V3D better = null;
		if(pos.y > foot.y ) {
			if(pos.y < foot.y + 7) {
				if(!foot.isEqual(treeRoot)) {
					better = treeRoot;
				}
			}
		}
		return better;
	}
	
	private void cutOneLog(V3D pos) {
		this.holdStuff();
		this.util.leftDown();
		while(!APIChunk.isEmpty(pos)) {
			new FaceTo(pos, 1);
			delay(20);
		}
		this.util.leftUp();
		hasCutted++;
	}
	
	@Override
	public void action() {
		WallE.isCuttingTrees = true;
		if(this.condition() && !WallE.TreePos.isEmpty()) {
			V3D startPos = null;
			V3D tpos = WallE.TreePos.get(0);
			V3D t = new V3D(tpos.x, APIPlayer.getHeadPos().y, tpos.z);
			if(APIChunk.isLog(t)) {
				startPos = t;
			}else{
				startPos = tpos;
			}
			if(APIPlayer.getHeadPos().distance(startPos) > 1.1D) {
				new RayTraceTarget(startPos, false);
				new Walk2There();
//				delay(100);
				return;
			}else{
				if(currentTree == null) {
					currentTree = new RebuildTree(WallE.TreePos.get(0));
					this.originPos = APIPlayer.getFootWithOffset();
					hasCutted = 0;
				}
				this.cutATree(currentTree);
				if(currentTree.getLogs().size() <= hasCutted) {
					new FaceTo(currentTree.getRoot(), 1);
					new Stuffing("sapling");
					this.util.leftDown();
					delay(50);
					this.util.leftUp();
					new RayTraceTarget(this.originPos, false);
					new Walk2There();
					WallE.TreePos.remove(0);
					currentTree = null;
					originPos = null;
					WallE.isCuttingTrees = false;
				}
			}
		}
	}
}
