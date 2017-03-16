package com.woog.walle.ai;

public class Fishing extends ActionBase{
	float buff;
	float max;
	
	@Override
	public String getActName() {
		return "Automatic Fishing";
	}
	
	@Override
	public String[] getHolds() {
		String[] holds = {"item.fishingRod"};
		return holds;
	}
	
	@Override
	public boolean condition() {
		return (mc.world != null) & doing & (!pause);
//		return (APIInventory.getHeldID() == 346) & doing & (!pause);
	}
	@Override
	public void action() {
		while(condition()) {
			if(mc.player.fishEntity != null) {
//				if(Math.abs(mc.thePlayer.fishEntity.motionY) > 0.032D ) {
//					System.out.println("[FISH]            >0.032D" );
//					this.rightClick();
//					delay(400);
//					this.rightClick();
//					hooking();
//				}
//				System.out.println("[FISH]       < fishEntity" );
				
//				this.holdStuff();
				delay(100);
			}else{
//				System.out.println("[FISH] 111    >null    fishEntity" );
				this.holdStuff();
				this.rightClick();
				hooking();
//				System.out.println("[FISH]            >null    fishEntity" );
			}
		}
		if(mc.player != null && mc.player.fishEntity != null) {
			this.rightClick();
		}
	}
	
	private void rightClick(){
		mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
		delay(50);
		mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
	}
	
	private void hooking() {
		max = -1.0F;
		buff = 0.0F;
		delay(1000);
		while(doing && (!pause) && mc.player.fishEntity != null && mc.player.fishEntity.getDistanceToEntity(mc.player) > max) {
			max = mc.player.fishEntity.getDistanceToEntity(mc.player) > buff ? mc.player.fishEntity.getDistanceToEntity(mc.player) : buff;
//			System.out.println("[][]  1111   " + mc.thePlayer.fishEntity.getDistanceToEntity(mc.thePlayer));
			delay(300);
		}
		delay(500);
	}
}
