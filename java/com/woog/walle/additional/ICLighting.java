package com.woog.walle.additional;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.V3DHelper;

import net.minecraft.util.EnumFacing;

public class ICLighting {
	public int stepLenght;
	public EnumFacing longitude;
	public EnumFacing latitude;
	public int longitudeLenght;
	public int latitudeLenght;
	public V3D firstLight;
	
	public ICLighting() {
		this.set();
	}
	
	public V3D set() {
		V3D nowfloor = APIPlayer.getFootWithOffset();
		List<V3D> torchs = this.getTorch(V3DHelper.getCrossPlane(nowfloor, 6));
		if(torchs != null && !torchs.isEmpty()) {
			this.longitudeLenght = (int)APIPlayer.getFootWithOffset().distance(torchs.get(0)) * 2;
			this.longitude = V3DHelper.toEnumFacing(V3DHelper.getDirection(torchs.get(0), APIPlayer.getFootWithOffset()));
			this.latitudeLenght = (int)APIPlayer.getFootWithOffset().distance(torchs.get(0)) * 2;
			this.latitude = V3DHelper.toEnumFacing(V3DHelper.getDirection(torchs.get(1), APIPlayer.getFootWithOffset()));
//			this.firstLight = torchs.get(0).add(latitude.x * this.latitudeLenght, 0, latitude.z * this.latitudeLenght);
			this.firstLight = torchs.get(1);
		}
		return null;
	}
	
	private List<V3D> getTorch(List<V3D> list) {
		List<V3D> b = new ArrayList<V3D>(2);
		for(V3D t : list) {
			if(APIChunk.getBlock(t).getRegistryName().toString().equals("minecraft:torch")) {
				b.add(t);
			}
		}
		return b;
	}
}
