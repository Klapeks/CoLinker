package com.klapeks.colinker.bungee;

import java.io.File;
import java.io.FileWriter;

import com.klapeks.coserver.dFunctions;
import com.klapeks.funcs.FileCfgUtils;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Config {
	
	static class lang {
		static String alldisabled = "§4§oConfig error";
		static String notfound = "§4§oConfig error";
	}
	
	static boolean alivechecker = true;
	static String hubmode = "hub";
//	static String id_format;
	static boolean sum_online = true;
	public static boolean isSumOnline() {
		return sum_online;
	}
//	public static String getIdFormat() {
//		return id_format;
//	}
	
	
	static final String fs = File.separator;
	
	private static FileWriter fw = null;
	private static Configuration config = null;
	
	static void __init() throws Exception {
		try {
			File file = new File(MainBungee.plugin.getDataFolder() + fs + "config.yml");
			if (!file.exists()) try { 
				file.getParentFile().mkdirs(); 
				file.createNewFile();
				dFunctions.debug("§6Config was not found; Creating new one");
				fw = new FileWriter(file);
				fw.write("# Config for Bukkit server side" + "\n");
			} catch (Throwable e) { throw new RuntimeException(e); }
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			if (fw==null) fw = open(file);
			
//			Config.id_format = g("idformat", "{M}{hex}", "Id that will have your servers.",
//								"For example:",
//								"  Server:\n    Name: Hub\n    Index: 6734",
//								"  Format: {M}_{HEX}  ->  H_1A4E",
//								"  Format: {mode}_{hex}  ->  hub_1a4e",
//								"  Format: {dec}{m}  ->  6734h",
//								"All placeholders:",
//								"  M: H (First letter, UPPER CASE)",
//								"  m: h (First letter, lower case)",
//								"  MODE: HUB (All word, UPPER CASE)",
//								"  Mode: Hub (All word, Lower case)",
//								"  mode: hub (All word, lower case)",
//								"  HEX: 1A4E (id -> hex, UPPER CASE)",
//								"  hex: 1a4e (id -> hex, lower case)",
//								"  dec: 6734 (id -> dec)");

			Config.sum_online = g("sum_online", Config.sum_online, "Will the server list show the sum of the maximum online of all servers");
			Config.alivechecker = g("alivechecker", Config.alivechecker, "If true: all connected servers will be checked for alive (in case they crashed)");

			Config.lang.alldisabled = g("alldisabled", "Servers are currently disabled");
			Config.lang.notfound = g("notfound", "Server wasn't found");
			Config.hubmode = g("hubmode", hubmode);
			
			
			
			fw.flush();
			fw.close();
			config = null; fw = null;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	private static FileWriter open(File file) {
		return FileCfgUtils.open(file);
	}
	private static <T> T g(String key, T defaultValue, String... comment) {
		return FileCfgUtils.gbg(config, fw, key, defaultValue, comment);
	}
}
