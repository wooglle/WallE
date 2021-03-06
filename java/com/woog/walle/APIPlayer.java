package com.woog.walle;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.FMLClientHandler;

public class APIPlayer {
	private static Minecraft mc = FMLClientHandler.instance().getClient();
	public static int getFoodLevel(){
		FMLClientHandler.instance().getClientPlayerEntity();
		return FMLClientHandler.instance().getClientPlayerEntity().getFoodStats().getFoodLevel();
	}
	public static float getHealth(){
		return FMLClientHandler.instance().getClientPlayerEntity().getHealth();
	}
	public static boolean isHungry(){
		return getFoodLevel() < 10;
	}
	public static boolean isFull() {
		return getFoodLevel() < 20;
	}
	public static double posX(){
		return Math.floor(mc.player.posX);
	}
	public static double posY(){
		return Math.floor(mc.player.posY - mc.player.eyeHeight + 0.2F);
	}
	public static double posZ(){
		//EntityFX.interpPosZ 1.8鍜�1.7鍧愭爣绯诲ソ鍍忎笉涓�鏍凤紝 鎴戣繕寰楄皟涓�涓嬪潗鏍囷紝 涓嶇劧鎬绘槸鎸栧亸銆傘��
		return Math.floor(mc.player.posZ);
	}
	public static Vec3d getFoot() {
		return new Vec3d((double)mc.player.getPositionEyes(1.0F).xCoord, 
				(double)mc.player.getPositionEyes(1.0F).yCoord - 1.62D, 
				(double)mc.player.getPositionEyes(1.0F).zCoord);
	}
	
	public static V3D getFootWithOffset() {
//		mc.player.boundingBox.minY
//		System.out.println("=====  " + mc.player.getPositionEyes(1.0F));
//		return new V3D(mc.player.getPosition());
		return new V3D(mc.player.getPositionEyes(1.0F).xCoord, 
				mc.player.getPositionEyes(1.0F).yCoord - 1.62D + 0.2D, 
				mc.player.getPositionEyes(1.0F).zCoord);
	}
	
	public static Entity getEntityEyesOn() {
//		mc.getItemRenderer();
//		mc.theWorld.rayTraceBlocks(p_72933_1_, p_72933_2_);
		return mc.pointedEntity;
	}
	
	public static Vec3d getEye() {
//		mc.player.getEntityBoundingBox();
		return new Vec3d((double)mc.player.getPositionEyes(1.0F).xCoord, 
				(double)mc.player.getPositionEyes(1.0F).yCoord, 
				(double)mc.player.getPositionEyes(1.0F).zCoord);
	}
	
	public static V3D getHeadPos() {
		return new V3D(mc.player.getPositionEyes(0));
	}
	
	public static int getHeading() {
//		net.minecraftforge.client.GuiIngameForge;
		return MathHelper.floor((double)(mc.player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
	}
	
	/**
	 * 获取表示玩家朝向的坐标数据
	 * @return
	 */
	public static Vec3d getDirectionDiff() {
		return mc.player.getForward();
	}
	
	/**
	 * 获取玩家的水平方向
	 * @return
	 */
	public static EnumFacing getFacing() {
		//GuiOverlayDebug
		return mc.player.getHorizontalFacing();
	}
	
	public static V3D getEyesOn() {
		return new V3D(viewX(), viewY(), viewZ());
	}
	
	public static float yaw() {
		return mc.player.rotationYaw;
	}
	public  static double yaw2() {
		return MathHelper.wrapDegrees(mc.player.rotationYaw);
	}
	public static float pitch(){
		float pitch = mc.player.rotationPitch;
//		pitch = pitch % 90.0F;
		return pitch;
	}
	public static int viewX(){
		return mc.objectMouseOver.getBlockPos().getX();
	}
	public static int viewY(){
		return mc.objectMouseOver.getBlockPos().getY();
	}
	public static int viewZ(){
		return mc.objectMouseOver.getBlockPos().getZ();
	}	
	public static ItemStack currentInHand() {
		return mc.player.inventory.getCurrentItem();
	}
}
