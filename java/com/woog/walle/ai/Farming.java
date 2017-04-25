package com.woog.walle.ai;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.V3DHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class Farming extends ActionBase{
	private boolean isWart;
	@Override
	public String getActName(){
		return "高科技农业";
	}
	
	@Override
	public boolean condition(){
		return doing & !pause;
	}
	
	private Item seedOfCrops(){
		if(APIChunk.getBlockEyesOn() instanceof BlockBush){
			Block block = APIChunk.getBlockEyesOn();
			this.isWart = false;
			if(block == Blocks.WHEAT){
				return Items.WHEAT_SEEDS;
			}else if(block == Blocks.CARROTS){
				return Items.CARROT;
			}else if(block == Blocks.NETHER_WART){
				this.isWart = true;
				return Items.NETHER_WART;
			}else if(block == Blocks.POTATOES){
				return Items.POTATO;
			}
		}
		return null;
	}
	
	private V3D getDirt(int n){
		for(int z = 0; z < n + 1; z++){
			for(int x = 0; x < n + 1; n++){
				Block block = Minecraft.getMinecraft().world.getBlockState(new BlockPos(
						APIPlayer.getFootWithOffset().x + x, APIPlayer.getFootWithOffset().y, APIPlayer.getFootWithOffset().z + z)).getBlock();
				if(block == Blocks.FARMLAND){
					return new V3D(APIPlayer.getFootWithOffset().x + x, APIPlayer.getFootWithOffset().y, APIPlayer.getFootWithOffset().z + z);
				}
			}
		}
		return null;
	}
	
	private V3D getDirtInLine(){
		int dis = 5;
		if(getDirt(dis) == null){
			return null;
		}
		if(isWart){
			
			List<V3D> list = V3DHelper.getRadiantSquare(APIPlayer.getFootWithOffset().addY(1), dis);
			for(int i = 0; i < (dis + 1) * (dis + 1); i++){
				if(Minecraft.getMinecraft().world.getBlockState(list.get(i).toBlockPos()) == Blocks.FARMLAND){
					return list.get(i);
				}
			}
		}else{
			List<V3D> list = V3DHelper.getRadiantSquare(APIPlayer.getFootWithOffset(), dis);
			for(int i = 0; i < (dis + 1) * (dis + 1); i++){
				if(Minecraft.getMinecraft().world.getBlockState(list.get(i).toBlockPos()) == Blocks.FARMLAND){
					return list.get(i);
				}
			}
		}
		return null;
	}
	
	private V3D getLongSide(){
		List<V3D> cross = new ArrayList<V3D>(5 * 4);
		Integer[] sides = new Integer[4];
		sides[0] = 0;
		sides[1] = 0;
		sides[2] = 0;
		sides[3] = 0;
		List<V3D> list = V3DHelper.getCrossPlane(APIPlayer.getFootWithOffset(), 5);
		for(int i = 0; i < 5 * 4; i++){
			BlockPos pos = list.get(i).toBlockPos();
			if(Minecraft.getMinecraft().world.getBlockState(pos) == Blocks.FARMLAND){
				sides[i % 4]++; 
			}
		}
		int max = 0;
		int index = 0;
		for(int i = 0; i < 4; i++){
			if(sides[i] > max){
				max = sides[i];
				index = i;
			}
		}
		return APIPlayer.getFootWithOffset().add(cross.get(index));
	}
	
	@Override
	public void action(){
		this.seedOfCrops();
	}
}
