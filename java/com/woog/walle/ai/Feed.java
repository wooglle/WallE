package com.woog.walle.ai;

import com.woog.walle.APIInventory;
import com.woog.walle.APIPlayer;

public class Feed extends ActionBase {
	
	@Override
	public String getActName() {
		return "Automatic Feeding";
	}
//				GameData.getItemRegistry().getDefaultValue();
//				APIInventory.myInventory[i].getItem().15
	
	@Override
	protected boolean condition() {
//		System.out.println("【Con0】 " + (mc.theWorld != null)  );
//		System.out.println("【Con0】 " + doing  );
//		System.out.println("【Con1】 " + APIPlayer.getFoodLevel()   );
//		System.out.println("【Con2】 " +  APIPlayer  );
//		System.out.println("【Con2】 " +  APIPlayer.getHealth()  );
//		System.out.println("【Con2】 " +  mc.thePlayer.getMaxHealth() );
//		System.out.println("【Con2】 " +  (APIPlayer.getHealth() < mc.thePlayer.getMaxHealth()) );
//		System.out.println("【Con3】 " + (APIPlayer.getFoodLevel() < 20 | APIPlayer.getHealth() < mc.thePlayer.getMaxHealth()) );
//		System.out.println("【Con4】 " + mc.theWorld != null && doing & (APIPlayer.getFoodLevel() < 20 | APIPlayer.getHealth() < mc.thePlayer.getMaxHealth()) );
		return (mc.world != null) && doing && (!pause) && (APIPlayer.getFoodLevel() < 20 | APIPlayer.getHealth() < mc.player.getMaxHealth());
	}
	
	@Override
	protected void action() {
//		while(APIPlayer.getHealth() == mc.thePlayer.getMaxHealth()) {}
//		delay(1000);
//		APIInventory.printInventory();
//		getFood();
//		System.out.println("【 Con 】" + condition());
		int prev = mc.player.inventory.currentItem;
		while(condition()) {
			int hotbar = APIInventory.getFoodMax();
//			System.out.println("FEED1111111111111111  " + hotbar);
			delay(200);
			while(condition() && hotbar != -1) {
				mc.player.inventory.currentItem = hotbar;
//				System.out.println("FEED22222222222222222");
				mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
				delay(200);
				if(APIInventory.getHeldItem() == null) {
					break;
				}
//				System.out.println("FEED22222" + (mc.theWorld != null && doing && APIPlayer.getFoodLevel() > 18 && APIPlayer.getHealth() != mc.thePlayer.getMaxHealth() & APIInventory.getHeldItem() != null) );
				while(mc.world != null && doing && APIPlayer.getFoodLevel() > 18 && APIPlayer.getHealth() != mc.player.getMaxHealth() & APIInventory.getHeldItem() != null) {
					mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
					delay(1000);
				}
			}
			if(hotbar == -1) {
				break;
			}
		}
		mc.player.inventory.currentItem = prev;
		mc.gameSettings.keyBindUseItem.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
//		delay(500);
//		keyPress(hotbar);
//		mc.gameSettings.keyBindsHotbar[this.food].setKeyBindState(mc.gameSettings.keyBindsHotbar[this.food].getKeyCode(), true);
	}
}
