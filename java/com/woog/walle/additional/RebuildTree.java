package com.woog.walle.additional;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.APIChunk;
import com.woog.walle.V3D;

public class RebuildTree {
	private V3D pos;
	private List<V3D> logs = new ArrayList<V3D>(10);
	private List<V3D> leaves = new ArrayList<V3D>(20);
	private V3D root;
	private V3D top;
	public boolean isStraight = true;
	public RebuildTree(V3D pos) {
		this.pos = pos;
		this.rebuild(pos);
	}
	
	private void rebuild(V3D p) {
		List<V3D> tLogs = new ArrayList<V3D>(10);
		List<V3D> tLeaves = new ArrayList<V3D>(20);
		
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
		V3D min = this.logs.get(0);
		V3D max = this.logs.get(0);
		for(V3D t : this.logs) {
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
		this.logs = this.getFloors(tLogs);
		this.leaves = tLeaves;
	}
	
	private List<V3D> getFloors(List<V3D> list) {
		List<V3D> b = new ArrayList<V3D>(list.size());
		
		for(int i = 0; i <= this.top.y - this.root.y; i++) {
			for(V3D t : list) {
				if(t.y == this.root.y + i) {
					b.add(t);
				}
			}
		}
		return b;
	}
	
	private boolean hasRebuild(V3D p) {
		if(this.logs == null) {
			return false;
		}
		for(V3D t : this.logs) {
			if(p.isEqual(t)) {
				return true;
			}
		}
		for(V3D t : this.leaves) {
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
