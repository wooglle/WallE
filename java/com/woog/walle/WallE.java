package com.woog.walle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import com.woog.walle.ai.ActionBase;

import io.netty.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = WallE.MODID, name = WallE.MODNAME, version = WallE.MODVERSION)
@SideOnly(Side.CLIENT)
public class WallE {
	public static final String MODVERSION = "1.11";
	public static final String MODID = "wall-e";
	public static final String MODNAME = "人形自走全能挂机苦工-瓦力";
//	public static Timer timer = null;
//	public static final String mcTitle = Display.getTitle();
	public static String wallepath = Minecraft.getMinecraft().mcDataDir.toString() + "/mods/WallE/";
	public static WalleConfig config = new WalleConfig();
	public static String username = FMLClientHandler.instance().getClient().getSession().getUsername();
//	public static World world;
	public static Minecraft mc = Minecraft.getMinecraft();
	public static String myfriends = "strideyouyou,mcfy,st_,woog,020";
	public static String[] vip = { "woog", "sdsas", "mr_caigi", "lzd", "ck250", "ls", "panjy", "yunzhongyan",
			"strideyouyou", "ann", "david", "fant", "st", "020", "ez12", "virginia", "reden", "touxin", "candy_1",
			"mcfy" };
	public static String[] master = {"foobar"};
	public static int[] dangerId = { 8, 9, 10, 11 };
	public static int[] dangerStand = { 0, 81, 90, 119, 209 };
	public static int minItemDamage = -5;
	public static List<V3D> way = new ArrayList(100);
//	public static boolean isWalking = false;
	public static List<ActionBase> acts = new ArrayList(10);
	public static ActionBase currentAct = null;
	public static List<V3D> TreeRootPos = new ArrayList<V3D>(20);
	/**砍树AI保存已经长成的树的坐标列表*/
	public static List<V3D> TreePos = new ArrayList<V3D>(20);
	public static boolean isCuttingTrees = false;

	public void preLoad(FMLPreInitializationEvent event) throws FileNotFoundException, IOException {
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		// ArgumentAcceptingOptionSpec argumentacceptingoptionspec9 = new
		// OptionParser().accepts("username").withRequiredArg().defaultsTo("WooG");
		// ForgeEventFactory;
		// GameProfile
		// FMLClientHandler.instance().getClientPlayerEntity().getDisplayName();
		// ForgeEventFactory
		// mc.mcProfiler;
		// FMLClientHandler.instance().getClient().func_152347_ac()
		if(event.getSide() == Side.SERVER)
			return;
		
//		System.out.println(mc.gameSettings.getSoundLevel(SoundCategory.MASTER));
		if(mc.gameSettings.getSoundLevel(SoundCategory.MASTER) == 0F) {
			mc.gameSettings.setSoundLevel(SoundCategory.MASTER, 0.01F);
		}
		// GetPrivate.getField(net.minecraft.main.Main, name)

		this.mc = FMLClientHandler.instance().getClient();
		System.out.println("客户端输出：" + WallE.MODNAME);

		MinecraftForge.EVENT_BUS.register(new EventChatClass());
		MinecraftForge.EVENT_BUS.register(new EventSoundClass());
		MinecraftForge.EVENT_BUS.register(new EventGuiClass());
		MinecraftForge.EVENT_BUS.register(new EventTickClass());

		// MinecraftForge.EVENT_BUS.register(new EventOfflineClass());
		// System.out.println("客户端输出：" +
		// chat.matches("^\\<\\w+>\\s+~~\\s+\\w+\\s+.+~$"));
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event) {
	}
}