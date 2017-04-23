package com.woog.walle.additional;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.woog.walle.V3D;

public class GLDraw {
	public static void drawPointList(List<V3D> list) {
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(1, 32769);
		setColor(0 % 6);
		GL11.glPointSize(3.0F);
        GL11.glLineWidth(3.0F);
        GL11.glEnable(2832);
        
        
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDepthMask(true);
	}
	
	public static void drawRect(int x, int y, int width, int height, int color) {
	    GL11.glDisable(2929);
	    GL11.glDisable(3553);
	    GL11.glEnable(3042);
	    GL11.glBlendFunc(770, 771);

	    double red = (color >> 16 & 0xFF) / 255.0D;
	    double green = (color >> 8 & 0xFF) / 255.0D;
	    double blue = (color & 0xFF) / 255.0D;
	    double alpha = (color >> 24 & 0xFF) / 255.0D;

	    GL11.glColor4d(red, green, blue, alpha);

	    GL11.glBegin(7);
	    GL11.glVertex2i(x, y);
	    GL11.glVertex2i(x, y + height);
	    GL11.glVertex2i(x + width, y + height);
	    GL11.glVertex2i(x + width, y);
	    GL11.glEnd();

	    GL11.glDisable(3042);
	    GL11.glEnable(3553);
	}
	
	public static void drawLine(int x, int y, int width, int height, int color) {
		GL11.glEnable(3042);
		GL11.glDisable(2929);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		
		double red = (color >> 16 & 0xFF) / 255.0D;
		double green = (color >> 8 & 0xFF) / 255.0D;
		double blue = (color & 0xFF) / 255.0D;
		double alpha = (color >> 24 & 0xFF) / 255.0D;
		
//		GL11.glColor4d(red, green, blue, alpha);
		GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
		
		GL11.glBegin(7);
		GL11.glVertex2i(x, y);
		GL11.glVertex2i(x, y + height);
		GL11.glVertex2i(x + width, y + height);
		GL11.glVertex2i(x + width, y);
		GL11.glEnd();
		
		GL11.glDisable(3042);
		GL11.glEnable(3553);
	}
	
	
	
	private static void setColor(int c)
	  {
	    if (c == 0)
	    {
	      GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
	    }
	    else if (c == 4)
	    {
	      GL11.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
	    }
	    else if (c == 2)
	    {
	      GL11.glColor4f(0.0F, 0.0F, 1.0F, 1.0F);
	    }
	    else if (c == 5)
	    {
	      GL11.glColor4f(1.0F, 1.0F, 0.0F, 1.0F);
	    }
	    else if (c == 1)
	    {
	      GL11.glColor4f(1.0F, 0.0F, 1.0F, 1.0F);
	    }
	    else if (c == 3)
	    {
	      GL11.glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
	    }
	    else
	    {
	      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    }
	  }
}
