package com.woog.walle.ai;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.EventTickClass;
import com.woog.walle.RayTraceTarget;
import com.woog.walle.V3D;
import com.woog.walle.WallE;
import com.woog.walle.additional.RebuildTree;

public class CutTrees extends ActionBase {
	/*【记者】叁只逗比比比比比比比比比比比比比比比比比 2017/4/15 23:37:12
	       总之在必须初始化地方前面初始化不用初始化就别初始化
	 * 
	 */
	public static RebuildTree currentTree;
	public static V3D originPos;
	public static int hasCutted;
	
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
		if(tree.isStraight) {
			for(int i = hasCutted; i < tree.getLogs().size(); i++) {
				this.cutOneLog(tree.getLogs().get(i));
				delay(100);
				if(!APIChunk.isEmpty(tree.getLogs().get(i))) {
					this.cutOneLog(tree.getLogs().get(i));
				}
				V3D foothold = getBetterFoothold(tree.getRoot(), tree.getLogs().get(i));
				if(foothold != null && !foothold.isEqual(APIPlayer.getFootWithOffset())) {
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
		delay(50);
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
			if(CutTrees.hasCutted == 0 & APIPlayer.getFootWithOffset().distance(startPos) > 2.9D) {
				CutTrees.originPos = APIPlayer.getFootWithOffset();
				new RayTraceTarget(startPos, false);
				new Walk2There();
				return;
			}
			if(CutTrees.hasCutted == 0) {
				CutTrees.currentTree = new RebuildTree(WallE.TreePos.get(0));
			}
			this.cutATree(CutTrees.currentTree);
			if(CutTrees.currentTree.getLogs().size() <= CutTrees.hasCutted) {
				if(APIChunk.isEmpty(CutTrees.currentTree.getRoot())) {
					new FaceTo(CutTrees.currentTree.getRoot().addY(-1), 1);
					new Stuffing("sapling");
					this.util.rightDown();
					delay(50);
					this.util.rightUp();
					new RayTraceTarget(CutTrees.originPos, false);
					new Walk2There();
				}else{
					new FaceTo(CutTrees.currentTree.getRoot(), 1);
					WallE.TreePos.remove(0);
					CutTrees.currentTree = null;
					CutTrees.originPos = null;
					CutTrees.hasCutted = 0;
					WallE.isCuttingTrees = false;
				}
			}
		}
	}
}
