package com.klapeks.colinker;

import org.bukkit.Bukkit;

import com.klapeks.colinker.api.CoServer;
import com.klapeks.colinker.api.CoLinkerAPI.Event;
import com.klapeks.coserver.aConfig;
import com.klapeks.coserver.dCoserver;
import com.klapeks.funcs.dRSA;

public class ColinServer implements CoServer {
	
	private static ColinServer colinServer = null;
	public static ColinServer getInstance() {
		return colinServer;
	}
	public static ColinServer makeInstance() {
		if (colinServer == null) {
			colinServer = new ColinServer(Config.mode);
		}
		return colinServer;
	}
	/**
	 * 0x - full
	 * 1x - First Letter in Upper Case
	 * 2x - first letter in lower case
	 * x0 - fullid
	 * x1 - hexid
	 */
	
	private ServerState state = ServerState.LOADING;
	private int id = -1;
	private final String mode;
	
	private int maxplayers;
	ColinServer(String mode) {
		this.mode = mode;
		this.maxplayers = Bukkit.getMaxPlayers();
	}

	@Override
	public String getMode() {
		return mode;
	}
	@Override
	public int getId() {
		return id;
	}
	
	void setState(ServerState state) {
		this.state = state;
	}
	@Override
	public ServerState getState() {
		return state;
	}

	void doLoadRequest() {
		ColinHandler.doConnectHandler(Event.PRELOAD);
		setState(ServerState.LOADING);
		String resp = send("loading", mode, Config.bukkit_port, Bukkit.getPort());
		int id = Integer.parseInt(resp.split(" ")[0]);
		this.id = id;
		ColinHandler.doConnectHandler(Event.INLOADING);
	}
	void doEnableRequest() {
		String resp = send("enabled", id, getMaxPlayers());
		if (resp.equals("fail")) {
			throw new RuntimeException("Server wasn't found at BungeeCord side. Please try to reload BungeeCord and Bukkit. If you still get this error please contact with creators.");
		}
		setState(ServerState.ENABLED);
		ColinHandler.doConnectHandler(Event.CONNECT);
	}
	void doDisableRequest() {
		setState(ServerState.DISABLING);
		ColinHandler.doConnectHandler(Event.DISCONNECT);
		String resp = send("disabled", id, getMaxPlayers());
		Functions.log(resp);
	}
	@Override
	public int getMaxPlayers() {
		if (this.maxplayers==0) this.maxplayers = Bukkit.getMaxPlayers();
		return this.maxplayers;
	}
	public void setMaxPlayers(int maxplayers) {
		this.maxplayers = maxplayers;
	}


	
	static String send(String cmd, Object... args) {
		cmd = dRSA.base64_encode(cmd);
		for (Object arg : args) {
			cmd += " " + dRSA.base64_encode(arg+"");
		}
		if (aConfig.useSecurity) {
			return dCoserver.securitySend(aConfig.bukkit.ip, aConfig.bukkit.port, "colinker " + cmd, false);
		} else {
			return dCoserver.send(aConfig.bukkit.ip, aConfig.bukkit.port, "colinker " + cmd, false);
		}
	}
	
//	private static String sendLarge(String cmd, Object... args) {
//		cmd = dRSA.base64_encode(cmd);
//		for (Object arg : args) {
//			cmd += " " + dRSA.base64_encode(arg+"");
//		}
//		if (aConfig.useSecurity) {
//			return dCoserver.securitySend(aConfig.bukkit.ip, aConfig.bukkit.port, "colinker " + cmd, true);
//		} else {
//			return dCoserver.send(aConfig.bukkit.ip, aConfig.bukkit.port, "colinker " + cmd, true);
//		}
//	}
//	private static void closeLarge() {
//		dCoserver.closeLarge(aConfig.bukkit.ip, aConfig.bukkit.port);
//	}

}
