package com.woog.walle.additional;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

public class IDebug {
	private final static Style yellow = new Style().setColor(TextFormatting.YELLOW);
	private final static Style red = new Style().setColor(TextFormatting.RED);
	
	public static void PrintYellow(String msg) {
		FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(Str2Yellow(msg));
	}
	
	public static void PrintRed(String msg) {
		FMLClientHandler.instance().getClient().ingameGUI.getChatGUI().printChatMessage(Str2Red(msg));
	}
	
	private static ITextComponent Str2Yellow(String str) {
//		new ITextComponent();
//		(new TextComponentString("")).appendSibling((new TextComponentString("[WallE]: ")).setStyle((new Style()).setColor(TextFormatting.YELLOW).setBold(Boolean.valueOf(true)))).appendSibling(new TextComponentTranslation(untranslatedTemplate, objs))
		return (new TextComponentString("")).appendSibling((new TextComponentString("[WallE]: ")).setStyle(yellow).appendText(str));
	}
	
	private static ITextComponent Str2Red(String str) {
		return (new TextComponentString("")).appendSibling((new TextComponentString("[WallE]: ")).setStyle(red).appendText(str));
	}
}
