package com.woog.walle;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.registry.GameData;

public class RayTraceTarget {
	private Minecraft mc = Minecraft.getMinecraft();
	public Block targetBlock;
	public double dis;
	public int x;
	public int y;
	public int z;
	private boolean isDanger;
	private V3D foothold = null;
	public AstarFindWay Astar;
	public V3D target;
//	public List<V3D> way;
	
	public RayTraceTarget(int distance, boolean canBreak) {
		WallE.way = null;
		dis = (double)distance;
		Vec3d vec1 = mc.player.getPositionEyes(1.0F);
		Vec3d vec2 = mc.player.getLook(1.0F);
		Vec3d vec3 = vec1.addVector(vec2.xCoord * dis, vec2.yCoord * dis, vec2.zCoord * dis);
		this.target = new V3D(vec3);
		this.targetBlock = mc.world.getBlockState(new BlockPos(target.x, target.y, target.z)).getBlock();
		this.isDanger = this.isDanger(target);
		if(distance < 2) {
			foothold =new V3D(APIPlayer.posX(), APIPlayer.posY(), APIPlayer.posZ());
			Astar= new AstarFindWay(APIPlayer.getFoot2(), foothold, canBreak);
		}else{
			foothold = getFoothold(target.x, target.y, target.z);
			Astar= new AstarFindWay(APIPlayer.getFoot2(), foothold, canBreak);
		}
		if(!Astar.way.isEmpty()) {
			WallE.way = Astar.way;
			for(int i = 0; i < Astar.way.size(); i++) {
//				System.out.println("【" + i + "】" + "  " + Astar.way.get(i));
			}
//			System.out.println("现在:" + new V3D(vec1) + "  目标:" + v3d1 + "  落脚点:" + foothold);
		}
	}
	
	public RayTraceTarget(V3D targetV3D, boolean canBreak) {
		WallE.way = null;
		dis = APIPlayer.getFoot2().distance(targetV3D);
		Vec3d vec1 = mc.player.getPositionEyes(1.0F);
		Vec3d vec2 = mc.player.getLook(1.0F);
		Vec3d vec3 = vec1.addVector(vec2.xCoord * dis, vec2.yCoord * dis, vec2.zCoord * dis);
		this.target = new V3D(vec3);
		this.targetBlock = mc.world.getBlockState(new BlockPos(target.x, target.y, target.z)).getBlock();
		this.isDanger = this.isDanger(target);
		if(dis < 2.0D) {
			foothold =new V3D(APIPlayer.posX(), APIPlayer.posY(), APIPlayer.posZ());
			Astar= new AstarFindWay(APIPlayer.getFoot2(), foothold, canBreak);
		}else{
			foothold = getFoothold(target.x, target.y, target.z);
			Astar= new AstarFindWay(APIPlayer.getFoot2(), foothold, canBreak);
		}
		if(!Astar.way.isEmpty()) {
			WallE.way = Astar.way;
			for(int i = 0; i < Astar.way.size(); i++) {
			}
//			System.out.println("现在:" + new V3D(vec1) + "  目标:" + v3d1 + "  落脚点:" + foothold);
		}
	}
	
	private V3D[] foots = new V3D[13];
	private double[] socre = new double[13];
	private V3D getFoothold(int x, int y, int z) {
		double max = -9999.0D;
		int s = 1;
		foots[0] = new V3D(mc.player.getPositionEyes(1.0F)).minus(new V3D(0, 1, 0));
		System.out.println(foots[0]);
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
		System.out.printf("%s", this.dis);
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
			System.out.printf("	%s", socre[i]);
//			if(i % 5 == 0) {
//				System.out.printf(" \n");
//			}
		}
		System.out.printf(" \n");
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
				this.dis, APIPlayer.getFoot2().toString(), this.target.toString(), this.foothold.toString());
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
