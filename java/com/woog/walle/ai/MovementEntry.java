package com.woog.walle.ai;

import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MovementEntry extends MovementInput {
	private float iForward;
	private float iStrafe;
	private boolean iJump;
	private boolean iSneak;
	
	public MovementEntry(float ws, boolean ad, boolean sneak) {
		this.iForward = ws;
		this.iJump = jump;
		this.iSneak = sneak;
	}
	
	public MovementEntry() {}
	
	public void set(float ws, float ad, boolean j, boolean s) {
		this.iForward = ws;
		this.iStrafe =ad;
		this.iJump = j;
		this.iSneak = s;
	}
	
	public void updatePlayerMoveState()
    {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if(this.iForward > 0.0F) {
            ++this.moveForward;
        }else if(this.iForward < 0.0F) {
            --this.moveForward;
        }
        if(this.iStrafe > 0.0F) {
        	++this.moveStrafe;
        }else if(this.iStrafe < 0.0F) {
        	--this.moveStrafe;
        }
        
        this.jump = this.iJump;
        this.sneak = this.iSneak;

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }
}
