package com.tokenshop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cloutteam.samjakob.gui.ItemBuilder;
import com.cloutteam.samjakob.gui.buttons.GUIButton;
import com.cloutteam.samjakob.gui.types.PaginatedGUI;

public class GUI implements CommandExecutor, Listener {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tokenshop")) {
			if (sender instanceof ConsoleCommandSender) {
				Utils.sendConsoleMessage("&cYou cannot use this command in console.");
			}
			else {
				Player player = (Player) sender;
				
				PaginatedGUI tokenshop = new PaginatedGUI("TokenShop");
				
				List<String> lore1 = new ArrayList<String>();
				lore1.add(Utils.replace("&eYou have &3" + Listeners.getPlayerTokens(player) + " &eTokens."));
				GUIButton button1 = new GUIButton(ItemBuilder.start(Material.BOOK).build());
				button1.setListener(event -> {
					event.setCancelled(true);
				});
				ItemMeta im = button1.getItem().getItemMeta();
				im.setLore(lore1);
				im.setDisplayName(Utils.replace("&3Tokens"));
				button1.getItem().setItemMeta(im);
				tokenshop.setButton(0, button1);
				
				GUIButton button2 = new GUIButton(ItemBuilder.start(Material.STAINED_GLASS_PANE).data((short)7).name(" ").build());
				button2.setListener(event -> {
					event.setCancelled(true);
				});
				for (int i = 1; i < 9; i++) {
					tokenshop.setButton(i, button2);
				}
				
				if (Main.get().getCustomData().getConfigurationSection("Items") != null) {
					Set<String> set = Main.get().getCustomData().getConfigurationSection("Items").getKeys(false);
					for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
						String next = it.next();
						if (Main.get().getCustomData().get("Items." + next) != null) {
							String name = Main.get().getCustomData().getString("Items." + next + ".Name");
							int worth = Main.get().getCustomData().getInt("Items." + next + ".Worth");
							int amount = Main.get().getCustomData().getInt("Items." + next + ".Amount");
							Material material = Material.getMaterial(Main.get().getCustomData().getString("Items." + next + ".Item"));
							List<String> lore = new ArrayList<String>();
							lore.add(Utils.replace("&ex" + amount));
							lore.add(Utils.replace("&eThis item costs &3" + worth + " &eTokens."));
							GUIButton button3 = new GUIButton(ItemBuilder.start(material).name(Utils.replace(name)).lore(lore).build());
							button3.setListener(event -> {
								if (Main.get().getCustomData().getString("Items." + next + ".Use-Command").equalsIgnoreCase("true")) {
									event.setCancelled(true);
									if (Listeners.getPlayerTokens(player) >= worth) {
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.get().getCustomData().getString("Items." + next + ".Command").replaceAll("%buyer%", player.getName().toString()));
										if (Main.get().getCustomData().getString("Items." + next + ".Send-Message") != null) {
											Utils.sendMessage(sender, Main.get().getCustomData().getString("Items." + next + ".Send-Message").replaceFirst("%buyer%", player.getName().toString()).replaceAll("%amount%", "" + amount).replaceAll("%worth%", "" + worth));
										}
										Listeners.removeTokensFromPlayer(player, worth);
										tokenshop.refreshInventory(player);
										player.closeInventory();
										performCommand(player, "tokenshop");
									}
									else {
										Utils.sendMessage(sender, "&cYou do not have enough tokens to purchase this.");
									}
									
								}
								else {
									event.setCancelled(true);
									if (Listeners.getPlayerTokens(player) >= worth) {
										ItemStack item;
										item = new ItemStack(material, amount);
										player.getInventory().addItem(item);
										Utils.sendMessage(player, "&eYou bought &3x" + amount + " &e" + item.getType().toString().toUpperCase());
										Listeners.removeTokensFromPlayer(player, worth);
										tokenshop.refreshInventory(player);
										player.closeInventory();
										performCommand(player, "tokenshop");
									}
									else {
										Utils.sendMessage(sender, "&cYou do not have enough tokens to purchase this.");
									}
								}
							});
							tokenshop.addButton(button3);
						}
					}
				}
				player.openInventory(tokenshop.getInventory());
			}
		}
		else if (cmd.getName().equalsIgnoreCase("tokens")) {
			if (sender instanceof ConsoleCommandSender) {
				Utils.sendConsoleMessage("&cYou cannot use this command in console.");
			}
			else {
				if (!sender.hasPermission("tokens.admin")) {
					Utils.sendMessage(sender, "&cYou don't have permission to do this.");
				}
				else {
					Player player = (Player) sender;
					if (args.length == 0 || args.length < 1) {
						Utils.sendMessage(sender, "&cUsage: /tokens <add, remove, check> <player> [amount]");
					}
					else if (args.length == 2) {
						if (args[0].equalsIgnoreCase("check")) {
							Player checked = (Player) Bukkit.getPlayer(args[1]);
							if (checked == null) {
								Utils.sendMessage(sender, "&c" + args[1] + " is currently offline.");
							}
							else {
								if (Listeners.getPlayerTokens(checked) < 1) {
									Utils.sendMessage(sender, "&c" + args[1] + " has 0 tokens.");
								}
								else {
									Utils.sendMessage(sender, "&c" + args[1] + " has " + Listeners.getPlayerTokens(checked) + " tokens.");
								}
							}
						}
						else {
							Utils.sendMessage(sender, "&cUsage: /tokens <add, remove, check> <player> [amount]");
						}
					}
					else if (args.length == 3) {
						if (args[0].equalsIgnoreCase("add")) {
							Player checked = (Player) Bukkit.getPlayer(args[1]);
							if (Utils.isInt(args[2])) {
								if (checked == null) {
									Utils.sendMessage(sender, "&c" + args[1] + " is currently offline.");
								}
								else {
									Listeners.addTokensToPlayer(checked, Integer.parseInt(args[2]));
									Utils.sendMessage(sender, "&eYou have given " + args[1] + " " + args[2] + " tokens.");
									Utils.sendMessage(checked, "&e" + player.getName() + " has given you " + args[2] + " tokens.");
								}
							}
							else {
								Utils.sendMessage(sender, "&cError: Please enter a number.");
							}
						}
						else if (args[0].equalsIgnoreCase("remove")) {
							Player checked = (Player) Bukkit.getPlayer(args[1]);
							if (Utils.isInt(args[2])) {
								if (checked == null) {
									Utils.sendMessage(sender, "&c" + args[1] + " is currently offline.");
								}
								else {
									if (Listeners.getPlayerTokens(checked) - Integer.parseInt(args[2]) < 0) {
										Utils.sendMessage(sender, "&cYou cannot remove more than the amount of tokens " + args[1] + " has.");
									}
									else {
										Listeners.removeTokensFromPlayer(checked, Integer.parseInt(args[2]));
										Utils.sendMessage(sender, "&eYou have removed " + args[2] + " tokens from " + args[1] + ".");
										Utils.sendMessage(checked, "&e" + player.getName() + " has removed " + args[2] + " tokens from you.");
									}
								}
							}
							else {
								Utils.sendMessage(sender, "&cError: Please enter a number.");
							}
						}
						else {
							Utils.sendMessage(sender, "&cUsage: /tokens <add, remove, check> <player> [amount]");
						}
					}
					else {
						Utils.sendMessage(sender, "&cUsage: /tokens <add, remove, check> <player> [amount]");
					}
				}
			}
		}
		return true;
	}
	
	public static void performCommand(final Player player, final String command) {
		TaskChain.newChain().add(new TaskChain.GenericTask() {
	        public void run() {
	            player.chat("/" + command);
	        }
	    }).execute();
	}

}
