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
	
	private void plantingAccordant() {
		boolean isStop = false;
		V3D eyesonPrevious = null;
		V3D eyeson = null;
		boolean AD = false;
		AD = getAD(WallE.runtime.farmingFace, WallE.runtime.farmingFaceBlock);
		Item seed = null;
		while(WallE.runtime.farmingHasNext) {
			this.setAD(AD);
			while(!this.canShift()) {
				eyeson = APIPlayer.getEyesOn();
				if(eyeson.equals(eyesonPrevious)) {
					delay(10);
					continue;
				}
				seed = this.seedOfCrops();
				if(seed != null) {
					this.toolsKeyword = seed.getUnlocalizedName();
					while(APIChunk.canHavest(eyeson)) {
						this.util.setMovement(0.0F, 0.0F, false, false);
						isStop = true;
						this.util.leftDown();
						delay(20);
						this.util.leftUp();
						delay(10);
					}
					while(APIChunk.isEmpty(eyeson)) {
						delay(10);
						if(!isStop) {
							this.util.setMovement(0.0F, 0.0F, false, false);
						}
						this.holdStuff();
						this.util.rightDown();
						delay(20);
						this.util.rightUp();
						delay(10);
						eyesonPrevious = eyeson;
					}
					if(isStop) {
						this.setAD(AD);
						isStop = false;
					}
				}
			}
			delay(200);
			if(APIPlayer.getFacing().getOpposite().equals(WallE.runtime.icFarming.facingShift)) {
				if(this.isPlantBlock(APIPlayer.getFootWithOffset().add(APIPlayer.getFacing().getOpposite()))) {
					new FaceTo(APIPlayer.getFacing().getOpposite(), 0.0F);
					WallE.runtime.farmingFace = WallE.runtime.farmingFace.getOpposite();
					WallE.runtime.farmingFaceBlock = WallE.runtime.farmingFaceBlock.getOpposite();
					WallE.runtime.farmingNextBlock = APIPlayer.getFootWithOffset().add(WallE.runtime.icFarming.facingShift);
					this.seeThePlant();
//					System.out.printf("Far 121   %s, %s, %s, %s\n", WallE.runtime.farmingNextPos, WallE.runtime.farmingFace, 
//							WallE.runtime.farmingFaceBlock, WallE.runtime.farmingNextBlock);
				}else{
					WallE.runtime.farmingHasNext = false;
					break;
				}
			}else{
				this.accordantShift();
				new RayTraceTarget(WallE.runtime.farmingNextPos, false);
				new Walk2There();
				return;
			}
		}
	}
	
	private void accordantShift() {
		boolean hasNext = false;
		V3D lastPlant = APIPlayer.getEyesOn();
		V3D nowStand = APIPlayer.getFootWithOffset();
		V3D pos = nowStand;
		for(int i = 0; i < 5; i++) {
			pos = pos.add(WallE.runtime.icFarming.facingShift);
			if(APIChunk.isSafeForStand(pos)) {
				WallE.runtime.farmingNextPos = pos;
				WallE.runtime.farmingFace = WallE.runtime.farmingFace.getOpposite();
				WallE.runtime.farmingFaceBlock = WallE.runtime.farmingFaceBlock.getOpposite();
				WallE.runtime.farmingNextBlock = WallE.runtime.farmingNextBlock.add(WallE.runtime.icFarming.facingShift);
//				System.out.printf("Far 232   %s, %s, %s, %s\n", WallE.runtime.farmingNextPos, WallE.runtime.farmingFace, 
//						WallE.runtime.farmingFaceBlock, WallE.runtime.farmingNextBlock);
				hasNext = true;
				break;
			}
		}
		WallE.runtime.farmingHasNext = hasNext;
	}
	
	private void setAD(boolean AD) {
		if(AD) {
//			System.out.println("       向左");
			this.util.setMovement(0.0F, 0.8F, false, false);
		}else{
//			System.out.println("       向右...");
			this.util.setMovement(0.0F, -0.8F, false, false);
		}
	}
	
	/**
	 * face1相对于face2的位置
	 * @param face1
	 * @param face2
	 * @return false : 左; true ： 右
	 */
	private boolean getAD(EnumFacing face1, EnumFacing face2) {
		if(face2.rotateY().getOpposite().equals(face1)) {
			return true;	//A， 向左
		}else if(face2.rotateY().equals(face1)) {
			return false;	//D, 向右
		}
//		System.out.println("Farming ERROR:" + face1 + " is not left or right side of " + face2);
		this.doing = false;
		return false;
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
	
	private void seeThePlant() {
		delay(50);
		new FaceTo(WallE.runtime.farmingFaceBlock, 0.0F);
		delay(50);
		new FaceTo(WallE.runtime.farmingNextBlock, EnumFacing.UP, 2);
		delay(50);
	}
	
	@Override
	public void action() {
		if(WallE.runtime.icFarming == null) {
			WallE.runtime.icFarming = new ICFarming();
			WallE.runtime.farmingNextPos = WallE.runtime.icFarming.firstStandPos;
			WallE.runtime.farmingNextBlock = WallE.runtime .icFarming.firstPlantPos;
			WallE.runtime.farmingFace = WallE.runtime.icFarming.facing;
			WallE.runtime.farmingHasNext = true;
			if(WallE.runtime.icFarming.isAccordant) {
				WallE.runtime.farmingFaceBlock = WallE.runtime.icFarming.facingShift;
			}else{
				WallE.runtime.farmingFaceBlock = EnumFacing.DOWN;
			}
		}
		if(!APIPlayer.getFootWithOffset().equals(WallE.runtime.farmingNextPos)) {
//			this.pause = true;
//			delay(200);
			new RayTraceTarget(WallE.runtime.farmingNextPos, false);
			new Walk2There();
			return;
		}else{
			delay(200);
//			System.out.println("  Far 222  " + WallE.runtime.icFarming.firstPlantPos + "   " + WallE.runtime.icFarming.isAccordant);
			if(WallE.runtime.icFarming.isAccordant) {
				this.seeThePlant();
				this.plantingAccordant();
			}
			if(!WallE.runtime.farmingHasNext) {
				WallE.runtime.icFarming = null;
			}
		}
	}
}
