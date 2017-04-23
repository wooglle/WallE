package com.woog.walle.additional;

import java.util.ArrayList;
import java.util.List;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.ai.AIManager;

import net.minecraft.client.Minecraft;

public class IChunk {
	private V3D[] target= new V3D[18];
	private V3D postion;
//	private static V3D[] clockwise;
	private static final V3D[] clockwise = {new V3D(0, 0, 1), new V3D(-1, 0, 0), new V3D(0, 0, -1), new V3D(1, 0, 0)};
	private static final V3D[] FRLB = {new V3D(0, 0, 1), new V3D(-1, 0, 0), new V3D(0, 0, -1), new V3D(1, 0, 0)};
	private static final V3D[] FRL = {new V3D(0, 0, 1), new V3D(-1, 0, 0), new V3D(0, 0, -1)};
	public boolean isTurn = false;
	
	public IChunk(V3D pos) {
		this.postion = pos;
	}
	
	public IChunk() {
		this.postion = APIPlayer.getFootWithOffset();
	}
	
	public V3D[] getCorssHeight4() {
		V3D[] cross = new V3D[18];
		cross[0] = new V3D(this.postion.x, this.postion.y + 2, this.postion.z);
		cross[1] = new V3D(this.postion.x, this.postion.y + 3, this.postion.z);
		int index = 0;
		int startIndex = APIPlayer.getHeading();
		for(int i = 0; i < 4; i++) {
			index = (startIndex + i + 1) < 4 ? startIndex + i + 1: startIndex + i + 1 - 4;
			for(int j = 0; j < 4; j++) {
				cross[i * 4 + j + 2] = new V3D(this.postion.x +clockwise[index].x, this.postion.y +clockwise[index].y + j, this.postion.z +clockwise[index].z);
			}
		}
		V3D[] out = getDigSafe(cross);
		return out;
	}
	
	/**
	 * 获取玩家以前后左右各1格方块围成（指定高度）的十字内方块的坐标
	 * @param height 从玩家脚底向上的高度， 为2时等于玩家高度
	 * @return 坐标数组
	 */
	public V3D[] getCorssHeight(int height) {
		V3D[] cross = new V3D[height - 2 + 4 * height];
		if(height - 2 > 0) {
			for(int i = 0; i < height - 2; i++) {
				cross[i] = new V3D(this.postion.x, this.postion.y + 2 + i, this.postion.z);
			}
		}
		int index = 0;
		int startIndex = APIPlayer.getHeading();
		for(int i = 0; i < 4; i++) {
			index = (startIndex + i + 1) < 4 ? startIndex + i + 1: startIndex + i - 3;
			for(int j = 0; j < height; j++) {
				cross[i * height + height - 2 + j] = new V3D(this.postion.x +clockwise[index].x, this.postion.y +clockwise[index].y + j, this.postion.z +clockwise[index].z);
			}
		}
		V3D[] out = getDigSafe(cross);
		return out;
	}
	
	public V3D[] getCorssHeight3() {
		V3D[] cross = new V3D[13];
		cross[0] = new V3D(this.postion.x, this.postion.y + 2, this.postion.z);
		int index = 0;
		int startIndex = APIPlayer.getHeading();
		for(int i = 0; i < 4; i++) {
			index = (startIndex + i + 1) < 4 ? startIndex + i + 1: startIndex + i + 1 - 4;
			for(int j = 0; j < 3; j++) {
				cross[i * 4 + j + 1] = new V3D(this.postion.x +clockwise[index].x, this.postion.y +clockwise[index].y + j, this.postion.z +clockwise[index].z);
			}
		}
		V3D[] out = getDigSafe(cross);
		return out;
	}	
	
	public V3D[] getDigSafe(V3D[] tar) {
		List<Integer> list = new ArrayList<Integer>(tar.length);
		int h = 6;
		V3D foot = APIPlayer.getFootWithOffset();
		for(int i = 0; i < tar.length; i++) {
			if(!tar[i].isDangerAjacent()) {
				list.add(i);
			}else{
				if(tar[i].x == foot.x && tar[i].z == foot.z) {
					h =3;
				}else{
					V3D forward = this.postion.add(this.clockwise[APIPlayer.getHeading()]);
					if(tar[i].isEqual(forward) | tar[i].isEqual(forward.add(new V3D(0, 1, 0)))) {
						this.isTurn  = true;
					}
				}
			}
		}
		if(list.isEmpty()) {
			return null;
		}
		List<Integer> list2 = new ArrayList<Integer>(list.size());
		int ind = 0;
		if(h < 6) {
			for(int i = 0; i < list.size(); i++) {
				if(tar[list.get(i)].y < foot.y + 2) {
					list2.add(list.get(i));
					ind++;
				}
			}
		}else{
			list2 = list;
		}
		
		V3D[] safe = new V3D[list2.size()];
		for(int i = 0; i < list2.size(); i++) {
			safe[i] = tar[list2.get(i)];
		}
		return safe;
	}
	
	private boolean canGet(V3D tar) {
//		System.out.println(tar.equals("canGet = " + new V3D(Minecraft.getMinecraft().thePlayer.rayTrace(5.0D, 1.0F).hitVec)));
		System.out.println("canGe  1 = " + tar);
		System.out.println("canGet 2 = " + new V3D(Minecraft.getMinecraft().player.rayTrace(5.0D, 5.0F).hitVec));
//		return tar.equals(new V3D(Minecraft.getMinecraft().thePlayer.rayTrace(5.0D, 1.0F).hitVec));
		return true;
	}
	
	public V3D getFRLB() {
		V3D[] cross = new V3D[4];
		int index = 0;
		int startIndex = APIPlayer.getHeading();
		for(int i = 0; i < cross.length; i++) {
			index = (startIndex + i) < cross.length ? startIndex + i : startIndex + i - cross.length;
			cross[i] = this.postion.add(FRLB[index]);
		}
		List<Integer> list = new ArrayList<Integer>(4);
		int n = 0;
		for(int i = 0; i < cross.length; i++) {
			if(APIChunk.isSafeForStand(cross[i])) {
				if(i > 0) {
					AIManager.addCorner(cross[i]);
					AIManager.setIsLoop();
				}
				return cross[i].addY(1);
			}
		}
		return cross[0];
	}
	
	public V3D getRandomFRLB() {
		V3D[] aims = new V3D[4];
		List<Integer> canStand = new ArrayList<Integer>();
		for(int i = 0; i < 4; i++) {
			aims[i] = this.postion.add(FRLB[i]);
		}
		for(int i = 0; i < 4; i++) {
			if(APIChunk.isSafeForStand(aims[i])) {
				canStand.add(i);
			}
		}
		int r = (int)(Math.random() * canStand.size());
		int ra = canStand.get(r);
//		System.out.println("     " + r + "  " + canStand);
		if(canStand.isEmpty()) {
			return null;
		}else{
			return aims[canStand.get(r)];
		}
		
	}
	
	public V3D getRandomFRL() {
		V3D[] cross = new V3D[4];
		int index = 0;
		int startIndex = APIPlayer.getHeading();
		for(int i = 0; i < cross.length; i++) {
			index = (startIndex + i) < cross.length ? startIndex + i : startIndex + i - cross.length;
			cross[i] = this.postion.add(FRLB[index]);
		}
		List<Integer> list = new ArrayList<Integer>(3);
		int n = 0;
		for(int i = 0; i < cross.length - 1; i++) {
			if(APIChunk.isSafeForStand(cross[i])) {
				if(i > 0) {
					AIManager.addCorner(cross[i]);
					AIManager.setIsLoop();
				}
				list.add(i);
			}
		}
//		System.out.println("【IChunk】  " + list.toArray()  + "   " + list.size());
		if(list.size() > 1) {
			int random = (int)Math.random() * list.size();
//			System.out.println("【IChunk】         " + random);
			return cross[random];
		}else{
			return cross[0];
		}
	}

	public V3D getClockWise() {
		boolean[] bool = new boolean[4];
		List<Integer> list = new ArrayList<Integer>(4);
		int n = 0;
		V3D[] clock = this.toClockWise();
		for(int i = 0;clock[i] != null && i < clock.length; i++) {
//			boolean boo = clock[i].isDangerStand();
			if(APIChunk.isSafeForStand(clock[i])) {
				return clock[i];
			}
		}
		return null;
	}
	public V3D getClockWise2() {
		boolean[] bool = new boolean[4];
		List<Integer> list = new ArrayList<Integer>(4);
		int n = 0;
		V3D[] clock = this.toClockWise();
		for(int i = 0;clock[i] != null && i < clock.length; i++) {
			if(APIChunk.isSafeForStand(clock[i])) {
				list.add(i);
			}
		}
		if(list.isEmpty()) {
			return null;
		}
		V3D[] safe = new V3D[list.size()];
		for(int i = 0; i < list.size(); i++) {
			safe[i] = this.toClockWise()[list.get(i)];
		}
		return safe[0];
	}
	
	public V3D[] toClockWise() {
		V3D[] cross = new V3D[4];
		int index = 0;
		int startIndex = APIPlayer.getHeading();
		for(int i = 0; i < cross.length; i++) {
			index = (startIndex + i) < cross.length ? startIndex + i : startIndex + i - cross.length;
			cross[i] = this.postion.add(clockwise[index]);
		}
		return cross;
	}
}
