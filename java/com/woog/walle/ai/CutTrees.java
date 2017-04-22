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
//	public static RebuildTree currentTree;
//	public static V3D originPos;
//	public static int hasCutted;
	
	@Override
	public boolean showMsg() {
		return false;
	}
	
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
			for(int i = WallE.runtime.hasCutted; i < tree.getLogs().size(); i++) {
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
		}else{
			this.running = false;
			EventTickClass.doCheckTrees = false;
			WallE.runtime.isCuttingTree = false;
			System.out.println(this.getActName() + "错误！！！！！！！！！！！！！！");
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
		while(this.condition() && !APIChunk.isEmpty(pos)) {
			new FaceTo(pos, 1);
			delay(20);
		}
		this.util.leftUp();
		delay(50);
		WallE.runtime.hasCutted++;
	}
	
	private void plantingTree() {
		while(this.condition() && APIChunk.isEmpty(WallE.runtime.currentTree.getRoot())) {
			new FaceTo(WallE.runtime.currentTree.getRoot().addY(-1), 1);
			new Stuffing("sapling");
			delay(100);
			this.util.rightDown();
			delay(50);
			this.util.rightUp();
			delay(100);
		}
	}
	
	private void breakLeaves() {
		
	}
	
	@Override
	public void action() {
		WallE.runtime.isCuttingTree = true;
		if(this.condition() && !WallE.TreePos.isEmpty()) {
			V3D startPos = null;
			V3D tpos = WallE.TreePos.get(0);
			V3D t = new V3D(tpos.x, APIPlayer.getHeadPos().y, tpos.z);
			if(APIChunk.isLog(t)) {
				startPos = t;
			}else{
				startPos = tpos;
			}
			if(WallE.runtime.hasCutted == 0) {
				WallE.runtime.currentTree = new RebuildTree(WallE.TreePos.get(0));
			}
			while(this.condition() && WallE.runtime.currentTree.getLeafLowerEye() != null) {
				WallE.runtime.originPos = APIPlayer.getFootWithOffset();
				V3D leaf = WallE.runtime.currentTree.getLeafLowerEye();
				RayTraceTarget rayTrace = new RayTraceTarget(leaf, false);
				delay(50);
				if(!APIPlayer.getFootWithOffset().isEqual(rayTrace.foothold)) {
					new Walk2There();
					return;
				}else{
					new FaceTo(leaf, 1);
					this.util.leftDown();
					while(!APIChunk.isEmpty(leaf)) {
						delay(40);
					}
					this.util.leftUp();
					WallE.runtime.currentTree.removeFirstLeaf();
				}
			}
			
			if(WallE.runtime.hasCutted == 0 & APIPlayer.getFootWithOffset().distance(startPos) > 2.9D) {
				if(WallE.runtime.originPos == null) {
					WallE.runtime.originPos = APIPlayer.getFootWithOffset();
				}
				new RayTraceTarget(startPos, false);
				new Walk2There();
				return;
			}
			this.cutATree(WallE.runtime.currentTree);
			if(WallE.runtime.currentTree.getLogs().size() <= WallE.runtime.hasCutted) {
				if(APIChunk.isEmpty(WallE.runtime.currentTree.getRoot())) {
					this.plantingTree();
				}
				while(this.condition() && WallE.runtime.currentTree.getLeafCanBreak() != null) {
					V3D leaf = WallE.runtime.currentTree.getLeafCanBreak();
					RayTraceTarget rayTrace = new RayTraceTarget(leaf, false);
					if(!APIPlayer.getFootWithOffset().isEqual(rayTrace.foothold)) {
						new Walk2There();
						return;
					}else{
						new FaceTo(leaf, 1);
						this.util.leftDown();
						while(!APIChunk.isEmpty(leaf)) {
							delay(40);
						}
						this.util.leftUp();
						WallE.runtime.currentTree.removeFirstLeaf();
					}
				}
				if(!APIPlayer.getFootWithOffset().isEqual(WallE.runtime.originPos)) {
					new RayTraceTarget(WallE.runtime.originPos, false);
					new Walk2There();
					return;
				}
				new FaceTo(WallE.runtime.currentTree.getRoot(), 1);
				WallE.TreePos.remove(0);
				WallE.runtime.currentTree = null;
				WallE.runtime.originPos = null;
				WallE.runtime.hasCutted = 0;
				WallE.runtime.isCuttingTree = false;
				System.out.println("CUTTING   结束了~~~~~~~~~~~~~~~~~~~~~~");
			}
			
		}
	}
}
