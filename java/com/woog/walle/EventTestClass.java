package com.woog.walle;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventTestClass {
	@SubscribeEvent
	public void TestEvent(RenderWorldLastEvent e) {
		System.out.println("RENDER  1   " + e.getContext().getDebugInfoEntities());
		System.out.println("RENDER  2   " + e.getContext().getDebugInfoRenders());
	}
}
