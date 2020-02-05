package dev.mrshawn.itemcollector.listeners;

import dev.mrshawn.itemcollector.ItemCollector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuit implements Listener {

	private ItemCollector main;

	public PlayerQuit(ItemCollector main) {
		this.main = main;
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {

		UUID uuid = e.getPlayer().getUniqueId();

		if (main.getCollected().containsKey(uuid)) {
			main.getDataFile().savePlayer(uuid, main.getCollected().get(uuid));
		}
	}

}
