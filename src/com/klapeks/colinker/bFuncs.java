package com.klapeks.colinker;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class bFuncs {

	static int getCoserverPort() {
		try {
			File file = new File("server.properties");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while((line=br.readLine())!=null) {
				if (line.startsWith("rcon.port")) {
					br.close();
					return Integer.parseInt(line.split("=")[1]);
				}
			}
			br.close();
		} catch (Throwable tr) {
			tr.printStackTrace();
		}
		return Bukkit.getPort()-3000;
	}

	public static ArrayList<String> playersinbungee = new ArrayList<String>();
	public static boolean connectToServer(Player p, String mode_server, Consumer<String> ifNotConnected) {
		String servername = ColinServer.send("playerconnect", mode_server, ColinServer.getInstance().getId());
		if (servername == null || servername.equals("null") || servername.equals("$fail")) {
			p.sendMessage("§cServer wasn't found");
			return false;
		}
		String playername = p.getName();
		if (!playersinbungee.contains(playername)) playersinbungee.add(playername);
		Runnable run = () -> {
			if (playersinbungee.contains(playername)) ifNotConnected.accept(servername);
			playersinbungee.remove(playername);
		};
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(b);
				try {
					out.writeUTF("Connect");
					out.writeUTF(servername);
					p.sendPluginMessage(Main.plugin, "BungeeCord", b.toByteArray());
					if (Config.timeoutkicktime>0) Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, run, Config.timeoutkicktime*20);
				}catch (Throwable ex) {
					ex.printStackTrace();
					run.run();
				}
			}
		}, 10);
		return true;
	}

	public static boolean connectToServer(Player p, String mode_server) {
		return connectToServer(p, mode_server, (servername) -> {
			if (p.isOnline()) {
				p.kickPlayer("%em%err_connect:" + servername + "");
			}
		});
	}
}
