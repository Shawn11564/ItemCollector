package dev.mrshawn.itemcollector;

import co.aikar.commands.PaperCommandManager;
import dev.mrshawn.itemcollector.commands.ItemCollectorCMD;
import dev.mrshawn.itemcollector.files.DataFile;
import dev.mrshawn.itemcollector.listeners.PlayerJoin;
import dev.mrshawn.itemcollector.listeners.PlayerQuit;
import dev.mrshawn.itemcollector.scheduler.InventoryCollector;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class ItemCollector extends JavaPlugin {

	private static ItemCollector instance;
	private DataFile dataFile;
	private InventoryCollector inventoryCollector;

	private Set<Material> materialList = new HashSet<>();
	private Map<UUID, Set<Material>> collected;

	@Override
	public void onEnable() {
		instance = this;
		loadMaterialList();
		getConfig().options().copyDefaults();
		saveDefaultConfig();

		dataFile = new DataFile(this);
		collected = dataFile.getLoadedPlayers();

		inventoryCollector = new InventoryCollector(this);
		inventoryCollector.startInventoryChecker();

		getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
		getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
		PaperCommandManager pcm = new PaperCommandManager(this);
		pcm.registerCommand(new ItemCollectorCMD(this));
	}

	@Override
	public void onDisable() {
		for (Map.Entry<UUID, Set<Material>> entries : collected.entrySet()) {
			dataFile.savePlayer(entries.getKey(), entries.getValue());
		}
		dataFile.save();
	}

	public static ItemCollector getInstance() {
		return instance;
	}

	private void loadMaterialList() {
		for (Material material : Material.values()) {
			if (material.isItem() && !material.name().startsWith("LEGACY")) {
				if (!blacklisted(material)) {
					if (getConfig().getBoolean("include-spawn-eggs")) {
						materialList.add(material);
					} else {
						if (!material.name().contains("SPAWN_EGG")) {
							materialList.add(material);
						}
					}
				}
			}
		}
	}

	public boolean blacklisted(Material material) {
		for (String s : getConfig().getStringList("blacklisted-items")) {
			if (material.name().equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	public void addItems(UUID uuid, ItemStack[] items) {
		Set<Material> materials;
		if (collected.containsKey(uuid)) {
			materials = collected.get(uuid);
		} else {
			materials = new HashSet<>();
		}

		for (ItemStack item : items) {
			if (item != null) {
				if (blacklisted(item.getType())) {
					continue;
				}

				if (getConfig().getBoolean("include-spawn-eggs")) {
					materials.add(item.getType());
				} else if (!item.getType().name().contains("SPAWN_EGG")) {
					materials.add(item.getType());
				}

			}
		}

		collected.put(uuid, materials);
	}

	public void processCompleted(UUID uuid) {
		dataFile.setComplete(uuid, true);
		dataFile.savePlayer(uuid, collected.get(uuid));

		for (String s : getConfig().getStringList("complete-commands")) {
			getServer().dispatchCommand(getServer().getConsoleSender(), s.replace("%player%", getServer().getPlayer(uuid).getName()));
		}

	}

	public Map<UUID, Set<Material>> getCollected() {
		return collected;
	}

	public DataFile getDataFile() {
		return dataFile;
	}

	public Set<Material> getMaterialList() {
		return materialList;
	}
}
