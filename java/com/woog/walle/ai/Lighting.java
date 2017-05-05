package com.woog.walle.ai;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.RayTraceTarget;
import com.woog.walle.V3D;
import com.woog.walle.V3DHelper;
import com.woog.walle.WallE;
import com.woog.walle.additional.ICLighting;

import net.minecraft.util.EnumFacing;

public class Lighting extends ActionBase{
	List<V3D> lightingSuperfluous = new ArrayList<V3D>(10);
	
	@Override
	public String getToolsKeyword() {
		return "torch";
	}
	
	@Override
	public String getActName() {
		return "Lighting";
	}
	
	@Override
	public boolean showMsg() {
		return false;
	}
	
	@Override
	public boolean condition() {
		return this.doing && !this.pause && APIPlayer.getFootWithOffset().y == WallE.runtime.lightingHeight;
	}
	
	private boolean hasNext() {
		if(WallE.runtime.lightingNext != null && !APIChunk.getBlock(WallE.runtime.lightingNext).getRegistryName().toString().equals("minecraft:torch")) {
			return true;
		}
		ICLighting ic = WallE.runtime.icLighting;
		if(WallE.runtime.lightingPrevious == null) {
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~");
			WallE.runtime.lightingPrevious = ic.firstLight;
		}
		V3D temp = V3DHelper.getPosByDistance(WallE.runtime.lightingPrevious, WallE.runtime.longitude, ic.longitudeLenght);
//		System.out.println("Lighting  1  " + WallE.runtime.lightingPrevious + temp);
		if(!APIChunk.isSafeForStand(temp)) {
			temp = V3DHelper.getPosByDistance(WallE.runtime.lightingPrevious, ic.latitude, ic.latitudeLenght / 2);
			WallE.runtime.longitude = WallE.runtime.longitude.getOpposite();
			if(!APIChunk.isSafeForStand(temp)) {
				temp = V3DHelper.getPosByDistance(temp, WallE.runtime.longitude, ic.longitudeLenght / 2);
				if(!APIChunk.isSafeForStand(temp)) {
					WallE.runtime.longitude = WallE.runtime.longitude.getOpposite();
					temp = temp.add(WallE.runtime.longitude.getFrontOffsetX() * ic.longitudeLenght, 0, WallE.runtime.longitude.getFrontOffsetZ() * ic.longitudeLenght);
					if(!APIChunk.isSafeForStand(temp)) {
						return false;
					}
				}
			}
		}
		WallE.runtime.lightingPrevious = WallE.runtime.lightingNext;
		WallE.runtime.lightingNext = temp;
		return true;
	}
	
	private boolean hasSuperfluous() {
		if(WallE.runtime.lightingSuperfluous != null && !WallE.runtime.lightingSuperfluous.isEmpty()) {
			return true;
		}
		List<V3D> list = V3DHelper.getSquareByFacing(WallE.runtime.lightingNext, WallE.runtime.icLighting.longitude, WallE.runtime.icLighting.longitudeLenght, 
				WallE.runtime.icLighting.latitude , WallE.runtime.icLighting.latitudeLenght);
//		System.out.println(list);
		for(V3D t : list) {
			if(APIChunk.getBlock(t).getRegistryName().toString().equals("minecraft:torch")) {
				WallE.runtime.lightingSuperfluous.add(t);
			}
		}
		return WallE.runtime.lightingSuperfluous == null ? false : !WallE.runtime.lightingSuperfluous.isEmpty();
	}
	
	@Override
	public void action() {
		if(WallE.runtime.icLighting == null) {
			ICLighting light = new ICLighting();
			WallE.runtime.icLighting = light;
			WallE.runtime.lightingPrevious = new V3D(light.firstLight.x, light.firstLight.y, light.firstLight.z);
			WallE.runtime.longitude = light.longitude;
			WallE.runtime.lightingHeight = APIPlayer.getFootWithOffset().y;
		}
		while(this.condition() && this.hasNext()) {
			while(this.condition() && this.hasSuperfluous()) {
				if(!APIPlayer.getFootWithOffset().equals(WallE.runtime.lightingSuperfluous.get(0))) {
					new RayTraceTarget(WallE.runtime.lightingSuperfluous.get(0));
					new Walk2There();
					return;
				}else{
					new FaceTo(APIPlayer.getFootWithOffset(), EnumFacing.DOWN, 1);
					delay(100);
					this.util.leftDown();
					delay(100);
					this.util.leftUp();
					WallE.runtime.lightingSuperfluous.remove(0);
					delay(800);
				}
			}
//			System.out.printf("%s", WallE.runtime.lightingNext);
			if(!APIChunk.getBlock(WallE.runtime.lightingNext).getRegistryName().toString().equals("minecraft:torch")) {
				if(!APIPlayer.getFootWithOffset().equals(WallE.runtime.lightingNext)) {
					new RayTraceTarget(WallE.runtime.lightingNext);
					new Walk2There();
					return;
				}else{
					this.holdStuff();
					new FaceTo(WallE.runtime.lightingNext, EnumFacing.DOWN, 1);
					delay(100);
					this.util.rightDown();
					delay(100);
					this.util.rightUp();
				}
			}
		}
		WallE.runtime.icLighting = null;
		WallE.runtime.lightingNext = null;
	}
}
