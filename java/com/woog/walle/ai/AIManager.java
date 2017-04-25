package com.woog.walle.ai;

import com.woog.walle.APIInventory;
import com.woog.walle.APIPlayer;
import com.woog.walle.EventChatClass;
import com.woog.walle.V3D;
import com.woog.walle.WallE;
import com.woog.walle.additional.IChunk;
import com.woog.walle.additional.IDebug;

import net.minecraftforge.fml.client.FMLClientHandler;

public class AIManager {
	public static V3D[] corners = new V3D[12];
	public static V3D[] footPoints = new V3D[8];
	private static int cornerIndex = 0;
	private static int footIndex = 0;
	private static int prevHandInDamage = 0;
	public static boolean doing = false;
	private static boolean isLoop = false;
	private static boolean isGhostWall = false;
	public static V3D blockLastDig = null;
	public static V3D blockLastFoot = null;
	
	public AIManager() {
		footPoints[0] = new V3D(0, 1024, 0);
		this.isLoop = false;
		this.doing = true;
		new Manager().start();
	}
	
	public void setDoing(boolean isDoing) {
		this.doing = isDoing;
	}
	
	public static void addCorner(V3D foot) {
		System.out.println("【AI】  " + cornerIndex);
		AIManager.blockLastDig = DigChunk.blockDigging;
		corners[cornerIndex] = foot;
		cornerIndex = cornerIndex + 2 > corners.length ? 0 : cornerIndex + 1;
	}
	
	private static void addFootPoint() {
		footPoints[footIndex] = APIPlayer.getFootWithOffset();
		footIndex = footIndex + 2 > footPoints.length ? 0 : footIndex + 1;
	}
	
	public static void setIsLoop() {
		if(isOK()) {
			isLoop = false;
			return;
		}
		if(DigChunk.blockDigging != null && AIManager.blockLastDig != null && !DigChunk.blockDigging.equals(AIManager.blockLastDig)) {
			isLoop = false;
			return;
		}
		for(int i = 0; i < 7; i++) {
			if(corners[i + 3] != null && corners[0].equals(corners[i + 2]) && corners[1].equals(corners[i + 3])) {
				isGhostWall = false;
				isLoop = true;
				return;
			}
		}
	}
	
	private static void setIsGhostWall() {
//		if(APIInventory.getHeldItemDamage() != prevHandInDamage) {
//			prevHandInDamage = APIInventory.getHeldItemDamage();
//			return false;
//		}
//		return true;
		int n = 0;
		for(int i = 1; i < footPoints.length; i++) {
			if(footPoints[0].equals(footPoints[i])) {
				n++;
			}
		}
//		System.out.println("   " + n);
		if(n > 2) {
			isLoop = true;
			isGhostWall = true;
		}
		isGhostWall = n < 3 ? false : true;
	}
	
	public static boolean isOK() {
		if(APIInventory.getHeldItemDamage() != prevHandInDamage) {
			prevHandInDamage = APIInventory.getHeldItemDamage();
			return true;
		}
		return false;
	}
	
	private class Manager extends Thread {
		public void run() {
			System.out.println("AI 已启动~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//			FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】." + "AI 已启动 。。。"));
			IDebug.PrintYellow("AI 已启动 。。。");
			clickRight();
			while(doing) {
				if(EventChatClass.skillTime < 1) {
					clickRight();
				}
				while(!WallE.acts.isEmpty()) {
//					System.out.println("AI 111111~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					delay(10);
				}
				new DigChunk();
				while(!WallE.acts.isEmpty()) {
//					System.out.println("AI 222222~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					delay(10);
				}
				IChunk chunk = new IChunk();
//				System.out.println("【AI】  " + cornerIndex);
//				System.out.println("【AI】   "+ cornerIndex +"  " + isLoop);
				AIManager.addFootPoint();
				AIManager.setIsGhostWall();
				AIManager.setIsLoop();
//				System.out.println("      " + isGhostWall + "   " + isLoop);
				if((!isLoop) & (!isGhostWall)) {
					new FaceTo(chunk.getFRLB(), 2);
					new WalkOneStep();
				}else{
					if(Math.random() > 0.5D) {
						V3D foot0 = APIPlayer.getFootWithOffset();
//						new FaceTo(chunk.getRandomFRL(), 2);
						new FaceTo(chunk.getRandomFRLB(), 2);
						new WalkOneStep();
//						setCorners2Default();
						if(!foot0.equals(APIPlayer.getFootWithOffset())) {
							isLoop = false;
						}
					}
				}
			}
//			FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e§o【Wall-E】." + "AI 已结束 。。。"));
			IDebug.PrintRed("AI 已结束 。。。");
//			System.out.println("AI 已结束~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
	}
	
	private void setCorners2Default() {
		V3D def = new V3D(0, 0, 0);
		for(int i = 0; i < this.corners.length; i++) {
			this.corners[i] = def;
		}
	}
	
	private void clickRight() {
		FMLClientHandler.instance().getClient().gameSettings.keyBindUseItem.setKeyBindState(FMLClientHandler.instance().getClient().gameSettings.keyBindUseItem.getKeyCode(), true);
		delay(50);
		FMLClientHandler.instance().getClient().gameSettings.keyBindUseItem.setKeyBindState(FMLClientHandler.instance().getClient().gameSettings.keyBindUseItem.getKeyCode(), false);
	}
	
	private void delay(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
