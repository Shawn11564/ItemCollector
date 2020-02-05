package dev.mrshawn.itemcollector.commands;

import dev.mrshawn.itemcollector.ItemCollector;
import dev.mrshawn.itemcollector.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemCollectorCMD implements CommandExecutor {

	private ItemCollector main;

	public ItemCollectorCMD(ItemCollector main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		// /itemcollector setcomplete <player> <true / false>
		if (args.length >= 3) {
			if (args[0].equalsIgnoreCase("setcomplete")) {
				if (Bukkit.getPlayer(args[1]) != null) {
					Player player = Bukkit.getPlayer(args[1]);
					if (args[2].equalsIgnoreCase("true")) {
						main.getDataFile().setComplete(player.getUniqueId(), true);
						main.processCompleted(player.getUniqueId());
						Chat.tell(sender, "&aSet &6" + player.getName() + "&a's status to: &2&lCOMPLETE!");
						return true;
					} else if (args[2].equalsIgnoreCase("false")) {
						main.getDataFile().setComplete(player.getUniqueId(), false);
						Chat.tell(sender, "&aSet &6" + player.getName() + "&a's status to: &c&lINCOMPLETE!");
						return true;
					}
					Chat.tell(sender, "&cUsage: /itemcollector setcomplete <player> <true/false>");
				}
			}
		} else {
			Chat.tell(sender, "&cUsage: /itemcollector setcomplete <player> <true/false>");
		}

		return false;
	}

}
