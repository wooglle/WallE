package com.woog.walle.additional;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.client.FMLClientHandler;

public class IGuiChest{
	public IInventory upper = null;
	public IInventory lower = null;

	public IGuiChest() {
		if(!FMLClientHandler.instance().isGUIOpen(GuiChest.class)) {
			return;
		}
		this.upper = getUpper();
		this.lower = getLower();
	}
	
	public IInventory getUpper() {
		return upper;
	}
	public IInventory getLower() {
		return lower;
	}
}
