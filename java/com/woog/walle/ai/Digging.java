package com.woog.walle.ai;

import java.awt.AWTException;
import java.awt.Robot;

import com.woog.walle.APIInventory;
import com.woog.walle.WallE;

import net.minecraft.client.Minecraft;

public class Digging extends ActionBase{
	@Override
	public String getActName() {
		return "刷石机刷石";
	}
	
	@Override
	public String[] getHolds() {
		String[] holds = { "item.pickaxeDiamond", "item.pickaxeIron", "item.pickaxeStone"};
//		String[] holds = {"item.pickaxeDiamond", "item.pickaxeIron"};
		return holds;
	}
	
	@Override
	public boolean condition() {
//		int id = APIInventory.getHeldID();
//		return (id == 257 | id == 270 | id == 274 | id == 278 | id == 285) & doing & (!pause);
		return doing & (!pause);
	}
	
	@Override
	public void action() {
		mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
		while(condition()) {
			delay(200);
			if(!mc.gameSettings.keyBindAttack.isKeyDown()) {
				while(mc.currentScreen != null) {
					delay(100);
				}
				mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
			}
			this.holdStuff();
		}
		mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
	}
}
