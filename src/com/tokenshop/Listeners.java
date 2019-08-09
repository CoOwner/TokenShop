package com.tokenshop;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Listeners implements Listener {
	
	@EventHandler
	public void onKill(PlayerDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			if (event.getEntity().getKiller() instanceof Player) {
				Player victim = (Player) event.getEntity();
				Player killer = (Player) event.getEntity().getKiller();
				if (victim.getKiller() != victim) {
					int tokens = Main.get().getTokenData().getInt("Token-Stats." + killer.getUniqueId().toString()) + 1;
					Main.get().getTokenData().set("Token-Stats." + killer.getUniqueId().toString(), tokens);
					Main.get().saveTokenConfig();
					Main.get().reloadTokenConfig();
				}
			}
		}
	}
	
	public static int getPlayerTokens(Player player) {
		int tokens = Main.get().getTokenData().getInt("Token-Stats." + player.getUniqueId().toString());
		return tokens;
	}
	
	public static void addTokensToPlayer(Player player, int amount) {
		int tokens = Main.get().getTokenData().getInt("Token-Stats." + player.getUniqueId().toString()) + amount;
		Main.get().getTokenData().set("Token-Stats." + player.getUniqueId().toString(), tokens);
		Main.get().saveTokenConfig();
		Main.get().reloadTokenConfig();
	}
	
	public static void removeTokensFromPlayer(Player player, int amount) {
		int tokens = Main.get().getTokenData().getInt("Token-Stats." + player.getUniqueId().toString()) - amount;
		Main.get().getTokenData().set("Token-Stats." + player.getUniqueId().toString(), tokens);
		Main.get().saveTokenConfig();
		Main.get().reloadTokenConfig();
	}
}
