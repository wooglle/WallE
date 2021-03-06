package com.woog.walle;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.registry.GameData;

public class RayTraceTarget {
	private Minecraft mc = Minecraft.getMinecraft();
	public Block targetBlock;
	private boolean isDanger;
	public V3D foothold = null;
	public AstarFindWay Astar;
	public V3D target;
	private boolean canEditeBlock = false;
//	public List<V3D> way;
	
	public RayTraceTarget(int distance, boolean canBreak) {
		this.canEditeBlock = canBreak;
		WallE.way.clear();
		double dis = (double)distance;
		Vec3d vec1 = mc.player.getPositionEyes(1.0F);
		Vec3d vec2 = mc.player.getLook(1.0F);
		Vec3d vec3 = vec1.addVector(vec2.xCoord * dis, vec2.yCoord * dis, vec2.zCoord * dis);
		this.target = new V3D(vec3);
		this.targetBlock = mc.world.getBlockState(new BlockPos(target.x, target.y, target.z)).getBlock();
		this.isDanger = APIChunk.isSafeForStand(target);
		if(distance < 2) {
			foothold =new V3D(APIPlayer.posX(), APIPlayer.posY(), APIPlayer.posZ());
			Astar= new AstarFindWay(APIPlayer.getFootWithOffset(), foothold, this.canEditeBlock);
		}else{
			foothold = getFoothold(target);
			Astar= new AstarFindWay(APIPlayer.getFootWithOffset(), foothold, this.canEditeBlock);
		}
		if(!Astar.way.isEmpty()) {
			WallE.way = Astar.way;
			for(int i = 0; i < Astar.way.size(); i++) {
				System.out.println("【" + i + "】" + "  " + Astar.way.get(i));
			}
//			System.out.println("现在:" + new V3D(vec1) + "  目标:" + v3d1 + "  落脚点:" + foothold);
		}
	}
	
	/**
	 * 自动获取指定坐标的落脚点及路线
	 * @param targetV3D 指定目标
	 * @param canBreak 是否破坏方块
	 */
	public RayTraceTarget(V3D targetV3D, boolean canBreak) {
		this.canEditeBlock = canBreak;
		WallE.way.clear();
		double dis = APIPlayer.getFootWithOffset().distance(targetV3D);
		this.target = targetV3D;
		this.targetBlock = mc.world.getBlockState(new BlockPos(target.x, target.y, target.z)).getBlock();
		this.isDanger = APIChunk.isSafeForStand(target);
		foothold = getFoothold(target);
		if(foothold == null) {
			return;
		}
		Astar= new AstarFindWay(APIPlayer.getFootWithOffset(), foothold, this.canEditeBlock);
		if(Astar.way != null && !Astar.way.isEmpty()) {
			WallE.way = Astar.way;
//			System.out.println("RayTraceTarget 1:  " + APIPlayer.getFootWithOffset() + "  目标:" + targetV3D + "  落脚点:" + foothold);
		}
	}
	
	/**
	 * 自动获取到达指定坐标的路线, 无限制模式
	 * @param targetV3D 目标（即落脚点）
	 */
	public RayTraceTarget(V3D targetV3D) {
		this.foothold = targetV3D;
		this.canEditeBlock = false;
		this.target = targetV3D;
		WallE.way.clear();
		double dis = V3DHelper.centerDistance(APIPlayer.getFootWithOffset(), targetV3D);
		this.targetBlock = APIChunk.getBlock(targetV3D);
		this.isDanger = false;
		Astar= new AstarFindWay(APIPlayer.getFootWithOffset(), target, false);
		if(!Astar.way.isEmpty()) {
			WallE.way = Astar.way;
//			System.out.println("RayTraceTarget 2:  " + APIPlayer.getFootWithOffset() + "  目标:" + targetV3D + "  落脚点:" + foothold);
		}
	}
	
	private V3D getFoothold(V3D targetPos) {
		List<V3D> foots = APIChunk.getStandModel(targetPos);
		V3D footNow = APIPlayer.getFootWithOffset();
		List<V3D> list1 = new ArrayList<V3D>(foots.size());
		for(V3D tem : foots) {
			if(APIChunk.isSafeForStand(tem)) {
				list1.add(tem);
			}
		}
		if(list1 == null || list1.isEmpty()) {
			foots = V3DHelper.getPosByDistance(targetPos, 5);
			list1 = new ArrayList<V3D>(foots.size());
			for(V3D tem : foots) {
				if(APIChunk.isSafeForStand(tem)) {
					list1.add(tem);
				}
			}
		}
		
		List<V3D> list2 = new ArrayList<V3D>(4);
		List<V3D> list3 = new ArrayList<V3D>(list1.size());
		List<Double> list2Socre = new ArrayList<Double>(4);
		List<Double> list3Socre = new ArrayList<Double>(list1.size());
		boolean hasUpPos = false;
		
		if(!list1.isEmpty()) {
			for(int i = 0; i < list1.size(); i++) {
				if(list1.get(i).y == footNow.y) {
					list2.add(list1.get(i));
					list2Socre.add(list1.get(i).distance(targetPos));
				}
				if(list1.get(i).y == targetPos.y + 1) {
					hasUpPos = true;
				}
				list3.add(list1.get(i));
				list3Socre.add(list1.get(i).addY(1).distance(targetPos));
			}
			if(!list2.isEmpty()) {
				if(list2.size() > 1) {
					int index = getFirstMin(list2Socre);
					return list2.get(index);
				}else{
					return list2.get(0);
				}
			}
			if(!list3.isEmpty()) {
				int index = getFirstMin(list3Socre);
				return list3.get(index);
			}
		}else{
			return APIPlayer.getFootWithOffset();
		}
		return null;
	}
	
	private int getFirstMin(List<Double> list) {
		double min = 999999D;
		int index = 0;
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i) < min) {
				min = list.get(i);
				index = i;
			}
		}
		return index;
	}
	
	private int getFirstFootholdy(V3D position) {
		V3D diff = this.target.minus(position);
		if(0 <= diff.y & diff.y < 5) {
			return position.y;
		}else if(diff.y >= 5) {
			return target.y - 3;
		}else{
			return target.y + 1;
		}
	}
	
	public void info() {
		System.out.printf("【RayTraceTarget】  [%s]    now: (%s), target: (%s), foothold: (%s)\n", 
				APIPlayer.getFootWithOffset().toString(), this.target.toString(), this.foothold.toString());
		if(!WallE.way.isEmpty()) {
			for(int i = 0; i < Astar.way.size(); i++) {
				System.out.printf("%s; ",Astar.way.get(i));
				if((i + 1) % 5 == 0) System.out.printf(" |  ");
				if((i + 1) % 10 == 0) System.out.printf("\n");
			}
		}else{
			System.out.printf("NULL.");
		}
		System.out.printf("\n");
	}
}
