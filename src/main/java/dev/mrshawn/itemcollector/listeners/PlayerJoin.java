package dev.mrshawn.itemcollector.listeners;

import dev.mrshawn.itemcollector.ItemCollector;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

	private ItemCollector main;

	public PlayerJoin(ItemCollector main) {
		this.main = main;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (!main.getDataFile().contains(player.getUniqueId())) {
			main.getDataFile().createPlayer(player.getUniqueId());
		}
		main.getDataFile().loadPlayer(player.getUniqueId());
	}

}
