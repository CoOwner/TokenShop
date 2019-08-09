package com.tokenshop;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Utils {
	
	public static String formatSeconds(long seconds) {
		long days = seconds / (24 * 60 * 60);
		seconds %= 24 * 60 * 60;
		long hh = seconds / (60 * 60);
		seconds %= 60 * 60;
		long mm = seconds / 60;
		seconds %= 60;
		long ss = seconds;
		
		if (days > 0) {
			return days + "d " + hh + "h " + mm + "m " + ss + "s";
		}
		if (hh > 0) {
			return hh + "h " + mm + "m " + ss + "s";
		}
		if (mm > 0) {
			return mm + "m " + ss + "s";
		}
		return ss + "s";
	}
	
	public static String replace(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(replace(message));
	}
	
	public static void bMsg(String message) {
		for (Player p : Bukkit.getOnlinePlayers()) { sendMessage(p, message); }
	}
	
	public static void sendConsoleMessage(String message) {
		Main.get().getServer().getConsoleSender().sendMessage(replace(message));
	}
	
	public static String replace1(String message) {
		return message.replaceAll("&", "\u00A7");
	}
	
	public static boolean isInt(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
}
