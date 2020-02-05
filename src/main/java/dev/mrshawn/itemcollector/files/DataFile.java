package dev.mrshawn.itemcollector.files;

import dev.mrshawn.itemcollector.ItemCollector;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataFile {

	private ItemCollector main;
	private File file;
	private YamlConfiguration config;

	public DataFile(ItemCollector main) {
		this.main = main;
		file = new File(main.getDataFolder() + File.separator + "data.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (final IOException ex) {
				ex.printStackTrace();
				main.getServer().getPluginManager().disablePlugin(main);
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}

	public Map<UUID, Set<Material>> getLoadedPlayers() {
		Map<UUID, Set<Material>> allPlayers = new HashMap<>();
		Set<Material> collected = new HashSet<>();
		for (String s : config.getKeys(false)) {
			ConfigurationSection cs = config.getConfigurationSection(s);
			for (String matName : cs.getStringList("collected")) {
				collected.add(Material.valueOf(matName.toUpperCase()));
			}
			allPlayers.put(UUID.fromString(s), collected);
			collected = new HashSet<>();
		}
		return allPlayers;
	}

	public void createPlayer(UUID uuid) {
		config.set(uuid + ".complete", false);
	}

	public void loadPlayer(UUID uuid) {
		Set<Material> collected = new HashSet<>();
		if (config.getConfigurationSection(uuid.toString() + ".collected") != null) {
			for (String s : config.getConfigurationSection(uuid.toString() + ".collected").getKeys(false)) {
				collected.add(Material.valueOf(s.toUpperCase()));
			}
		}
		main.getCollected().put(uuid, collected);
	}

	public void savePlayer(UUID uuid, Set<Material> collected) {
		List<String> types = new ArrayList<>();
		for (Material material : collected) {
			types.add(material.name());
		}
		config.set(uuid + ".collected", types);

		try {
			config.save(file);
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}

	public void save() {
		try {
			config.save(file);
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}

	public boolean contains(UUID uuid) {
		return config.getKeys(false).contains(uuid.toString());
	}

	public void setComplete(UUID uuid, boolean complete) {
		config.set(uuid + ".complete", complete);
	}

	public boolean isComplete(UUID uuid) {
		return config.getBoolean(uuid + ".complete");
	}
}
