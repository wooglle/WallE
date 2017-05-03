package com.woog.walle;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventTestClass {
	@SubscribeEvent
	public void TestEvent(BlockEvent e) {
		System.out.println("ET  1   " + e.toString());
//		System.out.println("RENDER  2   " + e.getContext().getDebugInfoRenders());
	}
}
