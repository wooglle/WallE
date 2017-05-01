package com.woog.walle;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.woog.walle.additional.IDirection;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.registry.GameData;

//@Immutable
public class V3D {
	public int x;
	public int y;
	public int z;
	
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
	
	public V3D(Vec3i vec3i) {
		this.x = vec3i.getX();
		this.y = vec3i.getY();
		this.z = vec3i.getZ();
	}
	
	public Vec3d toVec3d() {
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
	
	public V3D(EnumFacing face) {
		this.x = face.getFrontOffsetX();
		this.y = face.getFrontOffsetY();
		this.z = face.getFrontOffsetZ();
	}
	
	public double distance(V3D b) {
		return Math.sqrt(Math.pow(this.x - b.x, 2.0D) + Math.pow(this.y - b.y, 2.0D) + Math.pow(this.z - b.z, 2.0D));
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
	
	@Override
	public String toString() {
		return this.x + "," + this.y + "," + this.z + "   ";
	}
}
