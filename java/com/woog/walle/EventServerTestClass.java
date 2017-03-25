package com.woog.walle;

import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventServerTestClass {
	@SubscribeEvent
	public void Packet1(PlayerContainerEvent e) {
		System.out.println("[Player]     " + e.toString());
	}
	
//	@SubscribeEvent
//	public void Packet2(AchievementEvent e) {
//		System.out.println("[Item]     " + e.toString());
//	}
}
