package com.klapeks.colinker;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGoServer implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("colinker.command.goserver")) {
			String mode_server = args[0];
			Player p = null;
			if (sender instanceof Player) {
				p = (Player) sender;
				if (args.length >= 2) {
					p = find(args[1]);
				}
			} else {
				if (args.length < 2) {
					sender.sendMessage("§cPlease select a player. /goserver "+mode_server+" <player>");
					return true;
				}
				p = find(args[1]);
			}
			if (p == null) {
				sender.sendMessage("§cPlayer wasn't found");
				return true;
			}
			bFuncs.connectToServer(p, mode_server);
		}
		return true;
	}
	private static Player find(String playername) {
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (pl.getName().equalsIgnoreCase(playername)) {
				return pl;
			}
		}
		return null;
	}
}
