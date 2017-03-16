package com.woog.walle.ai;

import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MovementEntry extends MovementInput {
	private float f ;
	private boolean j;
	private boolean s;
	
	public MovementEntry(float f, boolean jump, boolean sneak) {
		this.f = f;
		this.j = jump;
		this.s = sneak;
//		updatePlayerMoveState();
	}
	
	public void set(float a, boolean b, boolean c) {
		this.f = a;
		this.j = b;
		this.s = c;
	}
	
	public void updatePlayerMoveState()
    {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (this.f > 0.0F)
        {
            ++this.moveForward;
        }

        if (this.f < 0.0F)
        {
            --this.moveForward;
        }

//        if (this.gameSettings.keyBindLeft.getIsKeyPressed())
//        {
//            ++this.moveStrafe;
//        }
//
//        if (this.gameSettings.keyBindRight.getIsKeyPressed())
//        {
//            --this.moveStrafe;
//        }

//        this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
//        this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
        
        this.jump = this.j;
        this.sneak = this.s;

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }
}
