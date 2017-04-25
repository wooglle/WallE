package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.WallE;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInputFromOptions;

public class ActionUtil {
	Minecraft mc = Minecraft.getMinecraft();
	private int keyForward = mc.gameSettings.keyBindForward.getKeyCode();
	private MovementEntry movement = new MovementEntry(0, false, false);
	private MouseEntry mouse = new MouseEntry();
	public boolean isShield = true;
	private boolean shielding = false;
	public V3D gazeBlockV3D = null;
	public boolean isKeepGaze = false;
	
	ActionUtil() {
		if(isShield) {
			if(!shielding) {
				mc.player.movementInput = this.movement;
				mc.mouseHelper = this.mouse;
				shielding = true;
			}
		}else{
			if(shielding) {
				allDefault();
				shielding = false;
			}
		}
//		p_i45001_3_ = "key.categories.gameplay";
		// TODO Auto-generated constructor stub
//		mc.gameSettings.keyBindAttack = new KeyBinding("key.Forward", 37, "key.categories.movement");
//		mc.gameSettings.keyBindUseItem = new KeyBinding("key.UseItem", 39, "key.categories.movement");
	}
	
	public void setMovement(float forward, boolean jump, boolean sneak) {
		if(isShield) {
			movement.set(forward, jump, sneak);
		}else{
			mc.gameSettings.keyBindForward.setKeyBindState(keyForward, true);
		}
	}
	
	public void moveLeft() {
		mc.gameSettings.keyBindLeft.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true);
	}
	
	public void moveLeftStop() {
		mc.gameSettings.keyBindLeft.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
	}
	
	public void moveRight() {
		mc.gameSettings.keyBindRight.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true);
	}
	
	public void moveRightStop() {
		mc.gameSettings.keyBindRight.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
	}
	
	public void gazeOnBlock(V3D gazeAt) {
		this.gazeBlockV3D = gazeAt;
		this.isKeepGaze = true;
		new GazeAtBlock().start();
	}
	
	public void allDefault() {
		if(WallE.acts.isEmpty()) {
			movementDefault();
			mouseDefault();
		}
	}
	
	public void movementDefault() {
		mc.player.movementInput = new MovementInputFromOptions(mc.gameSettings);
	}
	
	public void mouseDefault() {
		mc.mouseHelper = new MouseHelper();
	}
	
	public void leftDown() {
		mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
	}
	
	public void leftUp() {
		mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
	}
	
	public void rightDown() {
		mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
	}
	
	public void rightUp() {
		mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
	}
	
	public void leftClick() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
			}
		}).start();
	}
	
	private class GazeAtBlock extends Thread {
		private boolean isNear() {
			if(APIPlayer.getEye().distanceTo(gazeBlockV3D.toVec3d()) < 0.3D) {}
			return false;
		}
		
		@Override
		public void run() {
			while(isKeepGaze && !this.isNear()) {
				try {
					Thread.sleep(50);
					new FaceTo(gazeBlockV3D, 2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			gazeBlockV3D = null;
			isKeepGaze = false;
		}
	}
	
	private class MouseEntry extends MouseHelper {
		public void mouseXYChange() {
		}
		
		public void setDelta(int x, int y) {
			this.deltaX = x;
			this.deltaY = y;
		}
	}
}
