package com.woog.walle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.registry.GameData;

public class AstarFindWay {
	//A*寻路算法
	private V3D[] openList;
	private List<V3D> closeList;
	public List<V3D> way;
	private V3D start;
	private V3D end;
	private int step;
	private boolean canBreak;
	private static Block[] air = {Blocks.AIR, Blocks.CARROTS, Blocks.WHEAT, Blocks.REEDS, Blocks.NETHER_WART};
	
	public AstarFindWay(V3D start, V3D end, boolean canBreak) {
		this.start = start;
		this.end = end;
		if(this.start.isEqual(this.end)) {
			return;
		}
		this.canBreak = canBreak;
//		System.out.printf("[S-E]	%s		%s	 \n",start, end);
		double sToe = start.distance(end);
		way  = new ArrayList((int)(sToe * 3));
		closeList = new ArrayList((int)(sToe * 5));
		V3D current = start;					//初始化current路点
		V3D father,son;	
		father = current;					//father路点设置为current路点
		for(int i = 0; i < 200; i++) {		//路线长度限制200步
//			son = getRightPoint(current, father);
			son = getNextPoint(current, father);
			if(son.isEqual(father)) {
				System.out.println("Astar            ==============================");
				closeList.clear();
//				son = getRightPoint(current, null);
//				son = getNextPoint(current, null);
				closeList.add(current);
			}
			if(son.isEqual(start)) {
				way.clear();
			}
			way.add(son);
			closeList.add(son);
			if(way != null && way.size() > 1) {
				father = way.get(way.size() - 2);
			}
			if(son.isEqual(end)) {
				System.out.printf("[%d]	%s		%s	%s \n",i,father, son, end);
				break;
			}
			current = son;
			System.out.printf("[%d]	%s		%s		%s\n",i,father, son, end);
		}
	}
	
	private V3D getRightPoint(V3D current, V3D father) {	//下一路点选取函数
		int right = 0;
		V3D buff;
		double refer = current.centerDistance(end);
		ArrayList<V3D> list = new ArrayList(7);
		ArrayList<V3D> list1 = new ArrayList(7);
		ArrayList<V3D> list2 = new ArrayList(7);
		list1 = current.getNeighbors();
		boolean[] bool = new boolean[list1.size()];
		if(!closeList.isEmpty()) {
			for(int j = 1; j < list1.size(); j++) {
				for(int i = 0; i < closeList.size(); i++) {
					if(list1.get(j).isEqual(closeList.get(i))) {
						bool[j] = true;
					}
				}
			}
			for(int i = 0; i < bool.length; i++) {
				if(!bool[i]) {
					list2.add(list1.get(i));
				}
			}
		}else{
			list2 = list1;
		}
		for(int i = 0; i < list2.size(); i++) {
			buff = list2.get(i);
			if(!buff.isEqual(current) & !buff.isEqual(father)) {
				if(this.canBreak) {
					list.add(list2.get(i));
				}else{
					if(APIChunk.isSafeForStand(buff)) {
						list.add(list2.get(i));
					}
				}
			}
		}
		double[] score = new double[list.size()];	//评价函数
		for(int i = 0; i < list.size(); i++) {
//			System.out.println("   22    " + list.get(i) + "  " + end + "   " + list.get(i).centerDistance(end));
			score[i] = 1 / list.get(i).centerDistance(end);
//			score[i] = (refer / list.get(i).centerDistance(end)) * 10 * Math.abs(Math.sin(current.angle(end)));
//			if(isEmpty(list.get(i))) {
//				score[i] = score[i] + current.distance(end) / start.distance(end);
//			}
//			if(list.get(i).y == current.y) {
//				score[i] = score[i] * 10 * Math.abs(Math.cos(list1.get(i).angle(end)));
//			}
//			score[i] = score[i] ;
		}
		double buf = -9999999.0D;
		for(int i = 0; i < score.length; i++) {	//取评价值最大的脚标
//			System.out.printf("【%s】	%s %s \n", buf, score[i], list.get(i));
			if(score[i] > buf) {
				buf = score[i];
				right = i;
			}
		}
		
		if(!list.isEmpty()) {
			return list.get(right);
		}else{
//			System.out.printf("【ERROR】%b	%s ", list==null, right);
			return null;
		}
	}
	
	private V3D getNextPoint(V3D current, V3D father) {
		double reference = current.distance(this.end);
		List<V3D> crossCube = current.getCrossCube(1);
		List<V3D> list1 = new ArrayList<V3D>(crossCube.size());
		for(V3D temp : crossCube) {
			if(APIChunk.isSafeForStand(temp) & !this.existInCloseList(temp)) {
				list1.add(temp);
			}
		}
		if(list1.size() > 1) {
			double[] list1Evaluate = new double[list1.size()];
			for(int i = 0; i < list1.size(); i++) {
				list1Evaluate[i] = list1.get(i).distance(this.end);
			}
			List<V3D> list2 = new ArrayList<V3D>(list1.size());
			return list1.get(getMin(list1Evaluate));
		}else{
			return father;
		}
	}
	
	private int getMin(double[] array) {
		double min = array[0];
		int b = 0;
		for(int i = 0; i < array.length; i++) {
			if(array[i] < min) {
				min = array[i];
				b = i;
			}
		}
		return b;
	}
	
	private boolean existInCloseList(V3D pos) {
		for(V3D temp : this.closeList) {
			if(temp.isEqual(pos)) {
				return true;
			}
		}
		return false;
	}
}
