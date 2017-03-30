package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public class FaceTo {
	private Minecraft mc = Minecraft.getMinecraft();
	private String info = "info: ";
	private YawPitch yp;
	/**
	 * 调整视线落点至某一方块(V3D坐标)
	 * @param target 目标方块的V3D坐标
	 * @param code 视线落点的位置：1, 最近面的中心; 2, 正方体的中心点
	 */
	public FaceTo(V3D target, int code) {
		if(code == 1) {
			yp = getAngle(new Vec3d(target.x, target.y, target.z));
		}else if(code == 2) {
			yp = getCenterAngle(new Vec3d(target.x, target.y, target.z));
		}
		float yy = APIPlayer.yaw();
		float pp = APIPlayer.pitch();
		mc.player.turn((float)(yp.yaw / 0.15D), (float)(yp.pitch / 0.15D));
//		System.out.println("	角度" + (float)yp.yaw + "	  转动后" + APIPlayer.yaw());
//		System.out.println("▇转动前" + yy + "	角度" + (float)yp.yaw + "	  转动后" + APIPlayer.yaw());
	}
	
	private YawPitch getCenterAngle(Vec3d target) {
		V3D tar = new V3D(target);
		Vec3d center = tar.getCenter();
		double yaw = 0, pitch = 0;
		double x0 = APIPlayer.getEye().xCoord;
		double y0 = APIPlayer.getEye().yCoord;
		double z0 = APIPlayer.getEye().zCoord;
		double x1 = center.xCoord;
		double y1 = center.yCoord;
		double z1 = center.zCoord;
//		System.out.println("   " + APIPlayer.getEye());
		//   eye - target
		double x2 = -x0 +x1;
		double y2 = -y0 +y1;
		double z2 = -z0 +z1;
		
		double y = 0.0D,p = 0.0D;
//		if(z2 == 0.0D) {
//			if(x2 == 0.0D){
//				y = 0.0D;
//			}else if(x2 < 0.0D) {
//				y = 90.0D;
//			}else{
//				y = -90.0D;
//			}
//		}else if(x2 == 0.0D){
//			if(z2 < 0.0D) {
//				y = 180.0D;
//			}else{
//				y = 0.0D;
//			}
//		}else{
//			double A = x2 / Math.abs(x2);
//			double B = z2 / Math.abs(z2);
//			y = Math.toDegrees(- A * B * Math.atan(Math.abs(x2/ z2))) - 90 * A * (1 - B);
//			y = Math.toDegrees(- A * B * Math.atan(Math.abs(x2 / z2)) - 0.5 * Math.PI * A * (1 - B));
//		}
//		【膜蛤】叁只弱z和壹只渣z的701次故事
//		我一个Math.atan2(-x, z)就搞定了
		y = Math.toDegrees(Math.atan2(-x2, z2));
		double deltax = 0, deltaz = 0;
		double x3 = x2 + deltax;
		double z3 = z2 + deltaz;
		if(y2 > 0.0D) {
			this.info += "j";
			p = -Math.atan(Math.abs(y2) / Math.sqrt(x3 * x3 + z3 * z3));
		}else if(y2 < 0.0D) {
			this.info += "k";
			p = Math.atan(Math.abs(y2) / Math.sqrt(x3 * x3 + z3 * z3));
		}else{
			this.info += "l";
			p = 0.0D;
		}
		this.info += "; " + y2 + " = " + y0 + " - " + y1;
//		System.out.println("▇转动前" + APIPlayer.yaw2());
		yaw = y - (double)APIPlayer.yaw2();
		pitch = (double)(APIPlayer.pitch() - (float)Math.toDegrees(p));		// (double)Math.PI * 180.0D;
		while(yaw > 360.0D) {
			yaw = yaw - 360.0D;
		}
		while(yaw < -360.0D) {
			yaw = yaw + 360.0D;
		}
		return new YawPitch(yaw, pitch);
	}
	
	/**
	 * 计算目标点相对于palyer坐标的角度
	 * A = x / |x|, B = z / |z|
	 * yaw = -A * B * acrtan|x / z| - 180 * A *(1 - B) / 2
	 */
	
	private YawPitch getAngle(Vec3d target) {
		V3D tar = new V3D(target);
		Vec3d side = tar.getCenterOfNearestSide();
		double yaw = 0, pitch = 0;
		Vec3d look = mc.player.getLookVec();
		double x0 = APIPlayer.getEye().xCoord;
		double y0 = APIPlayer.getEye().yCoord;
		double z0 = APIPlayer.getEye().zCoord;
		double x1 = side.xCoord;
		double y1 = side.yCoord;
		double z1 = side.zCoord;
		//   eye - target
		double x2 = -x0 +x1;
		double y2 = -y0 +y1;
		double z2 = -z0 +z1;
		
		double y = 0.0D,p = 0.0D;
		if(z2 == 0.0D) {
			if(x2 == 0.0D){
				y = 0.0D;
			}else if(x2 < 0.0D) {
				y = 90.0D;
			}else{
				y = -90.0D;
			}
		}else if(x2 == 0.0D){
			if(z2 < 0.0D) {
				y = 180.0D;
			}else{
				y = 0.0D;
			}
		}else{
			double A = x2 / Math.abs(x2);
			double B = z2 / Math.abs(z2);
//			System.out.println("   【p】" + y2 + " ; " + Math.sqrt(x2 * x2 + z2 * z2));
			y = Math.toDegrees(- A * B * Math.atan(Math.abs(x2 / z2)) - 0.5 * Math.PI * A * (1 - B));
		}
//		System.out.println("   【1】" + y);
		double deltax = 0, deltaz = 0;
		
//		deltax = 0.5D;
//		deltaz = 0.5D;
		double x3 = x2 + deltax;
		double z3 = z2 + deltaz;
		if(y2 > 0.0D) {
			this.info += "j";
			p = -Math.atan(Math.abs(y2) / Math.sqrt(x3 * x3 + z3 * z3));
		}else if(y2 < 0.0D) {
			this.info += "k";
			p = Math.atan(Math.abs(y2) / Math.sqrt(x3 * x3 + z3 * z3));
		}else{
			this.info += "l";
			p = 0.0D;
		}
		this.info += "; " + y2 + " = " + y0 + " - " + y1;
		
//		System.out.println("   【1】" + y + " & " +p);
//		System.out.println("   【2】" + APIPlayer.yaw() + " & " +APIPlayer.pitch());
		
		yaw = y - (double)APIPlayer.yaw2();
		pitch = (double)(APIPlayer.pitch() - (float)Math.toDegrees(p));		// (double)Math.PI * 180.0D;
//		System.out.println("   【0】" + Math.toDegrees(APIPlayer.pitch()) + " & " + Math.toDegrees(p));
//		System.out.println("   【 】" + APIPlayer.pitch() + " & " + Math.toDegrees(p) + " & " + pitch);
//		System.out.println("【】" +x2 + ", " + y2 + ", " + APIPlayer.pitch() + " = " + p + " & " + pitch);
//		System.out.println("】" +x2 + ", " + y2 + ", " + APIPlayer.yaw2() +"  " + y + " = " + yaw + "& " + pitch);
//		System.out.println("   【3】" + yaw + " & " +pitch);
		
		while(yaw > 360.0D) {
			yaw = yaw - 360.0D;
		}
		while(yaw < -360.0D) {
			yaw = yaw + 360.0D;
		}
		
//		System.out.println(APIPlayer.getFoot().addVector(0.0D, 2.0D, 0.0D) + ",  " + mc.thePlayer.getLookVec());
//		System.out.println(target + ",  " + mc.thePlayer.rayTrace(5.0D, 1.0F));
		return new YawPitch(yaw, pitch);
	}
	
	private class YawPitch {
		public double yaw;
		public double pitch;
		public YawPitch(double yaw, double pitch) {
			this.yaw = yaw;
			this.pitch = pitch;
		}
	}
}
