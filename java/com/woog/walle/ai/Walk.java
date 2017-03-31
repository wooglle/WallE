package com.woog.walle.ai;

import java.util.Timer;
import java.util.TimerTask;

import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.WallE;

import net.minecraft.client.Minecraft;

public class Walk
{
	private static Minecraft mc = Minecraft.getMinecraft();
	private static int keyForward = mc.gameSettings.keyBindForward.getKeyCode();
	private static boolean isFinish = false;
	public static ActionUtil util;
	
	public Walk() {
		util = new ActionUtil();
	}
	
	public void run() {
//		System.out.println("get(0)=" + WallE.way.get(0));
//		new FaceTo(WallE.way.get(0), 2);
		Thread t = new stepThread();
		t.start();
	}
	
	public void forward() {
		util.setMovement(1, false, false);
	}
	
	public void forwardstop() {
		util.setMovement(0, false, false);
//			mc.gameSettings.keyBindForward.setKeyBindState(keyForward, false);
	}
	
	class stepThread extends Thread {
		public void run() {
			int n = WallE.way.size();
			V3D current = null;
			KeepOnWatch watch = new KeepOnWatch(WallE.way.get(0), 0);
			for(int i = 0; i < n; i++) {
//				System.out.println("~~~~~ " + WallE.way.size() + " " + WallE.way.get(0) + "   " + new V3D(APIPlayer.getFoot()));
//				new FaceTo(WallE.way.get(0), 2);
				watch.SetPos(WallE.way.get(0));
				forward();
				
				while(true) {
					if(mc.player.getEntityBoundingBox().isVecInside(WallE.way.get(0).toVec3())) {
						System.out.println("bounding====================");
					}
					if(WallE.way.get(0).isEqual(new V3D(APIPlayer.getFoot()))) {
//						mc.gameSettings.keyBindForward.setKeyBindState(keyForward, false);
						forwardstop();
						WallE.way.remove(0);
//						delay(500);
						break;
					}
				}
			}
			watch.isKeep = false;
			util.allDefault();
		}
		
		private void delay(int x) {
			try {
				Thread.sleep(x);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}