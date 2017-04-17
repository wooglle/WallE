package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.FMLClientHandler;

public class KeepOnWatch {
	public static boolean isKeep = false;
	private static V3D pos = null;
	private int code = 0;
	
	/**
	 * 使史蒂夫在移动时视线始终指向目标
	 * @param pos 目标的坐标
	 * @param code 0：pitch=0， 视线保持水平；1：pitch指向目标临近面中心；2：pitch指向目标体中心；
	 */
	public KeepOnWatch(V3D pos, int code) {
		this.pos = pos;
		this.isKeep =true;
		this.code = code;
		new Keep().start();
	}
	
	private void setAngle() {
		double deltaX = this.getPoint().xCoord - APIPlayer.getEye().xCoord;
		double deltaZ = this.getPoint().zCoord - APIPlayer.getEye().zCoord;
		double yaw = Math.toDegrees(Math.atan2(-deltaX, deltaZ)) - APIPlayer.yaw2();
		double pitch = 0.0D;
		if(this.code > 0) {
			double deltaY = this.getPoint().yCoord - APIPlayer.getEye().yCoord;
			double delta = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));
			pitch = Math.toDegrees(Math.atan2(deltaY, delta)) - (double)APIPlayer.pitch();
		}
//		System.out.println("  " + deltaX + "  " + deltaZ + "  " + Math.toDegrees(Math.atan2(-deltaX, deltaZ)) + "  " + APIPlayer.yaw2());
		FMLClientHandler.instance().getClient().player.turn((float)(yaw / 0.15D), (float)(pitch / 0.15D));
//		System.out.println("  " + yaw + "    " + pitch);
	}
	
	private void faceTo(Vec3d point) {
		double yaw = 0, pitch = 0;
		double x0 = APIPlayer.getEye().xCoord;
		double y0 = APIPlayer.getEye().yCoord;
		double z0 = APIPlayer.getEye().zCoord;
		double x1 = point.xCoord;
		double y1 = point.yCoord;
		double z1 = point.zCoord;
		double x2 = -x0 + x1;
		double y2 = -y0 + y1;
		double z2 = -z0 + z1;
		
		double y = 0.0D,p = 0.0D;
		y = Math.toDegrees(Math.atan2(-x2, z2));
		double deltax = 0, deltaz = 0;
		double x3 = x2 + deltax;
		double z3 = z2 + deltaz;
		if(y2 > 0.0D) {
			p = -Math.atan(Math.abs(y2) / Math.sqrt(x3 * x3 + z3 * z3));
		}else if(y2 < 0.0D) {
			p = Math.atan(Math.abs(y2) / Math.sqrt(x3 * x3 + z3 * z3));
		}else{
			p = 0.0D;
		}
		yaw = y - (double)APIPlayer.yaw2();
		pitch = (double)(APIPlayer.pitch() - (float)Math.toDegrees(p));		// (double)Math.PI * 180.0D;
		while(yaw > 360.0D) {
			yaw = yaw - 360.0D;
		}
		while(yaw < -360.0D) {
			yaw = yaw + 360.0D;
		}
//		System.out.println("▇   " + APIPlayer.getEye() + "   " + yaw + "    " + pitch);
		FMLClientHandler.instance().getClient().player.turn((float)(yaw / 0.15D), (float)(pitch / 0.15D));
	}
	
	private Vec3d getPoint() {
		if(this.code == 1) {
			return this.pos.getCenterOfNearestSide();
		}else if(this.code == 2) {
			return this.pos.getCenter();
		}else {
			Vec3d p = this.pos.getCenter();
			return new Vec3d(p.xCoord, APIPlayer.posY() + 3.5D, p.zCoord);
		}
	}
	
	public void SetPos(V3D pos) {
		this.pos = pos;
	}
	
	private class Keep extends Thread{
		public void run() {
			this.setName("KeepOnWatch");
			while(isKeep) {
//				setAngle();
				try {
					faceTo(getPoint());
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			pos = null;
		}
	}
}
