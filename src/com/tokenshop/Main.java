package com.tokenshop;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.cloutteam.samjakob.gui.types.PaginatedGUI;

import at.helpch.placeholderapi.example.expansions.TokenExpansion;


public class Main extends JavaPlugin implements Listener {
	
	public static Main plugin;
	private File dataFile;
	private FileConfiguration data;
	private File tokenFile;
	private FileConfiguration tokendata;
	
	@Override
	public void onEnable() {
		plugin = this;
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TokenExpansion(this).register();
		}
		createCustomData();
		createTokenData();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getCommand("tokenshop").setExecutor(new GUI());
		getCommand("tokens").setExecutor(new GUI());
		PaginatedGUI.prepare(this);
		System.out.println("[TokenShop] Enabled");
	}
	
	public void onDisable() {
		System.out.println("[TokenShop] Disabled");
	}
	
	public FileConfiguration getCustomData() {
    	return this.data;
    }
	
	public FileConfiguration getTokenData() {
    	return this.tokendata;
    }
	
	private void createCustomData() {
    	dataFile = new File(getDataFolder(), "data.yml");
    	if (!dataFile.exists()) {
    		dataFile.getParentFile().mkdirs();
    		saveResource("data.yml", false);
    	}
    	
    	data = new YamlConfiguration();
    	try {
    		data.load(dataFile);
    	} catch (IOException | InvalidConfigurationException e) {
    		e.printStackTrace();
    	}
    }
	
	private void createTokenData() {
    	tokenFile = new File(getDataFolder(), "tokens.yml");
    	if (!tokenFile.exists()) {
    		tokenFile.getParentFile().mkdirs();
    		saveResource("tokens.yml", false);
    	}
    	
    	tokendata = new YamlConfiguration();
    	try {
    		tokendata.load(tokenFile);
    	} catch (IOException | InvalidConfigurationException e) {
    		e.printStackTrace();
    	}
    }
	
	public static Main get() {
		return plugin;
	}
	
	public void saveConfig() {
        try {
            this.data.save(this.dataFile);
        } catch (IOException e) {
        	getLogger().warning("Unable to save data.yml");
        }
    }
 
    public void reloadConfig() {
        this.data = YamlConfiguration.loadConfiguration(this.dataFile);
    }
    
	public void saveTokenConfig() {
        try {
            this.tokendata.save(this.tokenFile);
        } catch (IOException e) {
        	getLogger().warning("Unable to save tokens.yml");
        }
    }
 
    public void reloadTokenConfig() {
        this.tokendata = YamlConfiguration.loadConfiguration(this.tokenFile);
    }

}
