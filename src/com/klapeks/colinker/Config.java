package com.klapeks.colinker;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.klapeks.coserver.dFunctions;
import com.klapeks.funcs.FileCfgUtils;

public class Config {
	
	static int bukkit_port;
	static String mode;
	static int afterEnableTime = 5;
	static int timeoutkicktime = 10;
	
	
	
	static final String fs = File.separator;
	
	private static FileWriter fw = null;
	private static FileConfiguration config = null;
	static List<String> commandblocklist = dFunctions.massiveToArray(new String[] {"stop", "end", "bukkit:stop", "disable"});
	static String commandblocktype = "blacklist";
	
	static void __init() throws Exception {
		try {
			File file = new File(Main.plugin.getDataFolder() + fs + "config.yml");
			if (!file.exists()) try { 
				file.getParentFile().mkdirs(); 
				file.createNewFile();
				dFunctions.debug("§6Config was not found; Creating new one");
				fw = new FileWriter(file);
				fw.write("# Config for Bukkit server side" + "\n");
			} catch (Throwable e) { throw new RuntimeException(e); }
			config = YamlConfiguration.loadConfiguration(file);
			if (fw==null) fw = open(file);
			

			Config.bukkit_port = g("port", bFuncs.getCoserverPort(), "The port that allows BungeeCord", "to communicate with this server");
			Config.mode = g("mode", "none", "Server mode (for example: bedwars, hub, survival, skywars)");
			Config.afterEnableTime = g("afterEnableTime", 5, 
									"Time (in seconds) after which the BungeeCord",
									"will know that the server has started up.",
									"Useful if other plugins load something",
									"after server has been started.");
			Config.timeoutkicktime = g("timeoutkicktime", Config.timeoutkicktime, 
									"When player use '/goserver' and no server is responding,",
									"then after the time (in seconds) expires the player",
									"will be kicked from the server",
									"Set 0 to disable this");
			
			Config.commandblocktype = g("commandblocktype", "blacklist", 
									"blacklist: these commands will not be dispatched", 
									"whitelist: only these commands will be dispatched");
			Config.commandblocklist = g("commandblocklist", Config.commandblocklist);
			
			
			fw.flush();
			fw.close();
			config = null; fw = null;
			if (Config.bukkit_port <= 0 || Config.bukkit_port >= 65535) {
				throw new IllegalArgumentException("Port value out of range (0 < " + Config.bukkit_port + " < 65535)");
			}
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	private static FileWriter open(File file) {
		return FileCfgUtils.open(file);
	}
	private static <T> T g(String key, T defaultValue, String... comment) {
		return FileCfgUtils.gb(config, fw, key, defaultValue, comment);
	}
}
