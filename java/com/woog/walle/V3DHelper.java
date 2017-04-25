package com.woog.walle;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.additional.IDirection;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.registry.GameData;

public class V3DHelper {
	
	/**
	 * 计算posA和posB坐标距离
	 * @param posA
	 * @param posB
	 * @return
	 */
	public static double distance(V3D posA, V3D posB) {
		double ax = (double)posA.x;
		double ay = (double)posA.y;
		double az = (double)posA.z;
		double bx = (double)posB.x;
		double by = (double)posB.y;
		double bz = (double)posB.z;
		return Math.sqrt(Math.pow(ax - bx, 2.0D) + Math.pow(ay - by, 2.0D) + Math.pow(az - bz, 2.0D));
	}
	
	/**
	 * 计算posA和posB中心点的距离
	 * @param posA
	 * @param posB
	 * @return
	 */
	public static double centerDistance(V3D posA, V3D posB) {
		double ax = (double)posA.x + 0.5D;
		double ay = (double)posA.y + 0.5D;
		double az = (double)posA.z + 0.5D;
		double bx = (double)posB.x + 0.5D;
		double by = (double)posB.y + 0.5D;
		double bz = (double)posB.z + 0.5D;
		return Math.sqrt(Math.pow(ax - bx, 2.0D) + Math.pow(ay - by, 2.0D) + Math.pow(az - bz, 2.0D));
	}
	
	/**
	 * 判断posA的方块挖掉后是否危险
	 * @param posA
	 * @return true：危险； false：安全
	 */
	public static boolean isDangerAjacent(V3D posA) {
		if(APIChunk.getMaterial(posA).isLiquid()) {
			return true;
		}
		V3D[] adjacent = V3DHelper.getUDLRFB(posA);
		for(int i = 0; i < adjacent.length; i++) {
			if(APIChunk.getMaterial(adjacent[i]).isLiquid()) {
				return true;
			}
		}
		return false;
	}
	
	public static Vec3d getFaceCenter(V3D posA, EnumFacing face) {
		Vec3d center = posA.getCenter();
		return center.addVector(face.getFrontOffsetX() * 0.5, face.getFrontOffsetY() * 0.5, face.getFrontOffsetZ() * 0.5);
	}
	
	/**
	 * posA + 相邻的6个坐标
	 */
	public static V3D[] getNeighborsAndThis(V3D posA) {
		int x = posA.x;
		int y = posA.y;
		int z = posA.z;
		V3D[] phase =new V3D[7];
		phase[0] = new V3D(x, y, z);
		phase[1] = new V3D(x - 1, y, z);
		phase[2] = new V3D(x + 1, y, z);
		phase[3] = new V3D(x, y, z - 1);
		phase[4] = new V3D(x, y, z + 1);
		phase[5] = new V3D(x, y - 1, z);
		phase[6] = new V3D(x, y + 1, z);
		return phase;
	}
	
	/**
	 * 相邻的6个坐标列表
	 */
	public static ArrayList<V3D> getNeighbors(V3D posA) {
		int x = posA.x;
		int y = posA.y;
		int z = posA.z;
		ArrayList<V3D> phase = new ArrayList();
		phase.add(new V3D(x - 1, y, z));
		phase.add(new V3D(x + 1, y, z));
		phase.add(new V3D(x, y, z - 1));
		phase.add(new V3D(x, y, z + 1));
		phase.add(new V3D(x, y - 1, z));
		phase.add(new V3D(x, y + 1, z));
		return phase;
	}
	
	/**
	 * 相邻6个方块的中心点列表
	 */
	public static ArrayList<Vec3d> targetPhaseCenterList(V3D posA) {
		int x = posA.x;
		int y = posA.y;
		int z = posA.z;
		ArrayList<Vec3d> phase = new ArrayList();
		phase.add(new Vec3d(x + 0.5, y + 0.5, z + 0.5));
		phase.add(new Vec3d(x - 0.5, y + 0.5, z + 0.5));
		phase.add(new Vec3d(x + 1.5, y + 0.5, z + 0.5));
		phase.add(new Vec3d(x + 0.5, y + 0.5, z - 0.5));
		phase.add(new Vec3d(x + 0.5, y + 0.5, z + 1.5));
		phase.add(new Vec3d(x + 0.5, y - 0.5, z + 0.5));
		phase.add(new Vec3d(x + 0.5, y + 1.5, z + 0.5));
		return phase;
	}
	
	
	/**
	 * 返回一个以posA为原点， 以2 * dis + 1为棱长的正方体
	 * @param posA 正方体中心坐标
	 * @param dis 正方体中心点至棱的水平距离
	 */
	public static List<V3D> getCubeByDistance(V3D posA, int dis) {
		List<V3D> b = new ArrayList<V3D>((int)Math.pow(2 * dis + 1, 3));
		for(int i = - dis; i <= dis; i++) {
			for(int j = - dis; j <= dis; j++) {
				for(int k = - dis; k <= dis; k++) {
					b.add(new V3D(posA.x + i, posA.y + j, posA.z + k));
				}
			}
		}
		return b;
	}
	
	/**
	 * 以此posA为球心，指定球体半径， 返回球体内的整数坐标点
	 * @param posA 球心坐标
	 * @param dis 球体中心至球面的距离
	 */
	public static List<V3D> getPosByDistance(V3D posA, int dis) {
		if(dis == 0) {
			return null;
		}
		List<V3D> b = new ArrayList<V3D>(dis * dis * dis);
		V3D temp;
		for(int i = - dis; i <= dis; i++) {
			for(int j = - dis; j <= dis; j++) {
				for(int k = - dis; k <= dis; k++) {
					temp = new V3D(posA.x + i, posA.y + j, posA.z + k);
					if(posA.distance(temp) <= dis) {
						b.add(temp);
					}
				}
			}
		}
		return b;
	}
	
	/**
	 * 以origin为中心点呈 (2 * distance + 1)^2 的矩形水平向外辐射扩散， 返回数值遵循由近至远、先中间后两边的原则
	 * @param origin 作为中心点的V3D坐标
	 * @param distance 向外扩散的距离
	 * @return 方块的V3D坐标
	 */
	public static List<V3D> getRadiantSquare(V3D origin, int distance) {
		List<V3D> delta = new ArrayList<V3D>((2 * distance + 1) * (2 * distance + 1));
		List<V3D> radiant = new ArrayList<V3D>((2 * distance + 1) * (2 * distance + 1));
		delta.add(new V3D(0, 0, 0));
		if(distance > 0) {
			for(int i = 1; i <= distance; i++) {
				delta.add(new V3D(0, 0, - i));
				delta.add(new V3D(- i, 0, 0));
				delta.add(new V3D(0, 0, i));
				delta.add(new V3D(i, 0, 0));
				if(distance > 1) {
					for(int n = 1; n < i; n++) {
						delta.add(new V3D(n, 0, + i));
						delta.add(new V3D(n, 0, - i));
						delta.add(new V3D(- n, 0, + i));
						delta.add(new V3D(- n, 0, - i));
						delta.add(new V3D(i, 0, n));
						delta.add(new V3D(- i, 0, n));
						delta.add(new V3D(i, 0, -n));
						delta.add(new V3D(- i, 0, -n));
					}
				}
				delta.add(new V3D(i, 0, - i));
				delta.add(new V3D(i, 0, i));
				delta.add(new V3D(- i, 0, - i));
				delta.add(new V3D(- i, 0, i));
			}
		}
		for(int i = 0; i < delta.size(); i++) {
			radiant.add(delta.get(i).add(origin));
		}
		return radiant;
	}
	
	/**
	 * 获取以此坐标点为中心， 各边距离为dis的正方形的边
	 * @param dis
	 * @return
	 */
	public static List<V3D> getSquareEdge(V3D posA, int dis) {
		List<V3D> b = new ArrayList<V3D>(dis * 8);
		b.add(new V3D(posA.x - dis, posA.y, posA.z - dis));
		b.add(new V3D(posA.x + dis, posA.y, posA.z - dis));
		b.add(new V3D(posA.x + dis, posA.y, posA.z + dis));
		b.add(new V3D(posA.x - dis, posA.y, posA.z + dis));
		for(int i = 1; i < 2 * dis; i++) {
			b.add(b.get(0).add(i, 0, 0));
			b.add(b.get(1).add(0, 0, i));
			b.add(b.get(2).add(-i, 0, 0));
			b.add(b.get(3).add(0, 0, -i));
		}
		return b;
	}
	
	/**
	 * 获得当前方块的上下及四周方块的坐标
	 * @return V3D[6]
	 */
	public static V3D[] getUDLRFB(V3D posA) {
		V3D[] udlrfb = new V3D[6];
		udlrfb[0] = new V3D(posA.x, posA.y + 1, posA.z);
		udlrfb[1] = new V3D(posA.x, posA.y - 1, posA.z);
		udlrfb[2] = new V3D(posA.x - 1, posA.y, posA.z);
		udlrfb[3] = new V3D(posA.x + 1, posA.y, posA.z);
		udlrfb[4] = new V3D(posA.x, posA.y, posA.z + 1);
		udlrfb[5] = new V3D(posA.x, posA.y, posA.z - 1);
		return udlrfb;
	}
	
	/**
	 * 在水平方向上由内向外呈十字形辐射， 每侧长度为distance， 不含中心点
	 * @param distance 十字每侧的长度
	 * @return
	 */
	public static List<V3D> getCrossPlane(V3D posA, int distance) {
		List<V3D> b = new ArrayList<V3D>(distance * 4);
		for(int i = - distance; i <= distance; i++) {
			for(int j = - distance; j <= distance; j++) {
				if(i * j == 0 & !(i == 0 & j == 0)) {
					b.add(new V3D(posA.x + i, posA.y, posA.z + j));
				}
			}
		}
		return b;
	}
	
	/**
	 * 以此坐标为原点向x、z正方向、负方向延伸距离为distance， 不含中心点, 所形成的十字高度为2 * distance + 1
	 * @param distance 辐射长度
	 */
	public static List<V3D> getCrossCube(V3D posA, int distance) {
		List<V3D> b = new ArrayList<V3D>(distance * distance * 8 + distance * 6);
		for(int i = - distance; i <= distance; i++) {
			for(int j = - distance; j <= distance; j++) {
				for(int k = - distance; k <= distance; k++) {
					if(i * j * k == 0 & !(i == 0 & j == 0 & k ==0) & !(j == 0 & i * k != 0)) {
						b.add(new V3D(posA.x + i, posA.y + j, posA.z + k));
					}
				}
			}
		}
		return b;
	}
	
	/**
	 * 获取posA相邻6个方块邻近面的中心点
	 */
	public static Vec3d getCenterOfNearestSide(V3D posA) {
		V3D diff = posA.minus(APIPlayer.getFootWithOffset());
		Vec3d sideDiff = new Vec3d(posA.x + 0.0D, posA.y + 0.0D, posA.z + 0.0D);
		if(diff.x > 0) {
			sideDiff = new Vec3d(posA.x + 0.0D, posA.y + 0.5D, posA.z + 0.5D);
		}else if(diff.x < 0) {
			sideDiff = new Vec3d(posA.x + 1.0D, posA.y + 0.5D, posA.z + 0.5D);
		}else if(diff.z > 0) {
			sideDiff = new Vec3d(posA.x + 0.5D, posA.y + 0.5D, posA.z + 0.0D);
		}else if(diff.z < 0) {
			sideDiff = new Vec3d(posA.x + 0.5D, posA.y + 0.5D, posA.z + 1.0D);
		}else if(diff.y > 0) {
			sideDiff = new Vec3d(posA.x + 0.5D, posA.y + 0.0D, posA.z + 0.5D);
		}else if(diff.y < 0) {
			sideDiff = new Vec3d(posA.x + 0.5D, posA.y + 1.0D, posA.z + 0.5D);
		}
		return sideDiff;
	}
	
	/**
	 * A坐标点指向origin坐标点的方向坐标
	 * @return
	 */
	public static V3D getDirection(V3D posA, V3D origin) {
		V3D t = origin.minus(posA);
		return new V3D(Integer.signum(t.x), Integer.signum(t.y), Integer.signum(t.z));
	}
	
	/**
	 * 获得此坐标水平面周围指定距离内的所有坐标，方向优先, 先正方向后斜方向，先前方向， 由近至远
	 * @param dis
	 * @return
	 */
	public static V3D[] getNeighborByDistance(V3D posA, int dis) {
		V3D[] b = new V3D[(dis + 1) * dis * 4];
		int index = 0;
		IDirection direction = new IDirection();
		V3D[] wads = direction.getWADS();
		for(int i = 0; i < wads.length; i++) {
			for(int j = 1; j <= dis; j++) {
				b[index] = posA.add(wads[i].multiply(j));
				index++;
			}
		}
		V3D[] incline = direction.getIncline();
		for(int i = 0; i < incline.length; i++) {
			for(int j = 1; j <= dis; j++) {
				for(int k = 1; k < j; k++) {
					b[index] = posA.add(incline[i].multiplyXSetZ(k, j));
					index++;
					b[index] = posA.add(incline[i].multiplyZSetX(k, j));
					index++;
				}
				b[index] = posA.add(incline[i].multiply(j));
				index++;
			}
		}
		return b;
	}
	
	public static BlockPos toBlockPos(V3D posA) {
		return new BlockPos(posA.x, posA.y, posA.z);
	}
}
