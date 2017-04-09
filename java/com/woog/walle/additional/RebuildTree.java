package com.woog.walle.additional;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.APIChunk;
import com.woog.walle.V3D;

public class RebuildTree {
	private V3D pos;
	private List<V3D> logs = new ArrayList<V3D>(10);
	private List<V3D> leaves = new ArrayList<V3D>(10);
	private V3D root;
	public RebuildTree(V3D pos) {
		this.pos = pos;
		this.rebuild(pos);
	}
	
	private void rebuild(V3D p) {
		for(V3D tem : p.getUDLRFB()) {
			if(!hasRebuild(tem)) {
				String name = APIChunk.getBlock(tem).getRegistryName().toString();
				if(name.equals("minecraft:log")) {
					this.logs.add(tem);
					rebuild(tem);
				}
				if(name.equals("minecraft:leaves")) {
					this.leaves.add(tem);
					rebuild(tem);
				}
			}
		}
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
		V3D min = new V3D(0, 999, 0);
		for(V3D t : this.logs) {
			if(t.y < min.y) {
				min = t;
			}
		}
		return min;
	}
}
