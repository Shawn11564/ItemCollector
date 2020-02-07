package dev.mrshawn.itemcollector.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import dev.mrshawn.itemcollector.ItemCollector;
import dev.mrshawn.itemcollector.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("itemcollecter|ic")
public class ItemCollectorCMD extends BaseCommand {

	private ItemCollector main;

	public ItemCollectorCMD(ItemCollector main) {
		this.main = main;
	}

	@Subcommand("setcomplete")
	@CommandPermission("itemcollector.admin")
	public void onSetComplete(CommandSender sender, String[] args) {
		if (args.length >= 2) {
			if (Bukkit.getPlayer(args[0]) != null) {
				Player player = Bukkit.getPlayer(args[0]);
				if (args[1].equalsIgnoreCase("true")) {
					main.getDataFile().setComplete(player.getUniqueId(), true);
					main.processCompleted(player.getUniqueId());
					Chat.tell(sender, "&aSet &6" + player.getName() + "&a's status to: &2&lCOMPLETE!");
					return;
				} else if (args[1].equalsIgnoreCase("false")) {
					main.getDataFile().setComplete(player.getUniqueId(), false);
					Chat.tell(sender, "&aSet &6" + player.getName() + "&a's status to: &c&lINCOMPLETE!");
					return;
				}
			}
		}
		Chat.tell(sender, "&cUsage: /itemcollector setcomplete <player> <true/false>");
	}

	@Subcommand("check")
	@Default
	@CommandPermission("itemcollector.check")
	public void onCheck(Player sender, String[] args) {
		if (args.length == 0) {
			Chat.tell(sender, main.getConfig().getString("status-message")
					.replace("%player%", sender.getName())
					.replace("%collected%", String.valueOf(main.getCollected().get(sender.getUniqueId()).size()))
					.replace("%max%", String.valueOf(main.getMaterialList().size())));
		} else if (Bukkit.getPlayer(args[0]) != null) {
			if (sender.hasPermission("itemcollector.check.others")) {
				Player player = Bukkit.getPlayer(args[0]);
				Chat.tell(sender, main.getConfig().getString("status-message")
						.replace("%player%", player.getName())
						.replace("%collected%", String.valueOf(main.getCollected().get(player.getUniqueId()).size()))
						.replace("%max%", String.valueOf(main.getMaterialList().size())));
			} else {
				Chat.tell(sender, "&cYou do not have permission to check other's progress!");
			}
		} else {
			Chat.tell(sender, "&cUnable to find player: &4" + args[1]);
		}
	}

}
