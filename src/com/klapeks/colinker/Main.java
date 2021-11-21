package com.klapeks.colinker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.klapeks.coserver.IMLPack;
import com.klapeks.coserver.aConfig;
import com.klapeks.coserver.dFunctions;
import com.klapeks.coserver.plugin.bungee.SuperCoServer;

public class Main extends JavaPlugin implements IMLPack<JavaPlugin> {
	
	public Main() { init(this); }
	@Override public void onLoad() { load(this); }
	@Override public void onDisable() { disable(this); }
	@Override public void onEnable() { enable(this); }

	public static SuperCoServer bukkitServer;
	public static JavaPlugin plugin;
	
	@Override
	public void init(JavaPlugin plugin) {
		Main.plugin = plugin;
		try {
			Config.__init();
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().severe("Config couldn't be loaded :(");
			return;
		}
		bukkitServer = new SuperCoServer(Config.bukkit_port);
		if (aConfig.useSecurity) bukkitServer.addSecurityHandler("colinkertos", ColinHandler::doHandler);
		else bukkitServer.addHandler("colinkertos", ColinHandler::doHandler);
		ColinServer.makeInstance();
	}

	@Override
	public void load(JavaPlugin plugin) {
		ColinHandler.loadDefaultHandlers();
		ColinServer.getInstance().doLoadRequest();
	}

	@Override
	public void enable(JavaPlugin plugin) {
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getCommand("goserver").setExecutor(new CommandGoServer());
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				try {
					ColinServer.getInstance().doEnableRequest();
				} catch (Throwable t) {
					t.printStackTrace();
					if (aConfig.shutdownOnError) {
						Functions.log("§cServer will be disabled, to prevent further errors");
						dFunctions.shutdown();
					}
				}
			}
		}, Config.afterEnableTime*20);
	}
	
	@Override
	public void disable(JavaPlugin plugin) {
		ColinServer.getInstance().doDisableRequest();
		Functions.log("Thank you and goodbye :)");
	}
}
