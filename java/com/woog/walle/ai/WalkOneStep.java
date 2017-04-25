package com.woog.walle.ai;

import com.woog.walle.APIChunk;
import com.woog.walle.APIPlayer;
import com.woog.walle.V3D;
import com.woog.walle.additional.IChunk;

import net.minecraft.util.math.Vec3d;

public class WalkOneStep extends ActionBase{
	private V3D[] clockwise = {new V3D(0, 0, 1), new V3D(-1, 0, 0), new V3D(0, 0, -1), new V3D(1, 0, 0)};
	private int fartherTimes = 0;
	private double prevDistance = 0.0D;
	private V3D prevFoot;
	public static V3D blockFoot = null;
	
	@Override
	public String getActName() {
		return "Walk One Step";
	}
	
	@Override
	public boolean showMsg() {
		return false;
	}
	
	public boolean isGet2(Vec3d foot) {
		double distance = foot.distanceTo(APIPlayer.getFoot());
		if(distance >= prevDistance) {
			fartherTimes++;
			prevDistance = distance;
		}
		return distance > 0.8D | fartherTimes > 20;
	}
	
	public boolean isGet(V3D forward) {
		Vec3d fward = new Vec3d((double)forward.x + 0.5D, (double)forward.y, (double)forward.z + 0.5D);
		double distance = fward.distanceTo(APIPlayer.getFoot());
		if(distance > prevDistance) {
			fartherTimes++;
			prevDistance = distance;
		}
		return distance < 0.2D | fartherTimes > 20;
	}
	
	@Override
	public void action() {
		this.blockFoot = APIPlayer.getFootWithOffset();
		V3D forward = APIPlayer.getFootWithOffset().add(clockwise[APIPlayer.getHeading()]);
//		System.out.println("STEP=========" + APIPlayer.getFoot2() + "   " + forward);
		V3D forward2 = forward.add(new V3D(0, 1, 0));
		if(!APIChunk.isEmpty(forward) | !APIChunk.isEmpty(forward2)) {
			return;
		}else{
//			new FaceTo(forward2, 2);
			Vec3d foot = APIPlayer.getFoot();
			util.setMovement(1.0F, false, false);
			int n = 0;
			while(condition() & !isGet(forward) & n < 300) {
				n++;
				delay(2);
			}
			util.setMovement(0, false, false);
		}
	}
	
	private void turn() {
		V3D[] news = new IChunk().toClockWise();
		new FaceTo(news[1], 2);
		new WalkOneStep();
	}
}
