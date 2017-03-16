package com.woog.walle.additional;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.IInventory;

public class IGuiScreen {
	public IInventory upperChestInventory = getUpper();
	public IInventory lowerChestInventory = getLower();
	private IInventory getLower() {
		// TODO Auto-generated method stub
		return (IInventory) IReflect.getField(this, "lowerChestInventory");
	}
	private IInventory getUpper() {
		// TODO Auto-generated method stub
		return (IInventory) IReflect.getField(this, "upperChestInventory");
	}
}
