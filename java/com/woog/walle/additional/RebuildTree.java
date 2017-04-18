package com.woog.walle.additional;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;

public class RebuildTree {
	private V3D pos;
	private List<V3D> logs = new ArrayList<V3D>(10);
	private List<V3D> leaves = new ArrayList<V3D>(30);
	List<V3D> tLogs = new ArrayList<V3D>(10);
	List<V3D> tLeaves = new ArrayList<V3D>(30);
	private V3D root;
	private V3D top;
	public boolean isStraight = true;
	public RebuildTree(V3D pos) {
		if(pos == null) {
			return;
		}
		this.pos = pos;
		this.rebuild(pos);
		this.setTree();
	}
	
	private void rebuild(V3D p) {
		for(V3D tem : p.getUDLRFB()) {
			if(!hasRebuild(tem)) {
				String name = APIChunk.getBlock(tem).getRegistryName().toString();
				if(name.equals("minecraft:log")) {
					tLogs.add(tem);
					rebuild(tem);
				}
				if(name.equals("minecraft:leaves")) {
					tLeaves.add(tem);
					rebuild(tem);
				}
			}
		}
	}
	
	private void setTree() {
		V3D min = this.tLogs.get(0);
		V3D max = this.tLogs.get(0);
		for(V3D t : this.tLogs) {
			if(t.y < min.y) {
				min = t;
			}
			if(t.y > max.y) {
				max = t;
			}
			if(t.x != min.x | t.z != min.z) {
				this.isStraight = false;
			}
		}
		this.root = min;
		this.top = max;
		this.logs = this.getFloors(tLogs, max, min);
		
		V3D leavesMin = this.tLeaves.get(0);
		V3D leavesMax = this.tLeaves.get(0);
		for(V3D t : this.tLeaves) {
			if(t.y < leavesMin.y) {
				leavesMin = t;
			}
			if(t.y > leavesMax.y) {
				leavesMax = t;
			}
		}
		this.leaves = this.getFloors(tLeaves, leavesMax, leavesMin);
	}
	
	private List<V3D> getFloors(List<V3D> list, V3D max, V3D min) {
		List<V3D> b = new ArrayList<V3D>(list.size());
		for(int i = 0; i <= max.y - min.y; i++) {
			for(V3D t : list) {
				if(t.y == min.y + i) {
					b.add(t);
				}
			}
		}
		return b;
	}
	
	public V3D getLeafCanBreak() {
		if(this.leaves == null || this.leaves.isEmpty()) {
			return null;
		}
		for(V3D tem : this.leaves) {
			if(tem.y <= APIPlayer.getHeadPos().y + 5) {
				return tem;
			}
		}
		return null;
	}
	
	public void removeFirstLeaf() {
		this.leaves.remove(0);
	}
	
	private boolean hasRebuild(V3D p) {
		if(this.tLogs == null) {
			return false;
		}
		for(V3D t : this.tLogs) {
			if(p.isEqual(t)) {
				return true;
			}
		}
		for(V3D t : this.tLeaves) {
			if(p.isEqual(t)) {
				return true;
			}
		}
		return false;
	}
	
	public List<V3D> getLogs() {
		return this.logs;
	}
	
	public List<V3D> getLeaves() {
		return this.leaves;
	}
	
	public V3D getRoot() {
		return this.root;
	}
	
}
