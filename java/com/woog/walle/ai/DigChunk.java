package com.woog.walle.ai;

import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.additional.IChunk;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DigChunk extends ActionBase {
	public static V3D blockDigging = null;
	
	@Override
	public String[] getHolds() {
		String[] holds = {"item.pickaxeDiamond", "item.pickaxeIron"};
//		String[] holds = {"item.pickaxeDiamond", "item.pickaxeIron"};
		return holds;
	}
	
	@Override
	public String getActName() {
		return "Dig Blocks Nearby";
	}
	
	@Override
	public boolean showMsg() {
		return false;
	}
	
	@Override
	public boolean condition() {
		return (mc.world != null) && doing && (!pause);
	}
	
	private void digCrossChunk(V3D t) {
		Block block = mc.world.getBlockState(new BlockPos(t.x, t.y, t.z)).getBlock();
		if(t.getId() != 0) {
			new FaceTo(t, 1);
			this.blockDigging = t;
			delay(50);
			mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
			int n = 0;
			while(t.getId() != 0 & n < 500) {
				++n;
				delay(10);
			}
			mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
			this.holdStuff();
//			delay(1000);
//			if(APIInventory.getHeldItemDamage() < WallE.minItemDamage + 5) {
//			notifyBlockUpdate
//			【续命】虾米Mickey 2016/5/27 10:01:19
//			嫌麻烦直接setBlockState
//			setBlockState会调用markAndNotifyBlock
//
//			}
		}
	}
	
	private void turn() {
		V3D[] news = new IChunk().toClockWise();
		new FaceTo(news[1], 2);
	}
	
	@Override
	public void action() {
		IChunk chunk = new IChunk(APIPlayer.getFoot2());
		V3D[] nearby = chunk.getCorssHeight(3);
		
		if(!chunk.isTurn) {
			for(int i = 0; i < nearby.length; i++) {
				Block block = mc.world.getBlockState(new BlockPos(nearby[i].x, nearby[i].y, nearby[i].z)).getBlock();
				digCrossChunk(nearby[i]);
//				delay(5);
			}
		}else{
			System.out.println("nooooooooooo  nearby!!!!!!!");
			turn();
			new WalkOneStep();
		}
	}
}
