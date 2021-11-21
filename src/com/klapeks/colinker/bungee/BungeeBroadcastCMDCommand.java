package com.klapeks.colinker.bungee;

import com.klapeks.colinker.Functions;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.command.ConsoleCommandSender;

public class BungeeBroadcastCMDCommand extends Command{
	
	public BungeeBroadcastCMDCommand() {
		super("broadcastcmd", null, new String[] {"broadcastcommand"});
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			if (args.length < 2) {
				sender.sendMessage(new TextComponent("§cUsage: /broadcastcmd <mode> <command> [args]"));
				return;
			}
			String mode = args[0];
			if (!ColinServer.mode$server.containsKey(mode)) {
				sender.sendMessage(new TextComponent(Functions.formater("§cServers with mode {0} weren't found", mode)));
				return;
			}
			Object[] a = new Object[args.length-1];
			for (int i = 0; i < args.length-1; i++) {
				a[i] = args[i+1];
			}
			String answer = "";
			for (ColinServer cs : ColinServer.mode$server.get(mode)) {
				answer = cs.sendData("command", a);
			}
			sender.sendMessage(new TextComponent("§eResponse from one of servers:§r\n"+answer));
		}
	}
	
}
