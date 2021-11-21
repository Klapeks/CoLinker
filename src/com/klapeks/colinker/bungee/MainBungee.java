package com.klapeks.colinker.bungee;

import com.klapeks.coserver.IMLPack;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class MainBungee extends Plugin implements IMLPack<Plugin>{
	
	public static Plugin plugin;
	
	public MainBungee() { init(this); }
	@Override public void onLoad() { load(this); }
	@Override public void onDisable() { disable(this); }
	@Override public void onEnable() { enable(this); }

	
	@Override
	public void init(Plugin plugin) {
		MainBungee.plugin = plugin;
		try {
			Config.__init();
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().severe("Config couldn't be loaded :(");
			return;
		}
		ColinHandler.__init();
	}

	@Override
	public void load(Plugin plugin) {
		
	}
	static boolean isEnabled = false;
	@Override
	public void enable(Plugin plugin) {
		isEnabled = true;
		getProxy().getPluginManager().registerCommand(this, new BungeeBroadcastCMDCommand());
		if (Config.alivechecker) {
			ProxyServer.getInstance().getScheduler().runAsync(this, () -> {
				while(true) {
					try {
						Thread.sleep(60*1000);
						ColinServer.recalculateServers();
					} catch (InterruptedException e) {
						if (isEnabled) e.printStackTrace();
					}
				}
			});
		}
		if (Config.sum_online) {
			getProxy().getPluginManager().registerListener(this, new EventListeners.PingListener());
			getProxy().getPluginManager().registerListener(this, new EventListeners.ConnectListener());
		}
	}

	@Override
	public void disable(Plugin plugin) {
		isEnabled = false;
	}

}
