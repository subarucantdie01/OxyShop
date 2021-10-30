package com.gmail.subarucantdie.OxyShop;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class YAML {
	
	private File file;
	private YamlConfiguration yamlConfiguration;
	
	// NON-DEPRECATED METHODS
	
	public YAML(String path, String fileNameWithExtension) {
		file = new File(path + File.separator + fileNameWithExtension);
		
		if (!file.getParentFile().exists())
			file.getParentFile().mkdir();
			
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				Bukkit.broadcastMessage("§7[OxyShop] §cYaml dosyası oluşturulamadı! §e" + file.getPath());
				return;
			}

		yamlConfiguration = YamlConfiguration.loadConfiguration(file);
	}
	
	public YamlConfiguration getYamlConfiguration() {
		return yamlConfiguration;
	}
	
	public File getFile() {
		return file;
	}
	
	public static boolean isYamlExists(String path, String fileNameWithExtension) {
		return new File(path + File.separator + fileNameWithExtension).exists();
	}
	
	public boolean isEmpty() {
		return file.length() == 0;
	}
	
	public void save() throws IOException {
		yamlConfiguration.save(file);
	}
	
	public void load() throws IOException, InvalidConfigurationException {
		yamlConfiguration.load(file);
	}
	
	public void saveAndLoad() throws IOException, InvalidConfigurationException {
		save();
		load();
	}
	
	public static YAML[] getAllYAMLsInsideDir(String dirPath) {
		File[] listFiles = new File(dirPath).listFiles();
		YAML[] yamlArray = new YAML[listFiles.length];
		
		for (int i = 0; i < listFiles.length; i++) {
			if (!listFiles[i].isDirectory()) {
				yamlArray[i] = new YAML(listFiles[i].getParent(), listFiles[i].getName());				
			}
		}
		
		return yamlArray;
	}
	
	
	// DEPRECATED METHODS
	
	@Deprecated
	public boolean isBothExists() {
		return file.exists() && yamlConfiguration != null;
	}
	
	@Deprecated
	public static YAML loadYaml(String path, String fileNameWithExtension, String loadingText, String loadedText) {
		Bukkit.broadcastMessage(loadingText);
		YAML yaml = new YAML(path, fileNameWithExtension);
		Bukkit.broadcastMessage(loadedText);
		return yaml;
	}
	
}
