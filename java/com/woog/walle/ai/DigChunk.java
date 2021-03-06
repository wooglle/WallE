package com.woog.walle.ai;

import com.woog.walle.APIChunk;
import com.woog.walle.APIInventory;
import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.additional.IChunk;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DigChunk extends ActionBase {
	public static V3D blockDigging = null;
	private static V3D iniPos = null;
	
	@Override
	public String getToolsKeyword() {
		return "pickaxe";
	}
	
	@Override
	public String getActName() {
		return "Dig Chunk";
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
		if(this.iniPos.distance(APIPlayer.getFootWithOffset()) > 2) {
			return;
		}
		Block block = mc.world.getBlockState(new BlockPos(t.x, t.y, t.z)).getBlock();
		if(!APIChunk.isEmpty(t)) {
			new FaceTo(t, 1);
			this.blockDigging = t;
			delay(50);
			mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
			int n = 0;
			while(!APIChunk.isEmpty(t) & n < 200) {
				++n;
				delay(50);
			}
			mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
//			this.holdStuff();
//			delay(1000);
//			if(APIInventory.getHeldItemDamage() < WallE.minItemDamage + 5) {
//			notifyBlockUpdate
//			【续命】虾米Mickey 2016/5/27 10:01:19
//			嫌麻烦直接setBlockState
//			setBlockState会调用markAndNotifyBlock
//			}
		}
	}
	
	private void turn() {
		V3D[] news = new IChunk().toClockWise();
		new FaceTo(news[1], 2);
	}
	
	@Override
	public void action() {
		this.iniPos = APIPlayer.getFootWithOffset();
		IChunk chunk = new IChunk(iniPos);
		V3D[] nearby = chunk.getCorssHeight(4);
		boolean hasDigged = false;
		if(!chunk.isTurn) {
			for(int i = 0; i < nearby.length; i++) {
				Block block = mc.world.getBlockState(new BlockPos(nearby[i].x, nearby[i].y, nearby[i].z)).getBlock();
//				if(!APIInventory.getHeldName().matches("^.*" + this.getToolsKeyword() + ".*")) {
//				}
				this.holdStuff();
				ItemStack tool = APIInventory.getHeldItem();
				if(APIInventory.canItemBrokeBlock(tool.getItem(), nearby[i])) {
					hasDigged = true;
					digCrossChunk(nearby[i]);
				}
				delay(10);
			}
			if(!hasDigged) {
				return;
			}
		}else{
			System.out.println("nooooooooooo  nearby!!!!!!!");
			turn();
			new WalkOneStep();
		}
	}
}