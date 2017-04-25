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
		if(this.start.equals(this.end)) {
			return;
		}
		this.canBreak = canBreak;
//		System.out.printf("[S-E]	%s		%s	 \n",start, end);
		double sToe = start.distance(end);
		this.way  = new ArrayList<V3D>(200);
		this.closeList = new ArrayList<V3D>(200);
		this.closeList.add(start);
		V3D current = start;					//初始化current路点
		V3D father,son = null;	
		father = current;					//father路点设置为current路点
		
		for(int i = 0; i < 200; i++) {		//路线长度限制200步
			son = this.getSon(current, father);
			if(son.equals(start)) {
				way.clear();
			}
			way.add(son);
			closeList.add(son);
			if(way != null && way.size() > 2) {
				father = way.get(way.size() - 2);
			}
			if(son.equals(end)) {
//				System.out.printf("[%d]	%s		%s	到达：%s \n",i,father, son, end);
				break;
			}
			current = son;
//			System.out.printf("[%d]	%s		%s		%s\n",i,father, son, end);
		}
	}
	
	private V3D getSon(V3D curr, V3D fa) {
		V3D b = getNextPoint(curr, fa);
		if(b == null) {
			if(this.way != null && this.way.size() > 0) {
//				System.out.println("Astar  " + curr + "   " + fa);
				curr = this.way.get(this.way.size() - 1);
//				System.out.println("Astar  " + curr + "   " + fa);
				this.way.remove(this.way.size() - 1);
				fa = null;
				b = this.getSon(curr, fa);
			}else{
				b = this.getSon(start, null);
			}
		}
		return b;
	}
	
	private V3D getNextPoint(V3D current, V3D father) {
		List<V3D> crossCube = V3DHelper.getCrossCube(current, 1);
		List<V3D> list1 = new ArrayList<V3D>(crossCube.size());
		for(V3D temp : crossCube) {
			if(APIChunk.isSafeForStand(temp) & !this.existInCloseList(temp)) {
				list1.add(temp);
			}
		}
		if(!list1.isEmpty()) {
			double[] list1Evaluate = new double[list1.size()];
			for(int i = 0; i < list1.size(); i++) {
				list1Evaluate[i] = list1.get(i).distance(this.end);
			}
			List<V3D> list2 = new ArrayList<V3D>(list1.size());
			return list1.get(getMin(list1Evaluate));
		}else{
			return null;
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
			if(temp.equals(pos)) {
				return true;
			}
		}
		return false;
	}
}
