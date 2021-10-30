package com.gmail.subarucantdie.OxyShop;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Main instance;
	public HashMap<UUID, YAML> yamlContainer;
	
	@Override
	public void onLoad() {
		instance = this;
		yamlContainer = new HashMap<>();
	}
	
	@Override
	public void onEnable() {
		if (EcoHook.setupEconomy()) {
			Bukkit.broadcastMessage("§7[OxyShop] §2Economy §abaşarıyla kuruldu!");
		} else {
			Bukkit.broadcastMessage("§7[OxyShop] §2Economy §ckurulumu başarısız!");
		}
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		Bukkit.broadcastMessage("§7[OxyShop] §aEklenti aktifleştirildi!");
	}
	
	@Override
	public void onDisable() {
		Bukkit.broadcastMessage("§7[OxyShop] §cEklenti pasifleştirildi!");
	}
	
}
