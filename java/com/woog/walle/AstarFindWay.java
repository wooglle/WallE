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
		this.canBreak = canBreak;
		System.out.printf("[S-E]	%s		%s	 \n",start, end);
		double sToe = start.distance(end);
		way  = new ArrayList((int)(sToe * 3));
		closeList = new ArrayList((int)(sToe * 5));
		V3D current = start;					//初始化current路点
		V3D father,son;	
		father = current;					//father路点设置为current路点
		for(int i = 0; i < 200; i++) {		//路线长度限制200步
			son = getRightPoint(current, father);
			way.add(son);
			closeList.add(son);
			if(i > 1) {
				father = way.get(way.size() - 2);
			}
			if(current.isEqual(end)) {
				break;
			}
			current = son;
			System.out.printf("[%d]	%s		%s	%s	%s \n",i,father, son,getId(son),isDanger(son));
		}
	}
	
	private V3D getRightPoint(V3D current, V3D father) {	//下一路点选取函数
		int right = 0;
		V3D buff;
		double refer = current.centerDistance(end);
		ArrayList<V3D> list = new ArrayList(7);
		ArrayList<V3D> list1 = new ArrayList(7);
		ArrayList<V3D> list2 = new ArrayList(7);
		list1 = current.targetPhaseList();		
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
			if(!isDanger(buff) && !buff.isEqual(current) && !buff.isEqual(father)) {
				list.add(list2.get(i));
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

	private boolean isNear() {
		return false;
	}
	
	private boolean isEmpty(V3D target) {
		int x = target.x;
		int y = target.y + 1;
		int z = target.z;
//		if(getId(target) == 0 && getId(x, y, z) == 0) {
//			return true;
//		}
		if(isNullBlock(target) && isNullBlock(new V3D(x, y, z))) {
			System.out.println("isNull: " + target + "    " + x + " " + y + " " + z);
			return true;
		}
		return false;
	}
	
	private boolean isNullBlock(V3D target) {
		switch(getId(target)) {
		case 0: return true;
		case 31: return true;
		case 32: return true;
		case 37: return true;
		case 38: return true;
		case 39: return true;
		case 40: return true;
		case 50: return true;
		case 55: return true;
		case 59: return true;
		case 63: return true;
		case 66: return true;
		case 68: return true;
		case 69: return true;
		case 70: return true;
		case 72: return true;
		case 75: return true;
		case 76: return true;
		case 77: return true;
		case 83: return true;
		case 115: return true;
		case 141: return true;
		case 142: return true;
		case 171: return true;
		case 175: return true;
		}
		return false;
	}
	
	private boolean isDanger(V3D target) {
		int id,id2;
		V3D[] phase = target.targetAndPhase();
		V3D target2 = new V3D(target.x, target.y + 1, target.z);
		V3D[] phase2 = target2.targetAndPhase();
		if(this.isBelongtoBlock(APIChunk.getBlock(phase[0])) && this.isBelongtoBlock(APIChunk.getBlock(phase2[0]))) {return false;}
//		if(getId(phase[0]) == 0 && getId(phase2[0]) == 0) {return false;}
		for(int i = 1; i <= 6; i++) {
			id = getId(phase[i]);
			id2 = getId(phase2[i]);
			if(this.canBreak) {
				if(id == 8 || id == 9 || id == 10 || id == 11) {
					return true;
				}
				if(id2 == 8 || id2 == 9 || id2 == 10 || id2 == 11) {
					return true;
				}
			}else{
				if(!isNullBlock(target)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean isBelongtoBlock(Block block) {
		for(int i = 0; i < this.air.length; i++) {
			if(block == this.air[i]) {
				return true;
			}
		}
		return false;
	} 
	
	private int getId(V3D target) {
		return GameData.getBlockRegistry().getIDForObject(Minecraft.getMinecraft().world.getBlockState(new BlockPos(target.x, target.y, target.z)).getBlock());
	}
	
	private int getId(int x, int y, int z) {
		return GameData.getBlockRegistry().getIDForObject(Minecraft.getMinecraft().world.getBlockState(new BlockPos(x, y, z)).getBlock());
	}
}
