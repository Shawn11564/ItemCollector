package dev.mrshawn.itemcollector.scheduler;

import dev.mrshawn.itemcollector.ItemCollector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryCollector {

	private ItemCollector main;

	public InventoryCollector(ItemCollector main) {
		this.main = main;
	}

	public void startInventoryChecker() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					main.addItems(player.getUniqueId(), player.getInventory().getContents());

					if (main.getCollected().get(player.getUniqueId()).size() >= main.getMaterialList().size()) {
						if (!main.getDataFile().isComplete(player.getUniqueId())) {
							main.processCompleted(player.getUniqueId());
						}
					}

				}
			}
		}.runTaskTimer(main, 0L, 20L * main.getConfig().getInt("inventory-delay-seconds"));
	}
}
