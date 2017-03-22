package com.woog.walle;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.woog.walle.additional.Crypto;
import com.woog.walle.additional.IDebug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EventGuiClass {
	private Minecraft mc = Minecraft.getMinecraft();
	public ServerData server;
	
	public static String guiName = null;
	private static String oldName = "null";
	private static int oldHash = 0;
	public static boolean conecting = false;
	public static boolean passing = false;
	private static boolean isChange = false;
	public static boolean isDisconected = false;
	private int n = 0;

	@SubscribeEvent
	public void GuiEvent(GuiScreenEvent e) {
		guiName = getGuiName(e.getGui());
		if (e.getGui().hashCode() != oldHash) {
			System.out.println(" 【GUI】 guiName = " + guiName + "  oldName = " + oldName);
			isChange = true;
			oldHash = e.getGui().hashCode();
			oldName = guiName;
			n = 0;
		} else {
			isChange = false;
			n += 1;
		}
		if (guiName.matches("^(GuiMultiplayer)|(GuiDisconnected)|(DisconnectedOnlineScreen)|(GuiErrorScreen)$")
				& n > 100) {
			if (!WallE.acts.isEmpty()) {
				WallE.acts.get(0).pause = true;
			}
			isDisconected = false;
//			conect();
		} else if (guiName.equals("iplayer.GuiConnecting") && EventGuiClass.isChange) {
			isDisconected = true;
			new interval().start();
		} else if (guiName.equals("inventory.GuiChest")) {
			if (n == 200) {
				// if(lower.getName().equals("§6§l服务器快捷切换")) {
				// mc.ingameGUI.getChatGUI().printChatMessage(new
				// ChatComponentText("§e§o【Wall-E】" + "afdsnfjdshfjs"));
				// short short1 =
				// mc.thePlayer.openContainer.getNextTransactionID(mc.thePlayer.inventory);
				// ItemStack is = mc.thePlayer.openContainer.slotClick(21, 1, 1,
				// mc.thePlayer);
				// FMLClientHandler.instance().getClient().getNetHandler().addToSendQueue(
				// new C0EPacketClickWindow(mc.thePlayer.openContainer.windowId,
				// 21, 1, 1, is, short1));
				// n = -200;
				// }
			}
		} else if (guiName.equals("inventory.GuiInventory") && isChange) {
			// System.out.println("【W】" + mc.theWorld.provider.getSpawnPoint()
			// +" "+ mc.theWorld.getProviderName());
			// System.out.println("【W】" + mc.theWorld.provider.getHorizon() +"
			// "+ mc.theWorld.getGameRules().getRules()[1]);
			// System.out.println(APIInventory.getMainInventory()[0].toString());
		} else if (guiName.equals("GuiMainMenu") && isChange) {
			System.out.println("【】" + mc + "  " + mc.world + "  " + mc.player);
		}
	}

	private void conect() {
		// boolean serverStatus = false;
		if(server == null) {
			ServerList serList = new ServerList(FMLClientHandler.instance().getClient());
			serList.loadServerList();
			ServerData serTemp;
			for(int i = 0; i < serList.countServers(); i++) {
				serTemp = serList.getServerData(i);
				if(serTemp.serverName.toLowerCase().equals("walle")) {
					this.server = serTemp;
					break;
				}
			}
			if(server == null) {
				return;
			}
		}
		for (int i = 0; i < EventChatClass.lastTenChat.length; i++) {
			EventChatClass.lastTenChat[i] = null;
		}
		if (!this.conecting) {
			System.out.println(" 【GUI】 开始连接服务器。。。");
			EventGuiClass.conecting = true;
			try {
				FMLClientHandler.instance().connectToServer(new GuiMainMenu(), server);
			} catch (Exception e) {
				e.printStackTrace();
				// delay(3000);
				// EventGuiClass.conecting = false;
				new interval().start();
			}
		}
	}

	private class interval extends Thread {
		@Override
		public void run() {
			delay(500);
			EventGuiClass.conecting = false;
		}
	}

	private String getGuiName(GuiScreen gui) {
		String guiName = gui.toString();
		try {
			guiName = guiName.substring(25, guiName.indexOf("@"));
			return guiName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "no";
	}

	private int getScale() {
		if (mc.gameSettings.guiScale == 0) {
			int scale = 0;
			while (scale < 1000 && mc.displayWidth / (scale + 1) >= 320 && mc.displayHeight / (scale + 1) >= 240) {
				++scale;
			}
			return scale;
		} else {
			return mc.gameSettings.guiScale;
		}
	}

	private ServerData getServer() {
		NBTTagCompound nbttagcompound;
		try {
			nbttagcompound = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));
			NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);
			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				System.out.println(
						"[000]" + ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i)).serverName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return server;
	}

	private void delay(int x) {
		try {
			Thread.sleep(x);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
