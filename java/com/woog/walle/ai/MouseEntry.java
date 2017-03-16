package com.woog.walle.ai;

import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class MouseEntry extends MouseEvent {
	//net.minecraftforge.client.event.MouseEvent
	 public final int x;
	 public final int y;
	 public final int dx;
	 public final int dy;
	 public final int dwheel;
	 public final int button;
	 public final boolean buttonstate;
	 public final long nanoseconds;

	 public MouseEntry(int x, int y, int button, boolean buttonstate, long nanoseconds)
	 {
	     this.x = x;
	     this.y = y;
	     this.dx = 0;
	     this.dy = 0;
	     this.dwheel = 0;
	     this.button = button;
	     this.buttonstate = buttonstate;
	     this.nanoseconds = nanoseconds;
	 }
}
