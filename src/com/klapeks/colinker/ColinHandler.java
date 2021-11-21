package com.klapeks.colinker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.Bukkit;

import com.klapeks.colinker.api.CoLinkerAPI;
import com.klapeks.colinker.api.CoLinkerAPI.Event;
import com.klapeks.funcs.dRSA;

public class ColinHandler {

	static List<Consumer<Event>> connecthandlers = new ArrayList<>();
	static void doConnectHandler(Event e) {
		for (Consumer<Event> handler : connecthandlers) {
			handler.accept(e);
		}
	}
	public static void addConnectHandler(Consumer<Event> e) {
		connecthandlers.add(e);
	}
	
	static Map<String, Function<String[], String>> cmdhandlers = new HashMap<>();
	public static void addHandler(String cmd, Function<String[], String> handler) {
		cmdhandlers.put(cmd, handler);
	}
	public static String doHandler(String request) {
		String cmd = request.split(" ")[0];
		if (request.contains(" ")) request = request.substring(cmd.length()+1);
		else request = "";
		cmd = dRSA.base64_decode(cmd);
		if (cmdhandlers.containsKey(cmd)) 
			return cmdhandlers.get(cmd).apply(doArgs(request));
		return "nohandle";
	}
	private static String[] doArgs(String req) {
		if (req.equals("")) return new String[] {};
		String[] ss = req.split(" ");
		for (int i = 0; i < ss.length; i++) {
			try { 
				ss[i] = dRSA.base64_decode(ss[i]);
			} 
			catch (Throwable r) {}
		}
		return ss;
	}
	static void loadDefaultHandlers() {
		CoLinkerAPI.addHandler("command", (args) -> {
			String cmd = args[0].toLowerCase();
			if (Config.commandblocklist.contains(cmd)) {
				if (Config.commandblocktype.equals("blacklist")) {
					return "§4[Colinker] §cThis command in blacklist";
				}
			} else {
				if (Config.commandblocktype.equals("whitelist")) {
					return "§4[Colinker] §cThis command not in whitelist";
				}
			}
			cmd = "";
			for (String s : args) {
				cmd += " " + s;
			}
			cmd = cmd.replaceFirst(" ", "");
			ColinConsoleSender ccs = new ColinConsoleSender();
			Bukkit.dispatchCommand(ccs, cmd);
			return ccs.getResponse();
		});
		CoLinkerAPI.addHandler("isalive", (args) -> "yesiamalive");
	}
}
