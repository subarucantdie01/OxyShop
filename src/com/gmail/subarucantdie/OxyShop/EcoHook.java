package com.gmail.subarucantdie.OxyShop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class EcoHook {

	private static Economy economy;
	
	public static boolean setupEconomy() {
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
			return false;
		economy = rsp.getProvider();
		return economy != null;
		
	}
	
	public static Economy getEconomy() {
		return economy;
	}
	
}
