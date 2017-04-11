package com.woog.walle;

import com.woog.walle.ai.CutTrees;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class EventTickClass {
	public static boolean doCheckTrees = false;
	public static boolean doCheckFram = false;
	
	@SubscribeEvent
	public void Tick(ClientTickEvent e) {
//		System.out.println("[IIIIII]     " + e);
		if(doCheckTrees){
			boolean findTree = false;
			if(WallE.TreeRootPos.isEmpty()) {
				V3D tree = APIChunk.getWood();
				if(tree != null) {
					WallE.TreeRootPos.add(tree);
					WallE.TreePos.add(tree);
					findTree = true;
				}
			}else{
				for(V3D root : WallE.TreeRootPos) {
					if(APIChunk.isLog(root)) {
						WallE.TreePos.add(root);
						findTree = true;
					}
				}
			}
			if(findTree & (!WallE.isCuttingTrees)) {
				System.out.println("+++++++++++++++++++++++++++++++++++++++");
				WallE.isCuttingTrees = true;
				new CutTrees();
			}
		}
	}
	
//	@SubscribeEvent
//	public void Packet2(RenderTooltipEvent e) {
//		System.out.println("[Item]     " + e.toString());
//	}
	
}
