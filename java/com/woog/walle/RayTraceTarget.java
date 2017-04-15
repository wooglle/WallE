package com.woog.walle;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.registry.GameData;

public class RayTraceTarget {
	private Minecraft mc = Minecraft.getMinecraft();
	public Block targetBlock;
	private boolean isDanger;
	private V3D foothold = null;
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
		this.isDanger = this.isDanger(target);
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
		double dis = APIPlayer.getFootWithOffset().centerDistance(targetV3D);
		this.target = targetV3D;
		this.targetBlock = mc.world.getBlockState(new BlockPos(target.x, target.y, target.z)).getBlock();
		this.isDanger = this.isDanger(target);
		if(dis < 2.9D) {
			if(APIChunk.isSafeForStand(targetV3D)) {
				foothold = targetV3D;
			}else{
				foothold =new V3D(APIPlayer.posX(), APIPlayer.posY(), APIPlayer.posZ());
			}
			Astar= new AstarFindWay(APIPlayer.getFootWithOffset(), foothold, this.canEditeBlock);
		}else{
			if(APIChunk.isSafeForStand(targetV3D)) {
				foothold = targetV3D;
			}else{
				foothold = getFoothold(target);
			}
			Astar= new AstarFindWay(APIPlayer.getFootWithOffset(), foothold, this.canEditeBlock);
		}
		if(!Astar.way.isEmpty()) {
			WallE.way = Astar.way;
			System.out.println("RayTraceTarget 1:  " + APIPlayer.getFootWithOffset() + "  目标:" + targetV3D + "  落脚点:" + foothold);
		}
	}
	
	/**
	 * 自动获取到达指定坐标的路线
	 * @param targetV3D 目标（即落脚点）
	 */
	public RayTraceTarget(V3D targetV3D) {
		this.foothold = targetV3D;
		this.canEditeBlock = false;
		WallE.way.clear();
		double dis = APIPlayer.getFootWithOffset().centerDistance(targetV3D);
		this.target = targetV3D;
		this.targetBlock = APIChunk.getBlock(targetV3D);
		this.isDanger = false;
		Astar= new AstarFindWay(APIPlayer.getFootWithOffset(), target, false);
		if(!Astar.way.isEmpty()) {
			WallE.way = Astar.way;
			System.out.println("RayTraceTarget 2:  " + APIPlayer.getFootWithOffset() + "  目标:" + targetV3D + "  落脚点:" + foothold);
		}
	}
	
	private V3D getFoothold(V3D targetPos) {
		V3D[] foots = new V3D[13];
		double[] socre = new double[13];
		int x = targetPos.x;
		int y = targetPos.y;
		int z = targetPos.z;
		double max = -9999.0D;
		int s = 1;
		foots[0] = new V3D(mc.player.getPositionEyes(1.0F)).addY(1);
		foots[1] = new V3D(x - 1, y - 1, z);
		foots[2] = new V3D(x + 1, y - 1, z);
		foots[3] = new V3D(x, y - 1, z - 1);
		foots[4] = new V3D(x, y - 1, z + 1);
		foots[5] = new V3D(x - 1, y, z);
		foots[6] = new V3D(x + 1, y, z);
		foots[7] = new V3D(x , y, z - 1);
		foots[8] = new V3D(x , y, z + 1);
		foots[9] = new V3D(x , y + 1, z);
		foots[10] = new V3D(x , y - 2, z);
		foots[11] = new V3D(x , y - 3, z);
		foots[12] = new V3D(x , y - 4, z);
		int firsty = getFirstFootholdy(foots[0]);
		for(int i = 1; i < foots.length; i++) {
			socre[i] = socre[i] - Math.abs(foots[i].y - foots[0].y) * 10;
			if(foots[i].y == firsty) {
				int num = (int)socre[i],count = 0;
				while(num > 0) {
					num /= 10;
					count++;
				}
				socre[i] = socre[i] + Math.pow(10, num + 1) ;
			}
			socre[i] = socre[i] - foots[0].distance(foots[i]);
//			System.out.printf("	%s", socre[i]);
//			if(i % 5 == 0) {
//				System.out.printf(" \n");
//			}
		}
//		System.out.printf(" \n");
		for(int i = 1; i < socre.length; i++) {
			if(socre[i] > max) {
				s = i;
				max = socre[i];
			}
		}
		return foots[s];
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
	
	private boolean isDanger(V3D target) {
		int id;
		V3D[] phase = target.targetAndPhase();
		if(getId(phase[0]) == 0) return false;
		for(int i = 1; i <= 6; i++) {
			id = getId(phase[i]);
			if(id == 8 || id == 9 || id == 10 || id == 11) return true;
		}
		return false;
	}

	private boolean isDanger(int x, int y, int z) {
		int id = getId(x, y, z);
		if(id == 8 || id == 9 || id == 10 || id == 11) {
			return true;
		}
		id = getId(x - 1, y, z);
		if(id == 8 || id == 9 || id == 10 || id == 11) {
			return true;
		}
		id = getId(x + 1, y, z);
		if(id == 8 || id == 9 || id == 10 || id == 11) {
			return true;
		}
		id = getId(x, y - 1, z);
		if(id == 8 || id == 9 || id == 10 || id == 11) {
			return true;
		}
		id = getId(x, y + 1, z);
		if(id == 8 || id == 9 || id == 10 || id == 11) {
			return true;
		}
		id = getId(x, y, z - 1);
		if(id == 8 || id == 9 || id == 10 || id == 11) {
			return true;
		}
		id = getId(x, y, z + 1);
		if(id == 8 || id == 9 || id == 10 || id == 11) {
			return true;
		}
		return false;
	}
	
	private int getId(int x, int y, int z) {
		return GameData.getBlockRegistry().getIDForObject(mc.world.getBlockState(new BlockPos(x, y, z)).getBlock());
	}
	
	private int getId(V3D target) {
		return GameData.getBlockRegistry().getIDForObject(mc.world.getBlockState(new BlockPos(target.x, target.y, target.z)).getBlock());
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
