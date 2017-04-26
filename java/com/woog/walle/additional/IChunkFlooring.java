package com.woog.walle.additional;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.V3DHelper;

import net.minecraft.util.EnumFacing;

public class IChunkFlooring {
	private V3D startPos;
	private V3D squareCenter;
	private EnumFacing lastOppsite;
	private EnumFacing nextFacing;
	private List<V3D> list = new ArrayList<V3D>(20);
	public boolean isSquare = false;
	public List<V3D> square = new ArrayList<V3D>(4);
	//第一块空坐标
	public V3D firstEmpty;
	//第一块站立的坐标
	public V3D firstStandPos;
	//铺地时视线水平朝向
	public EnumFacing facing;
	//开始铺地时的横向移动的方向
	public EnumFacing facingTransverse;
	private int minX;
	private int maxX;
	private int minZ;
	private int maxZ;
	
	public IChunkFlooring() {
		this.startPos = APIPlayer.getFootWithOffset().addY(-1);
		this.getSquare();
		this.minX = Math.min(this.square.get(0).x, this.square.get(2).x);
		this.maxX = Math.max(this.square.get(0).x, this.square.get(2).x);
		this.minZ = Math.min(this.square.get(0).z, this.square.get(2).z);
		this.maxZ = Math.max(this.square.get(0).z, this.square.get(2).z);
		this.getStart();
	}
	
	private void getSquare() {
		list.add(this.getNextCorner(startPos));
		for(int i = 0; i < 100; i++) {
			if(this.isGetSquare()) {
				break;
			}else{
				list.add(this.getNextCorner(this.list.get(i)));
			}
		}
		if(this.isSquare) {
			for(int i = this.list.size() - 2; i > this.list.size() - 6; i--) {
				this.square.add(this.list.get(i));
			}
		}
	}
	
	private void getStart() {
		this.squareCenter = this.getCenter();
		V3D diff = this.squareCenter.minus(this.square.get(0));
		int max = Math.max(Math.abs(diff.x), Math.abs(diff.z));
		List<V3D> list = new ArrayList<V3D>(max * 8);
		aLoop:
		for(int i = max - 1; i > 1; i--) {
			list.clear();
			list = V3DHelper.getSquareEdge(this.squareCenter, i);
			for(V3D pos : list) {
				if(this.isPosInside(pos) && APIChunk.isEmpty(pos)) {
					this.firstEmpty = pos;
					break aLoop;
				}
			}
		}
		V3D direction = V3DHelper.getDirection(this.squareCenter, firstEmpty);
		V3D direct, directTransverse;
		V3D directionOppsite = new V3D(direction.x * -1, 0, direction.z * -1);
		V3D reference = this.firstEmpty.add(direction);
		int x, z;
		if(Math.max(this.maxX - reference.x, reference.x - this.minX) > Math.max(this.maxZ - reference.z, reference.z - this.minZ)) {
			direct = new V3D(direction.x, 0, 0);
			directTransverse = new V3D(0, 0, - direction.z);
		}else{
			direct = new V3D(0, 0, direction.z);
			directTransverse = new V3D(- direction.x, 0, 0);
		}
		EnumFacing face = EnumFacing.getFacingFromVector(direct.x, direct.y, direct.z);
		this.firstStandPos = this.firstEmpty.add(face.getFrontOffsetX(), face.getFrontOffsetY() + 1, face.getFrontOffsetZ());
		this.facing = face;
		this.facingTransverse = EnumFacing.getFacingFromVector(directTransverse.x, directTransverse.y, directTransverse.z);
	}
	
	public boolean isPosInside(V3D pos) {
		return pos.x > this.minX && pos.x < this.maxX && pos.z > this.minZ && pos.z < this.maxZ;
	}
	
	private V3D getCenter() {
		int x = (int) (square.get(0).x + (square.get(2).x - square.get(0).x) / 2);
		int z = (int) (square.get(0).z + (square.get(2).z - square.get(0).z) / 2);
		return new V3D(x, this.square.get(0).y, z);
	}
	
	private V3D getNextCorner(V3D pos) {
		EnumFacing face;
		V3D nextPos = null;
		for(int i = 0; i < 4; i++) {
 			face = EnumFacing.getHorizontal(i);
			if(!face.equals(this.lastOppsite)) {
				nextPos = pos.add(face.getFrontOffsetX(), 0, face.getFrontOffsetZ());
				if(!APIChunk.isEmpty(nextPos)) {
					this.nextFacing = face;
					break;
				}
			}
		}
		this.lastOppsite = this.nextFacing.getOpposite();
		int  n = 1;
		V3D b = pos;
		while(!APIChunk.isEmpty(nextPos)) {
			b = nextPos;
			n++;
			nextPos = pos.add(this.nextFacing.getFrontOffsetX() * n, 0, this.nextFacing.getFrontOffsetZ() * n);
		}
		return b;
	}
	
	private boolean isGetSquare() {
		if(this.list.size() > 4) {
			if(this.list.get(this.list.size() - 1).equals(this.list.get(this.list.size() - 5))) {
				this.isSquare = true;
				return true;
			}
		}
		return false;
	}
}
