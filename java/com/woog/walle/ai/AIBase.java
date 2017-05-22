package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;

public class AIBase {
	protected Minecraft mc = FMLClientHandler.instance().getClient();
	/**工作区的水平范围*/
	protected V3D[] areaXZ = null;
	/**工作区的高度范围*/
	protected int[] areaY = null;
	/**传送到工作区域的指令*/
	public String transferCommand = null;
	/**是否正在进行此任务*/
	public boolean doing;
	
	
	public AIBase() {
		
	}
	
	protected boolean isInArea() {
		V3D playerPos = APIPlayer.getFootWithOffset();
		if(playerPos.y > this.areaY[0] & playerPos.y < this.areaY[1]) {
			return false;
		}
		return false;
	}
}
