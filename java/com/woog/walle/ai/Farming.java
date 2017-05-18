package com.woog.walle.ai;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.RayTraceTarget;
import com.woog.walle.V3D;
import com.woog.walle.V3DHelper;
import com.woog.walle.WallE;
import com.woog.walle.additional.ICFarming;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class Farming extends ActionBase{
	private String toolsKeyword = "";
	
	@Override
	public String getActName() {
		return "Farming";
	}
	
	@Override
	public boolean condition() {
		return doing & !pause;
	}
	
	private Item seedOfCrops() {
		Item b = null;
		Block block = APIChunk.getBlockEyesOn();
		if(block instanceof BlockBush) {
			if(block == Blocks.WHEAT) {
				b = Items.WHEAT_SEEDS;
			}else if(block == Blocks.CARROTS) {
				b = Items.CARROT;
			}else if(block == Blocks.NETHER_WART) {
				b = Items.NETHER_WART;
			}else if(block == Blocks.POTATOES) {
				b = Items.POTATO;
			}
		}else if(block instanceof BlockFarmland) {
			b = Items.WHEAT_SEEDS;
		}else if(block instanceof BlockSoulSand) {
			b = Items.NETHER_WART;
		}
		return b;
	}
	
	private V3D getDirt(int n) {
		for(int z = 0; z < n + 1; z++) {
			for(int x = 0; x < n + 1; n++) {
				Block block = Minecraft.getMinecraft().world.getBlockState(new BlockPos(
						APIPlayer.getFootWithOffset().x + x, APIPlayer.getFootWithOffset().y, APIPlayer.getFootWithOffset().z + z)).getBlock();
				if(block == Blocks.FARMLAND) {
					return new V3D(APIPlayer.getFootWithOffset().x + x, APIPlayer.getFootWithOffset().y, APIPlayer.getFootWithOffset().z + z);
				}
			}
		}
		return null;
	}
	
	private void plantingAccordant() {
		boolean isStop = false;
		int AD = getAD(WallE.runtime.farmingFace, WallE.runtime.farmingFaceBlock);
		this.setAD(AD);
		V3D eyesonPrevious = null;
		V3D eyeson = null;
		Item seed;
		while(!this.canShift()) {
			eyeson = APIPlayer.getEyesOn();
			if(eyeson.equals(eyesonPrevious)) {
				delay(50);
				continue;
			}
			seed = this.seedOfCrops();
			if(seed != null) {
				this.toolsKeyword = seed.getUnlocalizedName();
				while(APIChunk.canHavest(eyeson)) {
					this.util.setMovement(0.0F, 0.0F, false, false);
					isStop = true;
					this.util.leftDown();
					delay(50);
					this.util.leftUp();
				}
				while(APIChunk.isEmpty(eyeson)) {
					delay(20);
					if(!isStop) {
						this.util.setMovement(0.0F, 0.0F, false, false);
					}
					this.holdStuff();
					this.util.rightDown();
					delay(50);
					this.util.rightUp();
					eyesonPrevious = eyeson;
				}
				if(isStop) {
					delay(50);
					this.setAD(AD);
					isStop = false;
				}
			}
			delay(20);
		}
	}
	
	private void setAD(int AD) {
		if(AD == 1) {
			System.out.println("       向左");
			this.util.setMovement(0.0F, 0.8F, false, false);
		}else if(AD == 2) {
			System.out.println("       向右...");
			this.util.setMovement(0.0F, -0.8F, false, false);
		}
	}
	
	/**
	 * face1相对于face2的位置
	 * @param face1
	 * @param face2
	 * @return 1 : 左; 2 ： 右
	 */
	private int getAD(EnumFacing face1, EnumFacing face2) {
		if(face2.rotateY().getOpposite().equals(face1)) {
			return 1;	//A， 向左
		}else if(face2.rotateY().equals(face1)) {
			return 2;	//D, 向右
		}
		System.out.println("Farming ERROR:" + face1 + " is not left or right side of " + face2);
		return 0;
	}
	
	private boolean canShift() {
		V3D pos = APIPlayer.getFootWithOffset().add(APIPlayer.getFacing());	///////
		V3D pos1 = pos.add(WallE.runtime.farmingFace);
		V3D pos2 = pos1.add(WallE.runtime.farmingFace);
		if(!isPlantBlock(pos1) && !isPlantBlock(pos2)) {
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isPlantBlock(V3D pos) {
		String name = APIChunk.getBlock(pos).getRegistryName().toString();
		return (name.equals("minecraft:farmland") | name.equals("minecraft:sand") | name.equals("minecraft:soul_sand"));
	}
	
	@Override
	public void action() {
		if(WallE.runtime.icFarming == null) {
			WallE.runtime.icFarming = new ICFarming();
			WallE.runtime.farmingNextPos = WallE.runtime.icFarming.firstStandPos;
			WallE.runtime.farmingNextBlock = WallE.runtime .icFarming.firstPlantPos;
			WallE.runtime.farmingFace = WallE.runtime.icFarming.facing;
			if(WallE.runtime.icFarming.isAccordant) {
				WallE.runtime.farmingFaceBlock = WallE.runtime.icFarming.facingShift;
			}else{
				WallE.runtime.farmingFaceBlock = EnumFacing.DOWN;
			}
		}
		if(!APIPlayer.getFootWithOffset().equals(WallE.runtime.farmingNextPos)) {
//			this.pause = true;
//			delay(200);
			new RayTraceTarget(WallE.runtime.farmingNextPos);
			new Walk2There();
			return;
		}else{
			delay(1000);
			System.out.println("   " + WallE.runtime.icFarming.firstPlantPos + "   " + WallE.runtime.icFarming.isAccordant);
			if(WallE.runtime.icFarming.isAccordant) {
				new FaceTo(WallE.runtime.farmingFaceBlock, 0.0F);
				delay(200);
				new FaceTo(WallE.runtime.farmingNextBlock, EnumFacing.UP, 2);
				delay(200);
				this.plantingAccordant();
			}
			WallE.runtime.icFarming = null;
		}
	}
}
