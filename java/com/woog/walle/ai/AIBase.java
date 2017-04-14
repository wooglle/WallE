package com.woog.walle.ai;

import com.woog.walle.V3D;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;

public class AIBase {
	private Minecraft mc = FMLClientHandler.instance().getClient();
	private V3D[] areaXZ = null;
	private int[] areaY = null;
	public String transferCommand = null;
	
	public AIBase() {
		
	}
}
