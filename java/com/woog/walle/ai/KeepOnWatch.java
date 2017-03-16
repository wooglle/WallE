package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;

import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.FMLClientHandler;

public class KeepOnWatch {
	public static boolean isKeep = false;
	public static V3D point = null;
	private int code = 0;
	
	/**
	 * 使史蒂夫在移动时视线始终指向目标
	 * @param point 目标的坐标
	 * @param code 0：pitch=0， 视线保持水平；1：pitch指向目标临近面中心；2：pitch指向目标体中心；
	 */
	public KeepOnWatch(V3D point, int code) {
		this.point = point;
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
	
	private Vec3d getPoint() {
		if(this.code == 1) {
			return this.point.getCenterOfSide();
		}else if(this.code == 2) {
			return this.point.getCenter();
		}else {
			Vec3d p = this.point.getCenter();
			return new Vec3d(p.xCoord, APIPlayer.posY(), p.zCoord);
		}
	}
	
	private class Keep extends Thread{
		public void run() {
			while(isKeep) {
				setAngle();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			point = null;
		}
	}
}
