package com.btbb.villageinv;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NPCCommands implements CommandExecutor {

	NPCInvPlugin plugin;

	public NPCCommands(NPCInvPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
			return true;
		}
		Player p = (Player) sender;
		String cmd = command.getName().toLowerCase();
		if (cmd.equals("npcinv")) {
			if (args.length < 1 || args[0] == null || args[0].length() < 5) {
				showNPCINVCommand(p, label);
				return true;
			}
			String param = args[0];
			if (param.equalsIgnoreCase("enable")) {
				plugin.enableMap.put(p.getUniqueId(), true);
				if (p.hasPermission("npcinv.edit"))
					p.sendMessage(ChatColor.BLUE + "NPC Inventory editing enabled.");
				else
					p.sendMessage(ChatColor.BLUE + "NPC Inventory viewing enabled.");
				return true;
			} else if (param.equalsIgnoreCase("disable")) {
				plugin.enableMap.put(p.getUniqueId(), false);
				if (p.hasPermission("npcinv.edit"))
					p.sendMessage(ChatColor.BLUE + "NPC Inventory editing disabled.");
				else
					p.sendMessage(ChatColor.BLUE + "NPC Inventory viewing disabled.");
				return true;
			} else {
				showNPCINVCommand(p, label);
				return true;
			}
		}
		return false;
	}

	private void showNPCINVCommand(Player p, String label) {
		p.sendMessage(ChatColor.YELLOW + "/" + label + " <enable/disable>");
	}
}
