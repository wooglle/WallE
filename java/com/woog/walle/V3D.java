package com.woog.walle;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.woog.walle.additional.IDirection;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.registry.GameData;

@Immutable
public class V3D {
	public int x;
	public int y;
	public int z;
	public static V3D[] wise= {new V3D(0, 0, 1), new V3D(-1, 0, 0), new V3D(0, 0, -1), new V3D(1, 0, 0)};
	
	public V3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public V3D(double x, double y, double z) {
		this.x = (int)Math.floor(x);
		this.y = (int)Math.floor(y);
		this.z = (int)Math.floor(z);
	}
	
//	public V3D(float x, float y, float z) {
//		this.x = (int)x;
//		this.y = (int)y;
//		this.z = (int)z;
//	}
	
	public V3D(int x, int y, int z, int side) {
		this.x = x;
		this.y = y;
		this.z = z;
//		this.side = side;
	}
	
	public V3D(Vec3i vec3i) {
		this.x = vec3i.getX();
		this.y = vec3i.getY();
		this.z = vec3i.getZ();
	}
	
	public Vec3d toVec3() {
		return new Vec3d((double)this.x, (double)this.y, (double)this.z);
	}
	
	public Vec3d getCenter() {
		return new Vec3d((double)this.x + 0.5D, (double)this.y + 0.5D, (double)this.z + 0.5D);
	}
	
	public V3D getOpposite() {
		return new V3D(- this.x, - this.y, - this.z);
	}
	
	public V3D(Vec3d position) {
		this.x = (int)Math.floor(position.xCoord);
		this.y = (int)Math.floor(position.yCoord);
		this.z = (int)Math.floor(position.zCoord);
	}
	
	public V3D(BlockPos pos) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}
	
	public double distance(V3D b) {
		double ax = (double)this.x;
		double ay = (double)this.y;
		double az = (double)this.z;
		double bx = (double)b.x;
		double by = (double)b.y;
		double bz = (double)b.z;
		return Math.sqrt(Math.pow(ax - bx, 2.0D) + Math.pow(ay - by, 2.0D) + Math.pow(az - bz, 2.0D));
	}
	
	public double centerDistance(V3D b) {
		double ax = (double)this.x + 0.5D;
		double ay = (double)this.y + 0.5D;
		double az = (double)this.z + 0.5D;
		double bx = (double)b.x + 0.5D;
		double by = (double)b.y + 0.5D;
		double bz = (double)b.z + 0.5D;
		return Math.sqrt(Math.pow(ax - bx, 2.0D) + Math.pow(ay - by, 2.0D) + Math.pow(az - bz, 2.0D));
	}
	
	public V3D add(V3D b) {
		return new V3D(this.x + b.x, this.y + b.y, this.z + b.z);
	}
	
	public V3D add(int dx, int dy, int dz) {
		return new V3D(this.x + dx, this.y + dy, this.z + dz);
	}
	
	public V3D addY(int y) {
		return new V3D(this.x, this.y + y, this.z);
	}
	
	public V3D minus(V3D b) {
		return new V3D(this.x - b.x, this.y - b.y, this.z - b.z);
	}
	
	public V3D multiply(int i) {
		return new V3D(this.x * i, this.y * i, this.z * i);
	}
	
	public V3D multiplyX(int i) {
		return new V3D(this.x * i, this.y, this.z);
	}
	
	public V3D multiplyZ(int i) {
		return new V3D(this.x, this.y, this.z * i);
	}
	
	public double angle (V3D b) {
		return Math.asin(Math.abs(this.y - b.y) / this.distance(b)) * Math.PI / 180;
	}
	
	public boolean isEqual(V3D b) {
		if(b == null) {
			return false;
		}
//		System.out.println("  " + this.toString() + "  " + b + "  " + (this.x == b.x & this.y == b.y & this.z == b.z));
		return this.x == b.x & this.y == b.y & this.z == b.z;
	}
	
	@Override
	public boolean equals(Object ob) {
		if(this == ob) {
			return true;
		}else if(!(ob instanceof V3D)){
			return false;
		}else{
			V3D obj = (V3D)ob;
			return this.x == obj.x && this.y == obj.y && this.z == obj.z;
		}
	}
	
	public boolean isDanger(int[] IDs) {
		int id = this.getId();
		for(int i = 0; i < IDs.length; i++) {
			if(id == IDs[i]) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDangerAjacent() {
		if(this.isDanger(WallE.dangerId)) {
			return true;
		}
		V3D[] adjacent = this.getUDLRFB();
		String out = "ID： ";
		for(int i = 0; i < adjacent.length; i++) {
			int id = adjacent[i].getId();
			out += id + ", ";
			for(int j = 0; j < WallE.dangerId.length; j++) {
				if(id == WallE.dangerId[j]) {
//					System.out.println(out);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 此坐标 + 相邻的6个坐标
	 * @return
	 */
	public V3D[] getNeighborsAndThis() {
		int x = this.x;
		int y = this.y;
		int z = this.z;
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
	 * @return
	 */
	public ArrayList<V3D> getNeighbors() {
		int x = this.x;
		int y = this.y;
		int z = this.z;
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
	 * @return
	 */
	public ArrayList<Vec3d> targetPhaseCenterList() {
		int x = this.x;
		int y = this.y;
		int z = this.z;
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
	 * 返回一个以此坐标为原点， 以2 * dis为棱长的正方体
	 * @param dis
	 * @return
	 */
	public List<V3D> getCubeByDistance(int dis) {
		List<V3D> b = new ArrayList<V3D>(dis * dis * dis);
		for(int i = - dis; i <= dis; i++) {
			for(int j = - dis; j <= dis; j++) {
				for(int k = - dis; k <= dis; k++) {
					b.add(new V3D(this.x + i, this.y + j, this.z + k));
				}
			}
		}
		return b;
	}
	
	/**
	 * 以此坐标为球心，指定球体半径， 返回球体内的整数坐标点
	 * @param dis
	 * @return
	 */
	public List<V3D> getPosByDistance(int dis) {
		if(dis == 0) {
			return null;
		}
		List<V3D> b = new ArrayList<V3D>(dis * dis * dis);
		V3D temp;
		for(int i = - dis; i <= dis; i++) {
			for(int j = - dis; j <= dis; j++) {
				for(int k = - dis; k <= dis; k++) {
					temp = new V3D(this.x + i, this.y + j, this.z + k);
					if(this.distance(temp) <= dis) {
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
	public List<V3D> getRadiantSquare(V3D origin, int distance) {
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
	 * 获得当前方块的上下及四周方块的坐标
	 * @return V3D[6]
	 */
	public V3D[] getUDLRFB() {
		V3D[] udlrfb = new V3D[6];
		udlrfb[0] = new V3D(this.x, this.y + 1, this.z);
		udlrfb[1] = new V3D(this.x, this.y - 1, this.z);
		udlrfb[2] = new V3D(this.x - 1, this.y, this.z);
		udlrfb[3] = new V3D(this.x + 1, this.y, this.z);
		udlrfb[4] = new V3D(this.x, this.y, this.z + 1);
		udlrfb[5] = new V3D(this.x, this.y, this.z - 1);
		return udlrfb;
	}
	
	/**
	 * 在水平方向上由内向外呈十字形辐射， 每侧长度为distance， 不含中心点
	 * @param distance 十字每侧的长度
	 * @return
	 */
	public static List<V3D> getCrossDelta(int distance) {
		V3D[] cross = APIPlayer.getFootWithOffset().getCrossClockWise();
		List<V3D> delta = new ArrayList<V3D>(distance * 4);
		for(int i = 1; i <= distance; i++) {
			for(int t = 0; t < 4; t++) {
				delta.add(new V3D(cross[t].x * i, cross[t].y, cross[t].z * i));
			}
		}
		return delta;
	}
	
	/**
	 * 以此坐标为原点向x、z正方向、负方向延伸距离为distance， 不含中心点, 所形成的十字高度为2 * distance + 1
	 * @param distance 辐射长度
	 */
	public List<V3D> getCrossCube(int distance) {
		List<V3D> b = new ArrayList<V3D>(distance * distance * 8 + distance * 6);
		for(int i = - distance; i <= distance; i++) {
			for(int j = - distance; j <= distance; j++) {
				for(int k = - distance; k <= distance; k++) {
					if(i * j * k == 0 & !(i == 0 & j == 0 & k ==0) & !(j == 0 & i * k != 0)) {
						b.add(new V3D(this.x + i, this.y + j, this.z + k));
					}
				}
			}
		}
		return b;
	}
	
	
	
	/**
	 * 获取玩家当前视角状态下， 东西南北方向的顺时针排序
	 * @return V3D[4]， 起点为当前方向
	 */
	public V3D[] getCrossClockWise() {
		V3D[] cross = new V3D[4];
		int index = 0;
		int startIndex = APIPlayer.getHeading();
		for(int i = 0; i < 3; i++) {
			index = startIndex + i > 3 ? startIndex + i - 3 : i;
			cross[i] = this.add(wise[index]);
		}
		return cross;
	}
	
	/**
	 * 获取此V3D位置方块的ID
	 * @return
	 */
	public int getId() {
		return GameData.getBlockRegistry().getIDForObject(Minecraft.getMinecraft().world.getBlockState(new BlockPos(x, y, z)).getBlock());
	}
	
	/**
	 * 获取方块邻近面的中心点
	 * @return
	 */
	public Vec3d getCenterOfNearestSide() {
		V3D diff = this.minus(APIPlayer.getFootWithOffset());
		Vec3d sideDiff = new Vec3d(this.x + 0.0D, this.y + 0.0D, this.z + 0.0D);
		if(diff.x > 0) {
			sideDiff = new Vec3d(this.x + 0.0D, this.y + 0.5D, this.z + 0.5D);
		}else if(diff.x < 0) {
			sideDiff = new Vec3d(this.x + 1.0D, this.y + 0.5D, this.z + 0.5D);
		}else if(diff.z > 0) {
			sideDiff = new Vec3d(this.x + 0.5D, this.y + 0.5D, this.z + 0.0D);
		}else if(diff.z < 0) {
			sideDiff = new Vec3d(this.x + 0.5D, this.y + 0.5D, this.z + 1.0D);
		}else if(diff.y > 0) {
			sideDiff = new Vec3d(this.x + 0.5D, this.y + 0.0D, this.z + 0.5D);
		}else if(diff.y < 0) {
			sideDiff = new Vec3d(this.x + 0.5D, this.y + 1.0D, this.z + 0.5D);
		}
		return sideDiff;
	}
	
	/**
	 * 获取以玩家朝向为基础的坐标差数组
	 * @return
	 */
	public V3D[] getDirectionDiff() {
		V3D[] b = new V3D[4];
		
		return b;
	}
	
	/**
	 * 获得此坐标水平面周围指定距离内的所有坐标，方向优先, 先正方向后斜方向，先前方向， 由近至远
	 * @param dis
	 * @return
	 */
	public V3D[] getNeighborByDistance(int dis) {
		V3D[] b = new V3D[(dis + 1) * dis * 4];
		int index = 0;
		IDirection direction = new IDirection();
		V3D[] wads = direction.getWADS();
		for(int i = 0; i < wads.length; i++) {
			for(int j = 1; j <= dis; j++) {
				b[index] = this.add(wads[i].multiply(j));
				index++;
			}
		}
		V3D[] incline = direction.getIncline();
		for(int i = 0; i < incline.length; i++) {
			for(int j = 1; j <= dis; j++) {
				for(int k = 1; k < j; k++) {
					b[index] = this.add(incline[i].multiplyXSetZ(k, j));
					index++;
					b[index] = this.add(incline[i].multiplyZSetX(k, j));
					index++;
				}
				b[index] = this.add(incline[i].multiply(j));
				index++;
			}
		}
		return b;
	}
	
	public V3D multiplyXSetZ(int i, int setz) {
		return new V3D(this.x * i, this.y, this.z / Math.abs(this.z) * setz);
	}
	
	public V3D multiplyZSetX(int i, int setx) {
		return new V3D(this.x / Math.abs(this.x) * setx, this.y, this.z * i);
	}
	
	public BlockPos toBlockPos() {
		return new BlockPos(this.x, this.y, this.z);
	}
	
	@Override
	public String toString() {
		return this.x + ", " + this.y + ", " + this.z + ";  ";
	}
}
