package com.woog.walle.ai;

import com.woog.walle.APIInventory;

import net.minecraft.client.gui.inventory.GuiInventory;

public class HeldReload {
	private int toolMinDamage = 3;
	
	public boolean isBreakdown() {
		return false;
	}
	
	public int leftDamage() {
		if(APIInventory.getHeldItem() == null) {
			return 0;
		}else{
			return APIInventory.getHeldItemDamage();
		}
	}
}
