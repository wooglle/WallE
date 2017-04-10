package com.woog.walle;

import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventItemClass {
	@SubscribeEvent
	public void TreeGrow(TextureStitchEvent e) {
		System.out.println("[IIIIII]     " + e);
	}
	
	@SubscribeEvent
	public void Packet2(RenderTooltipEvent e) {
		System.out.println("[Item]     " + e.toString());
	}
}
