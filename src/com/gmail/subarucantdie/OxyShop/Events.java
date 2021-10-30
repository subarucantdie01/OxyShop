package com.gmail.subarucantdie.OxyShop;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import GuiAPI.Inventory;
import net.md_5.bungee.api.ChatColor;

public class Events implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Main.instance.yamlContainer.put(e.getPlayer().getUniqueId(), new YAML(Main.instance.getDataFolder() + File.separator + "playerdata", e.getPlayer().getUniqueId() + ".yml"));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		try {
			YAML yaml = Main.instance.yamlContainer.get(e.getPlayer().getUniqueId()); yaml.save();
			Main.instance.yamlContainer.remove(e.getPlayer().getUniqueId());
		} catch (Exception e2) {
			Bukkit.getConsoleSender().sendMessage("§e" + e.getPlayer().getName() + " §badlı oyuncunun verisi kaydedilirken bir hataya rastlandı: §c§l" + e2.getStackTrace());
		}
	}
	
	@EventHandler
	public void onRightClick(PlayerInteractEntityEvent e) {
		try {
			YAML[] YAMLsInsideDir = YAML.getAllYAMLsInsideDir(Main.instance.getDataFolder().toString());
			for (final YAML yaml : YAMLsInsideDir) {
				if (!e.getRightClicked().getCustomName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', yaml.getYamlConfiguration().getString("settings.rightclickedEntityName"))))
					return;
				
				Inventory i = new Inventory((InventoryHolder)e.getPlayer(), yaml.getYamlConfiguration().getInt("settings.chestRow") > 6 ? 6 : yaml.getYamlConfiguration().getInt("settings.chestRow") < 1 ? 1 : yaml.getYamlConfiguration().getInt("settings.chestRow"), ChatColor.translateAlternateColorCodes('&', yaml.getYamlConfiguration().getString("settings.rightclickedEntityName")));
				
				ItemStack itemStack;
				ItemMeta itemMeta;
				
				String itemKey;
				String itemName;
				
				List<String> itemLore;
				int amountOfItem;
				
				boolean globalBuyable = yaml.getYamlConfiguration().getBoolean("settings.globalBuyable");
				boolean globalSellable = yaml.getYamlConfiguration().getBoolean("settings.globalSellable");
				
				for (String itemSlotAsString : yaml.getYamlConfiguration().getConfigurationSection("items").getKeys(false)) {
					itemKey = yaml.getYamlConfiguration().getString("items." + itemSlotAsString + ".Material");
					itemName = yaml.getYamlConfiguration().getString("items." + itemSlotAsString + ".UI.ItemName");
					itemLore = yaml.getYamlConfiguration().getStringList("items." + itemSlotAsString + ".UI.ItemLore");
					amountOfItem = yaml.getYamlConfiguration().getInt("items." + itemSlotAsString + ".Amount");
					itemStack = new ItemStack(Material.valueOf(itemKey), amountOfItem);
					itemMeta = itemStack.getItemMeta();
					itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
					
					int taxPercentage = yaml.getYamlConfiguration().getInt("settings.taxPercentage");
					int buyBasePrice = yaml.getYamlConfiguration().getInt("items." + itemSlotAsString + ".buyPrice.basePrice");
					int buyTaxPrice = yaml.getYamlConfiguration().getInt("items." + itemSlotAsString + ".buyPrice.taxPrice");
					final int calculatedBuyPrice = buyBasePrice - buyTaxPrice - (buyBasePrice / 100 * taxPercentage);
					int sellBasePrice = yaml.getYamlConfiguration().getInt("items." + itemSlotAsString + ".sellPrice.basePrice");
					int sellTaxPrice = yaml.getYamlConfiguration().getInt("items." + itemSlotAsString + ".sellPrice.taxPrice");
					final int calculatedSellPrice = sellBasePrice - sellTaxPrice - (sellBasePrice / 100 * taxPercentage);
					final int slot = Integer.parseInt(itemSlotAsString);
					
					for (int index = 0; index < itemLore.size(); index++) {
						itemLore.set(index, ChatColor.translateAlternateColorCodes('&', itemLore.get(index)
								.replace("{buyAmount}", Main.instance.yamlContainer.get(e.getPlayer().getUniqueId()).getYamlConfiguration().isSet(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".buyAmount") ? Main.instance.yamlContainer.get(e.getPlayer().getUniqueId()).getYamlConfiguration().getString(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".buyAmount") : yaml.getYamlConfiguration().getInt("settings.maxBuy") == -1 ? "SINIRSIZ" : yaml.getYamlConfiguration().getString("settings.maxBuy"))
								.replace("{sellAmount}", Main.instance.yamlContainer.get(e.getPlayer().getUniqueId()).getYamlConfiguration().isSet(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".sellAmount") ? Main.instance.yamlContainer.get(e.getPlayer().getUniqueId()).getYamlConfiguration().getString(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".sellAmount") : yaml.getYamlConfiguration().getInt("settings.maxSell") == -1 ? "SINIRSIZ" : yaml.getYamlConfiguration().getString("settings.maxSell"))
								.replace("{maxSell}", yaml.getYamlConfiguration().getInt("settings.maxSell") == -1 ? "SINIRSIZ" : yaml.getYamlConfiguration().getString("settings.maxSell"))
								.replace("{maxBuy}", yaml.getYamlConfiguration().getInt("settings.maxBuy") == -1 ? "SINIRSIZ" : yaml.getYamlConfiguration().getString("settings.maxBuy"))
								.replace("{calculatedBuyPrice}", String.valueOf(calculatedBuyPrice)).replace("{calculatedSellPrice}", String.valueOf(calculatedSellPrice))
								.replace("{baseBuyPrice}", String.valueOf(buyBasePrice))
								.replace("{baseSellPrice}", String.valueOf(sellBasePrice))
								.replace("{buyTaxPrice}", String.valueOf(buyTaxPrice))
								.replace("{sellTaxPrice}", String.valueOf(sellTaxPrice))
								.replace("{taxPercentage}", String.valueOf(taxPercentage))
								.replace("{globalBuyable}", globalBuyable ? "AÇIK" : "KAPALI")
								.replace("{globalSellable}", globalSellable ? "AÇIK" : "KAPALI")));
					}
					
					itemMeta.setLore(itemLore);
					itemStack.setItemMeta(itemMeta);
					
					i.getInventory().setItem(slot, itemStack);
					
					final Player p = e.getPlayer();
					final ItemStack finalItemStack = new ItemStack(Material.valueOf(itemKey), amountOfItem);
					
					i.setEventListener(slot, new GuiAPI.Listeners.Listener() {
						
						@Override
						public void onClick() {
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 100, 0);
						}
						
						@Override
						public void onRightClick() {
							int maxSell = yaml.getYamlConfiguration().getInt("settings.maxSell");
							if (maxSell != -1 && !Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().isSet(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".sellAmount")) {
								Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().set(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".sellAmount", maxSell);								
							}
							
							if (maxSell == -1 || Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().getInt(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".sellAmount") > 0) {
								for (int i = 0; i < p.getInventory().getContents().length; i++) {
									if (p.getInventory().getContents()[i] != null && p.getInventory().getContents()[i].getType() == finalItemStack.getType()) {
										if (p.getInventory().getContents()[i].getAmount() >= finalItemStack.getAmount()) {
											ItemStack newItemStack = new ItemStack(p.getInventory().getItem(i).getType(), p.getInventory().getItem(i).getAmount() - finalItemStack.getAmount());
											newItemStack.setItemMeta(p.getInventory().getItem(i).getItemMeta());
											p.getInventory().setItem(i, newItemStack);
											EcoHook.getEconomy().depositPlayer(p, calculatedSellPrice);
											p.sendMessage("§a§lSatış başarılı! §e" + finalItemStack.getAmount() + " §2tane §e" + finalItemStack.getType().toString() + " §2adlı eşyayı §a$" + calculatedSellPrice + " §2fiyatına sattın!");
											if (Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().isSet(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".sellAmount"))
											{
												int sellAmount = Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().getInt(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".sellAmount");
												Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().set(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".sellAmount", sellAmount - 1);											
											}
											return;
										}
									}
								}
								p.sendMessage("§cBu işlemi yapabilmek için §e" + finalItemStack.getAmount() + " §ctane §e" + finalItemStack.getType().toString() + " §cadlı eşyaya sahip olmalısın!");
							} else {
								p.sendMessage("§cBu işlemi yapabilmek için satış hakkına sahip olmalısın!");
							}
						}
						
						@Override
						public void onLeftClick() {
							if (EcoHook.getEconomy().has(p, calculatedBuyPrice)) {
								int freeSpace = 0;
								for (ItemStack i : p.getInventory().getContents()) {
									if (i == null) {
										freeSpace += finalItemStack.getType().getMaxStackSize();
									} else if (i.getType() == finalItemStack.getType()) {
										freeSpace -= i.getAmount();
									}
								}
								
								int maxBuy = yaml.getYamlConfiguration().getInt("settings.maxBuy");
								if (maxBuy != -1 && !Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().isSet(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".buyAmount")) {
									Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().set(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".buyAmount", maxBuy);								
								}
								
								if (maxBuy == -1 || Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().getInt(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".buyAmount") > 0) {
									if (finalItemStack.getAmount() <= freeSpace) {
										EcoHook.getEconomy().withdrawPlayer(p, calculatedBuyPrice);
										p.getInventory().addItem(finalItemStack);
										p.sendMessage("§a§lSatın alma başarılı! §e" + finalItemStack.getAmount() + " §2tane §e" + finalItemStack.getType().toString() + " §2adlı eşyayı §a$" + calculatedBuyPrice + " §2fiyatına aldın!");
										if (Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().isSet(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".buyAmount"))
										{
											int buyAmount = Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().getInt(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".buyAmount");
											Main.instance.yamlContainer.get(p.getUniqueId()).getYamlConfiguration().set(LocalDate.now() + "." + yaml.getFile().getName().substring(0, yaml.getFile().getName().indexOf(".")) + "." + slot + ".buyAmount", buyAmount - 1);												
										}
									} else {
										p.sendMessage("§cEnvanterinde yeterli alan yok!");
									}
								} else {
									p.sendMessage("§cBu işlemi yapabilmek için satın alma hakkına sahip olmalısın!");
								}
							} else {
								p.sendMessage("§cBu işlemi yapabilmek için §a$" + calculatedBuyPrice + " §ckadar paraya sahip olmalısın!");
							}
						}
						
					});
				}
				
				e.getPlayer().openInventory(i.getInventory());
			}
		} catch (NullPointerException e2) {}
	}
	
}
